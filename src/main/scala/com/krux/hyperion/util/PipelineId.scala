package com.krux.hyperion.util

import scala.collection.mutable.{Set => MutableSet}
import java.util.UUID

object PipelineId {

  def generateNewId(prefix: String): String = prefix + "_" + UUID.randomUUID.toString

}
