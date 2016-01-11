package com.krux.hyperion.action

import com.krux.hyperion.common.PipelineObject
import com.krux.hyperion.aws.{ AdpAction, AdpRef }

trait Action extends PipelineObject {

  type Self <: Action

  def serialize: AdpAction

  def ref: AdpRef[AdpAction] = AdpRef(serialize)

}
