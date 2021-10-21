/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

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
