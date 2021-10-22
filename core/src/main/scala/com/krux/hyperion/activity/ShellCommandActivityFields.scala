/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.adt.{ HString, HBoolean }
import com.krux.hyperion.datanode.S3DataNode

case class ShellCommandActivityFields(
  script: Script,
  scriptArguments: Seq[HString] = Seq.empty,
  stdout: Option[HString] = None,
  stderr: Option[HString] = None,
  stage: Option[HBoolean] = None,
  input: Seq[S3DataNode] = Seq.empty,
  output: Seq[S3DataNode] = Seq.empty
)
