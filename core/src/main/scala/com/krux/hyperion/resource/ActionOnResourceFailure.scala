package com.krux.hyperion.resource

trait ActionOnResourceFailure

case object RetryAllOnResourceFailure extends ActionOnResourceFailure {
  override val toString = "retryall"
}

case object RetryNoneOnResourceFailure extends ActionOnResourceFailure {
  override val toString = "retrynone"
}
