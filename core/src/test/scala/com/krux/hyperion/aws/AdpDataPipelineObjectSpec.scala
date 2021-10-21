package com.krux.hyperion.aws

import org.json4s.JsonDSL._
import org.scalatest.wordspec.AnyWordSpec

class AdpDataPipelineObjectSpec extends AnyWordSpec {

  class TestObject extends AdpDataPipelineObject {
    val id: String = "TestId"
    val name: Option[String] = None
    val `type`: String = "TestObject"
  }

  class TestObjectWithName extends AdpDataPipelineObject {
    val id: String = "TestId"
    val name: Option[String] = Option("TestName")
    val `type`: String = "TestObject"
  }

  "DataPipelineObject" should {

    "produce JSON" in {
      val resultShouldBe = ("id" -> "TestId") ~ ("type" -> "TestObject")
      val testObj = new TestObject()
      assert(AdpJsonSerializer(testObj) === resultShouldBe)
    }

  }
}
