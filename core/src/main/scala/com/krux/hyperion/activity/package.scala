/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion

import java.net.URI

import com.krux.hyperion.common.S3Uri
import com.krux.hyperion.common.S3Uri._


package object activity {

  final val EmrScriptRunner: S3Uri =
    s3 / "elasticmapreduce" / "libs" / "script-runner" / "script-runner.jar"

  final val EmrCommandRunner: String = "command-runner.jar"

  final val EmrHadoopJarsDir: URI = URI.create("/var/lib/aws/emr/step-runner/hadoop-jars/")
}
