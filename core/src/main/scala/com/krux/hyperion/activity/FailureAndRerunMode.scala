/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

trait FailureAndRerunMode {

  def serialize: String

  override def toString: String = serialize

}

object FailureAndRerunMode {

  case object CascadeOnFailure extends FailureAndRerunMode {
    val serialize = "cascade"
  }

  case object None extends FailureAndRerunMode {
    val serialize = "none"
  }
}
