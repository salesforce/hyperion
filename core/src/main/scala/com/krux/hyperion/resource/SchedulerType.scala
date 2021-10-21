/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.resource

trait SchedulerType {
  def serialize: String
  override def toString = serialize
}

case object ParallelFairScheduler extends SchedulerType {
  val serialize = "PARALLEL_FAIR_SCHEDULING"
}

case object ParallelCapacityScheduler extends SchedulerType {
  val serialize = "PARALLEL_CAPACITY_SCHEDULING"
}

case object DefaultScheduler extends SchedulerType {
  val serialize = "DEFAULT_SCHEDULER"
}
