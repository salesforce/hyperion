/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.HyperionContext
import com.krux.hyperion.adt.{HBoolean, HS3Uri}
import com.krux.hyperion.common.S3Uri

/**
  * Base trait for server-side PGP encryption activities.
  */
trait PgpActivity extends BaseShellCommandActivity with WithS3Input with WithS3Output {
  type Self <: PgpActivity

  def shellCommandActivityFields: ShellCommandActivityFields
  def updateShellCommandActivityFields(fields: ShellCommandActivityFields): Self

  def key: HS3Uri

  def markSuccessfulJobs: HBoolean
  def markOnSuccess: Self
}

object PgpActivity {
  def decryptScript(implicit hc: HyperionContext) = S3Uri(s"${hc.scriptUri}activities/gpg-decrypt.sh")

  def encryptScript(implicit hc: HyperionContext) = S3Uri(s"${hc.scriptUri}activities/gpg-encrypt.sh")
}
