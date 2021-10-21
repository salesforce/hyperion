package com.krux.hyperion.aws

import org.json4s.JsonDSL._
import org.scalatest.wordspec.AnyWordSpec

class AdpDataFormatsSpec extends AnyWordSpec {
  "TsvDataFormat" should {
    "converts to Json" in {
      val testObj = AdpTsvDataFormat(
        "tsv",
        None,
        None,
        Some("\\")
      )
      val objShouldBe = ("id" -> "tsv") ~
        ("escapeChar" -> "\\") ~
        ("type" -> "TSV")

      assert(AdpJsonSerializer(testObj) === objShouldBe)
    }
  }
}
