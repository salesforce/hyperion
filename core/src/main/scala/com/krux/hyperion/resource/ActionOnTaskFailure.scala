/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.resource

trait ActionOnTaskFailure {
  def serialize: String
  override def toString = serialize
}

case object ContinueOnTaskFailure extends ActionOnTaskFailure {
  val serialize: String = "continue"
}

case object TerminateOnTaskFailure extends ActionOnTaskFailure {
  val serialize: String = "terminate"
}
