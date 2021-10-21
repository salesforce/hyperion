package com.krux.hyperion.activity

import com.krux.hyperion.common.S3Uri
import com.krux.hyperion.HyperionContext
import com.krux.hyperion.Implicits._
import com.typesafe.config.ConfigFactory
import org.scalatest.wordspec.AnyWordSpec


class SparkStepSpec extends AnyWordSpec {
  class SomeClass

  object SomeObject

  implicit val hc: HyperionContext = new HyperionContext(ConfigFactory.load("example"))

  "SparkStepSpec" should {
    "allow mainClass from a String" in {
      val name = "com.foo.SomeClass"
      val ja = SparkStep(S3Uri("s3://something.jar"))
        .withMainClass(name)
        .withDriverMemory(9.gigabytes)
      assert(ja.mainClass === None)
      assert(ja.sparkMainClass.map(_.toString) == Some(name))
    }

    "allow mainClass an instance" in {
      val ja = SparkStep(S3Uri("s3://something.jar")).withMainClass(new SomeClass())
      assert(ja.mainClass === None)
      assert(ja.sparkMainClass.map(_.toString) == Some("com.krux.hyperion.activity.SparkStepSpec.SomeClass"))
    }

    "allow mainClass an object" in {
      val ja = SparkStep(S3Uri("s3://something.jar")).withMainClass(SomeObject)
      assert(ja.mainClass === None)
      assert(ja.sparkMainClass.map(_.toString) == Some("com.krux.hyperion.activity.SparkStepSpec.SomeObject"))
    }

    "allow mainClass a Class" in {
      val ja = SparkStep(S3Uri("s3://something.jar")).withMainClass(SomeObject.getClass)
      assert(ja.mainClass === None)
      assert(ja.sparkMainClass.map(_.toString) == Some("com.krux.hyperion.activity.SparkStepSpec.SomeObject"))
    }
  }
}
