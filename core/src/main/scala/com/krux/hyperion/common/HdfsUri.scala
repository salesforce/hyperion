/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.common

import scala.language.implicitConversions


case class HdfsUri(ref: String) {

  require(ref.startsWith("hdfs://"), "HdfsUri must start with hdfs protocol.")

  def /(next: String): HdfsUri = HdfsUri(s"$ref/$next")

  def / : HdfsUri = /("")

  override def toString = ref
}

trait HdfsUriHelper {

  class HdfsUriBuilder {
    def /(next: String) = hdfs(next)
  }

  def hdfs: HdfsUriBuilder = new HdfsUriBuilder

  def hdfs(uri: String*): HdfsUri = HdfsUri(s"hdfs://${uri.mkString("/")}")

}

object HdfsUri extends HdfsUriHelper {

  implicit class HdfsStringContext(val sc: StringContext) extends AnyVal {
    def hdfs(args: Any*): HdfsUri = HdfsUri.hdfs(sc.s(args: _*))
  }

  implicit def string2HdfsUri(hdfsPath: String): HdfsUri = HdfsUri(hdfsPath)
}

