/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.resource

import com.krux.hyperion.adt.{HString, HBoolean, HDuration, HInt}
import com.krux.hyperion.common.HttpProxy

case class ResourceFields(
  role: Option[HString],
  resourceRole: Option[HString],
  keyPair: Option[HString],
  region: Option[HString],
  availabilityZone: Option[HString],
  subnetId: Option[HString],
  useOnDemandOnLastAttempt: Option[HBoolean] = None,
  initTimeout: Option[HDuration] = None,
  terminateAfter: Option[HDuration] = None,
  actionOnResourceFailure: Option[ActionOnResourceFailure] = None,
  actionOnTaskFailure: Option[ActionOnTaskFailure] = None,
  httpProxy: Option[HttpProxy] = None,
  maximumRetries: Option[HInt] = None
)
