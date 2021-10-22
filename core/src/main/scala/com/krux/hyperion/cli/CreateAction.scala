/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.cli

import com.krux.hyperion.client.AwsClientForDef

private[hyperion] case object CreateAction extends AwsAction {

  def apply(options: Options, client: AwsClientForDef): Boolean = {
    if (options.activate)
      client.createPipelines(options.force, options.checkExistence).flatMap(_.activatePipelines())
    else
      client.createPipelines(options.force, options.checkExistence)
  }.isDefined

}
