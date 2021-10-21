/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.action

import com.krux.hyperion.aws.{ AdpTerminate, AdpRef }
import com.krux.hyperion.common.{ TerminateObjectId, PipelineObject }

/**
 * An action to trigger the cancellation of a pending or unfinished activity, resource, or data
 * node. AWS Data Pipeline attempts to put the activity, resource, or data node into the CANCELLED
 * state if it does not finish by the lateAfterTimeout value.
 */
object Terminate extends Action {

  val id = TerminateObjectId

  def serialize = new AdpTerminate()

  override def ref: AdpRef[AdpTerminate] = AdpRef(serialize)

  def objects: Iterable[PipelineObject] = None

}
