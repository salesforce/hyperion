/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.resource

import com.krux.hyperion.adt.{ HInt, HDouble, HString, HBoolean }

case class EmrClusterFields(
  amiVersion: Option[HString],
  standardBootstrapAction: Seq[HString],
  masterInstanceType: Option[HString],
  coreInstanceCount: HInt,
  coreInstanceType: Option[HString],
  taskInstanceCount: HInt,
  taskInstanceType: Option[HString],
  releaseLabel: Option[HString],  // do not use ami version with release label
  supportedProducts: Option[HString] = None,
  bootstrapAction: Seq[HString] = Seq.empty,
  enableDebugging: Option[HBoolean] = None,
  hadoopSchedulerType: Option[SchedulerType] = None,
  masterEbsConfiguration: Option[EmrEbsConfiguration] = None,
  coreEbsConfiguration: Option[EmrEbsConfiguration] = None,
  taskEbsConfiguration: Option[EmrEbsConfiguration] = None,
  masterInstanceBidPrice: Option[HDouble] = None,
  coreInstanceBidPrice: Option[HDouble] = None,
  taskInstanceBidPrice: Option[HDouble] = None,
  masterSecurityGroupId: Option[HString] = None,
  additionalMasterSecurityGroupIds: Seq[HString] = Seq.empty,
  slaveSecurityGroupId: Option[HString] = None,
  additionalSlaveSecurityGroupIds: Seq[HString] = Seq.empty,
  visibleToAllUsers: Option[HBoolean] = None,
  applications: Seq[EmrApplication] = Seq.empty,  // use with release label
  configuration: Seq[EmrConfiguration] = Seq.empty
)
