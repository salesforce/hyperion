/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.datanode

import com.krux.hyperion.action.SnsAlarm
import com.krux.hyperion.precondition.Precondition

case class DataNodeFields (
  preconditions: Seq[Precondition] = Seq.empty,
  onFailAlarms: Seq[SnsAlarm] = Seq.empty,
  onSuccessAlarms: Seq[SnsAlarm] = Seq.empty
)
