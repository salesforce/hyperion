/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.aws

case class AdpShellScriptConfig(
  id: String,
  name: Option[String],
  scriptUri: String,
  scriptArgument: Option[Seq[String]]
) extends AdpDataPipelineObject {

  val `type` = "ShellScriptConfig"

}
