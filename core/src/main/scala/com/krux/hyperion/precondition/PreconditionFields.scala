package com.krux.hyperion.precondition

import com.krux.hyperion.adt.{ HDuration, HString, HInt }
import com.krux.hyperion.action.Action

case class PreconditionFields(
  role: HString,
  preconditionTimeout: Option[HDuration] = None,
  maximumRetries: Option[HInt] = None,
  onFail: Seq[Action] = Seq.empty,
  onLateAction: Seq[Action] = Seq.empty,
  onSuccess: Seq[Action] = Seq.empty
)
