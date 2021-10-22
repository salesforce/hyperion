/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion

import com.krux.hyperion.PipelineLifeCycle.Status

trait PipelineLifeCycle {

  def onCreated(id: String, name: String, status: Status.Value): Unit = {
  }

  def onUploaded(id: String, name: String, status: Status.Value): Unit = {
  }

}

object PipelineLifeCycle {
  object Status extends Enumeration {
    val Success, Fail, SuccessWithWarnings = Value
  }
}
