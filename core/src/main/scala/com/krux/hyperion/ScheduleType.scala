/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion

sealed trait ScheduleType {
  def serialize: String
  override def toString = serialize
}

case object Cron extends ScheduleType {
  val serialize: String = "cron"
}

case object TimeSeries extends ScheduleType {
  val serialize: String = "timeseries"
}

case object OnDemand extends ScheduleType {
  val serialize: String = "ondemand"
}
