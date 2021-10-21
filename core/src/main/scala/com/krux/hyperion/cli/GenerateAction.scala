/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.cli

import java.io.PrintStream

import org.json4s.jackson.JsonMethods._

import com.krux.hyperion.DataPipelineDefGroup


private[hyperion] case object GenerateAction extends Action {

  def apply(options: Options, defGroup: DataPipelineDefGroup): Boolean = {

    defGroup.ungroup().foreach { case (key, pipelineDef) =>
      val outputStream = options.output
        .map(o => new PrintStream(o + key.map(pipelineDef.nameKeySeparator + _).getOrElse("")))
        .getOrElse(System.out)
      outputStream.println(pretty(render(pipelineDef.toJson)))
    }

    true
  }

}
