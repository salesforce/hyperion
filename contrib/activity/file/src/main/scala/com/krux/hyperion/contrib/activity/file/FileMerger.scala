/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.contrib.activity.file

import java.io._
import java.util.zip.{ GZIPInputStream, GZIPOutputStream }
import org.apache.commons.compress.compressors.bzip2.{ BZip2CompressorInputStream, BZip2CompressorOutputStream }

import org.apache.commons.io.IOUtils


case class FileMerger(destination: File, skipFirstLine: Boolean = false, headers: Option[String] = None) {
  def merge(sources: File*): File = {
    val output: OutputStream = new BufferedOutputStream({
      val s = new FileOutputStream(destination, true)
      if (destination.getName.endsWith(".gz")) new GZIPOutputStream(s)
      else if(destination.getName.endsWith(".bz2")) new BZip2CompressorOutputStream(s)
      else s
    })

    try {
      sources.foldLeft(headers -> output)(appendFile)
      destination
    } finally {
      try {
        output.close()
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }

  private def doSkipFirstLine(input: InputStream): InputStream = {
    while (skipFirstLine && (input.read() match {
      case -1 | '\n' => false
      case _ => true
    })) {}

    input
  }

  private def appendFile(state: (Option[String], OutputStream), source: File): (Option[String], OutputStream) = {
    val (headers, output) = state

    if (source.getName == "-") {
      print("Merging stdin...")
      headers.map(_.getBytes).foreach(output.write)
      IOUtils.copy(doSkipFirstLine(System.in), output)
      println("done")

      None -> output
    } else if (source.length() > 0) {
      print(s"Merging ${source.getAbsolutePath}...")

      val input = new BufferedInputStream({
        val s = new FileInputStream(source)
        if (source.getName.endsWith(".gz")) new GZIPInputStream(s)
        else if(source.getName.endsWith(".bz2")) new BZip2CompressorInputStream(s)
        else s
      })

      try {
        input.mark(2)

        if (input.read() != -1) {
          input.reset()

          headers.map(_.getBytes).foreach(output.write)

          IOUtils.copy(doSkipFirstLine(input), output)
        }
      } finally {
        try {
          input.close()
        } catch {
          case e: Exception => e.printStackTrace()
        } finally {
          println("done")
        }
      }

      None -> output
    } else {
      headers -> output
    }
  }
}
