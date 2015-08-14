package com.krux.hyperion.resource

trait ActionOnTaskFailure

case object ContinueOnTaskFailure extends ActionOnTaskFailure {
  override val toString = "continue"
}

case object TerminateOnTaskFailure extends ActionOnTaskFailure {
  override val toString = "terminate"
}
