package com.krux.hyperion

import org.scalatest.wordspec.AnyWordSpec

import com.krux.hyperion.common.{ NameGroupObjectId, RandomizedObjectId }

class PipelineObjectIdSpec extends AnyWordSpec {

  "RandomizedObjectId" should {
    "generate a stable id" in {
      val r = RandomizedObjectId("seed")
      assert(r.toString == r.copy().toString)
    }
  }

  "NameGroupObjectId" should {
    "generate a stable id with name" in {
      val n = NameGroupObjectId("name", "")
      assert(n.toString == n.copy().toString)
    }

    "generate a stable id with group" in {
      val g = NameGroupObjectId("", "g")
      assert(g.toString == g.copy().toString)
    }
  }

}
