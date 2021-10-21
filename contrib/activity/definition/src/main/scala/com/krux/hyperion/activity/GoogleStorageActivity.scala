/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.HyperionContext
import com.krux.hyperion.adt.{ HS3Uri, HString }
import com.krux.hyperion.common.S3Uri

trait GoogleStorageActivity extends BaseShellCommandActivity {

  type Self <: GoogleStorageActivity

  def botoConfigUrl: HS3Uri
  def googleStorageUri: HString

  override def scriptArguments = Seq(botoConfigUrl.serialize: HString, googleStorageUri)

}

object GoogleStorageActivity {

  def downloadScript(implicit hc: HyperionContext) = S3Uri(s"${hc.scriptUri}activities/gsutil-download.sh")

  def uploadScript(implicit hc: HyperionContext) = S3Uri(s"${hc.scriptUri}activities/gsutil-upload.sh")

}
