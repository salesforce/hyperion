package com.krux.hyperion.objects

import com.krux.hyperion.HyperionContext
import com.krux.hyperion.objects.aws.{AdpTerminate, AdpRef}

object Terminate extends PipelineObject {

  val id = TerminateObjectId

  def serialize = new AdpTerminate()
  def ref: AdpRef[AdpTerminate] = AdpRef(serialize)

}
