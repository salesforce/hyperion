/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.common

import scala.language.implicitConversions

import com.krux.hyperion.datanode.S3DataNode

/**
 * The S3Uri provides a typesafe way of representing S3 URI's.
 *
 * There are a variety of ways to create S3Uri's.  You don't have to create an instance
 * directly.  You can create using string interpolation such as s3"hyperion-bucket/some-path"
 * or using a builder such as s3 / "hyperion-bucket" / "some-path".
 */
case class S3Uri(ref: String) {
  require(ref.startsWith("s3://"), "S3Uri must start with s3 protocol.")

  def /(next: String): S3Uri = S3Uri(s"$ref/$next")

  def / : S3Uri = /("")

  override def toString = ref
}

trait S3UriHelper {

  class S3UriBuilder {
    def /(next: String) = s3(next)
  }

  def s3: S3UriBuilder = new S3UriBuilder

  def s3(uri: String*): S3Uri = S3Uri(s"s3://${uri.mkString("/")}")

}

object S3Uri extends S3UriHelper {

  implicit class S3StringContext(val sc: StringContext) extends AnyVal {
    def s3(args: Any*): S3Uri = S3Uri.s3(sc.s(args: _*))
  }

  implicit def string2S3Uri(s3path: String): S3Uri = S3Uri(s3path)

  implicit def s3Uri2S3DataNode(s3path: S3Uri): S3DataNode = S3DataNode(s3path)

}
