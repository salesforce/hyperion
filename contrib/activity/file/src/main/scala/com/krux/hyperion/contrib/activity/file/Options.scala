/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.contrib.activity.file

import java.io.File

import com.krux.hyperion.contrib.activity.file.CompressionFormat.CompressionFormat

case class Options(
  header: Option[String] = None,
  pattern: Option[String] = None,
  output: String = "",
  numberOfFiles: Option[Int] = None,
  numberOfLinesPerFile: Option[Long] = None,
  numberOfBytesPerFile: Option[Long] = None,
  suffixLength: Int = 5,
  bufferSize: Long = 8192,
  compressed: Boolean = false,
  link: Boolean = false,
  skipFirstLine: Boolean = false,
  markSuccessfulJobs: Boolean = false,
  ignoreEmptyInput: Boolean = false,
  inputs: Seq[File] = Seq.empty,
  outputDirectory: Seq[File] = Seq.empty,
  temporaryDirectory: Option[File] = None,
  compressionFormat: CompressionFormat = CompressionFormat.GZ)
