/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.HyperionContext
import com.krux.hyperion.adt.{HBoolean, HInt, HLong, HString, HType}
import com.krux.hyperion.common.{BaseFields, PipelineObjectId, S3Uri}
import com.krux.hyperion.activity.CompressionFormat.CompressionFormat
import com.krux.hyperion.expression.RunnableObject
import com.krux.hyperion.resource.{Ec2Resource, Resource}


case class SplitMergeFilesActivity private (
  baseFields: BaseFields,
  activityFields: ActivityFields[Ec2Resource],
  shellCommandActivityFields: ShellCommandActivityFields,
  jarUri: HString,
  mainClass: HString,
  filename: HString,
  header: Option[HString],
  compressedOutput: HBoolean,
  skipFirstInputLine: HBoolean,
  ignoreEmptyInput: HBoolean,
  linkOutputs: HBoolean,
  suffixLength: Option[HInt],
  numberOfFiles: Option[HInt],
  linesPerFile: Option[HLong],
  bytesPerFile: Option[HString],
  bufferSize: Option[HString],
  pattern: Option[HString],
  markSuccessfulJobs: HBoolean,
  temporaryDirectory: Option[HString],
  compressionFormat: CompressionFormat
) extends BaseShellCommandActivity with WithS3Input with WithS3Output {

  type Self = SplitMergeFilesActivity

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateActivityFields(fields: ActivityFields[Ec2Resource]) = copy(activityFields = fields)
  def updateShellCommandActivityFields(fields: ShellCommandActivityFields) = copy(shellCommandActivityFields = fields)

  def withCompressedOutput() = copy(compressedOutput = HBoolean.True)
  def withSkipFirstInputLine() = copy(skipFirstInputLine = HBoolean.True)
  def withLinkOutputs() = copy(linkOutputs = HBoolean.True)
  def withHeader(header: HString*) = copy(header = Option(header.mkString(","): HString))
  def withSuffixLength(suffixLength: HInt) = copy(suffixLength = Option(suffixLength))
  def withNumberOfFiles(numberOfFiles: HInt) = copy(numberOfFiles = Option(numberOfFiles))
  def withNumberOfLinesPerFile(linesPerFile: HLong) = copy(linesPerFile = Option(linesPerFile))
  def withNumberOfBytesPerFile(bytesPerFile: HString) = copy(bytesPerFile = Option(bytesPerFile))
  def withBufferSize(bufferSize: HString) = copy(bufferSize = Option(bufferSize))
  def withInputPattern(pattern: HString) = copy(pattern = Option(pattern))
  def markingSuccessfulJobs() = copy(markSuccessfulJobs = HBoolean.True)
  def ignoringEmptyInput() = copy(ignoreEmptyInput = HBoolean.True)
  def withTemporaryDirectory(temporaryDirectory: HString) = copy(temporaryDirectory = Option(temporaryDirectory))
  def withCompressionFormat(compressionFormat: CompressionFormat) = copy(compressionFormat = compressionFormat)

  private def arguments: Seq[HType] = Seq(
    compressedOutput.exists(Seq[HString]("-z")),
    skipFirstInputLine.exists(Seq[HString]("--skip-first-line")),
    linkOutputs.exists(Seq[HString]("--link")),
    markSuccessfulJobs.exists(Seq[HString]("--mark-successful-jobs")),
    ignoreEmptyInput.exists(Seq[HString]("--ignore-empty-input")),
    temporaryDirectory.map(dir => Seq[HString]("-T", dir)),
    header.map(h => Seq[HString]("--header", h)),
    suffixLength.map(s => Seq[HType]("--suffix-length", s)),
    numberOfFiles.map(n => Seq[HType]("-n", n)),
    linesPerFile.map(n => Seq[HType]("-l", n)),
    bytesPerFile.map(n => Seq[HString]("-C", n)),
    bufferSize.map(n => Seq[HString]("-S", n)),
    pattern.map(p => Seq[HString]("--name", p)),
    Option(Seq[HString](filename)),
    Option(Seq[HString]("-k", compressionFormat.toString))
  ).flatten.flatten

  override def scriptArguments = (jarUri.serialize: HString) +: mainClass +: arguments
}

object SplitMergeFilesActivity extends RunnableObject {

  def apply(filename: String)(runsOn: Resource[Ec2Resource])(implicit hc: HyperionContext): SplitMergeFilesActivity =
    new SplitMergeFilesActivity(
      baseFields = BaseFields(PipelineObjectId(SplitMergeFilesActivity.getClass)),
      activityFields = ActivityFields(runsOn),
      shellCommandActivityFields = ShellCommandActivityFields(S3Uri(s"${hc.scriptUri}activities/run-jar.sh")),
      jarUri = s"${hc.scriptUri}activities/hyperion-file-activity-current-assembly.jar",
      mainClass = "com.krux.hyperion.contrib.activity.file.RepartitionFile",
      filename = filename,
      header = None,
      compressedOutput = HBoolean.False,
      skipFirstInputLine = HBoolean.False,
      ignoreEmptyInput = HBoolean.False,
      linkOutputs = HBoolean.False,
      suffixLength = None,
      numberOfFiles = None,
      linesPerFile = None,
      bytesPerFile = None,
      bufferSize = None,
      pattern = None,
      markSuccessfulJobs = HBoolean.False,
      temporaryDirectory = None,
      compressionFormat = CompressionFormat.GZ
    )

}
