/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.adt.HBoolean
import com.krux.hyperion.datanode.S3DataNode

trait WithS3Output {

  type Self <: WithS3Output

  def shellCommandActivityFields: ShellCommandActivityFields
  def updateShellCommandActivityFields(fields: ShellCommandActivityFields): Self

  def withOutput(outputs: S3DataNode*): Self = updateShellCommandActivityFields(
    shellCommandActivityFields.copy(
      output = shellCommandActivityFields.output ++ outputs,
      stage = Option(HBoolean.True)
    )
  )

}
