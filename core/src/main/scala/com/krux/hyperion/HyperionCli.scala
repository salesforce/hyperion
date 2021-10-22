/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion

import com.krux.hyperion.cli.EntryPoint

/**
  * HyperionCli is a base trait that brings in CLI functionality to
  * DataPipelineDef's.
  */
trait HyperionCli { this: DataPipelineDefGroup =>

  def main(args: Array[String]): Unit = System.exit(EntryPoint(this).run(args))

}

