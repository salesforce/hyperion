/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import scala.language.implicitConversions

import com.krux.hyperion.adt.{ HString, HS3Uri }
import com.krux.hyperion.common.S3Uri

sealed trait Script {
  def uri: Option[HS3Uri]
  def content: Option[HString]
}

sealed case class ScriptUri(uri: Option[HS3Uri]) extends Script {
  def content: Option[HString] = None
}

sealed case class ScriptContent(content: Option[HString]) extends Script {
  def uri: Option[HS3Uri] = None
}

object Script {
  def apply(uri: HS3Uri): Script = ScriptUri(Option(uri))
  def apply(content: HString): Script = ScriptContent(Option(content))

  implicit def s3Uri2Script(uri: S3Uri): Script = Script(uri)
  implicit def string2Script(content: String): Script = Script(content)
}
