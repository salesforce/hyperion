package com.krux.hyperion.objects.aws

import org.scalatest.WordSpec
import org.json4s.JsonDSL._
import com.github.nscala_time.time.Imports.DateTime

class AdpScheduleSpec extends WordSpec {

  "AdpStartAtSchedule" should {
    "converts to Json" in {
      val testObj = AdpStartAtSchedule("theId", None, "1 day", "FIRST_ACTIVATION_DATE_TIME", Some("1"))
      val objShouldBe = ("id" -> "theId") ~
        ("period" -> "1 day") ~
        ("startAt" -> "FIRST_ACTIVATION_DATE_TIME") ~
        ("occurrences" -> "1") ~
        ("type" -> "Schedule")

      assert(AdpJsonSerializer.apply(testObj) === objShouldBe)
    }
  }

  "AdpStartDateTimeSchedule" should {
    val testObj = AdpStartDateTimeSchedule(
      "theId", Some("SomeName"), "1 day", new DateTime("2014-04-02T00:00:00Z"), None)
    val objShouldBe = ("id" -> "theId") ~
      ("name" -> "SomeName") ~
      ("period" -> "1 day") ~
      ("startDateTime" -> "2014-04-02T00:00:00") ~
      ("type" -> "Schedule")

    assert(AdpJsonSerializer.apply(testObj) === objShouldBe)
  }

}
