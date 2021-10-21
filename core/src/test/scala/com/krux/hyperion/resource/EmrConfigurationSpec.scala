package com.krux.hyperion.resource

import org.scalacheck.Arbitrary
import org.scalacheck.Gen._
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

import com.krux.hyperion.adt.HType
import com.krux.hyperion.aws.AdpEmrConfiguration

object EmrConfigurationSpec extends Properties("String") {

  implicit val propGen = Arbitrary(
    listOf(
      for {
        key <- alphaStr
        value <- alphaStr
      } yield Property(HType.string2HString(key), HType.string2HString(value))
    )
  )

  implicit val emrConfigGen = Arbitrary(
    listOf(for { classification <- alphaStr } yield EmrConfiguration(classification))
  )

  property("Non-empty configuration and property") = forAll {
    (ps: List[Property], configs: List[EmrConfiguration], classification: String) =>
      val emrConfig = EmrConfiguration(classification)
        .withConfiguration(configs: _*)
        .withProperty(ps: _*)

      val ser = emrConfig.serialize

      ser match {
        case AdpEmrConfiguration(_, _, _, prop, conf) =>
          conf.forall(_.size > 0) && prop.forall(_.size > 0)
      }
  }

}
