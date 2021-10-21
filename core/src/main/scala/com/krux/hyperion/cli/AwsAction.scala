/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.cli

import com.krux.hyperion.client.{AwsClientForDef, AwsClient}
import com.krux.hyperion.DataPipelineDefGroup

private[hyperion] trait AwsAction extends Action {

  def apply(options: Options, client: AwsClientForDef): Boolean

  def apply(options: Options, pipelineDef: DataPipelineDefGroup): Boolean =
    apply(options, AwsClient(pipelineDef, options.region, options.roleArn))

}
