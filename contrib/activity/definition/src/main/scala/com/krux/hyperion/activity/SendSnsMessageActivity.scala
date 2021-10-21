/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.adt.{HBoolean, HString}
import com.krux.hyperion.common.{BaseFields, PipelineObjectId, S3Uri}
import com.krux.hyperion.expression.RunnableObject
import com.krux.hyperion.HyperionContext
import com.krux.hyperion.resource.{Ec2Resource, Resource}

case class SendSnsMessageActivity private (
  baseFields: BaseFields,
  activityFields: ActivityFields[Ec2Resource],
  shellCommandActivityFields: ShellCommandActivityFields,
  jarUri: HString,
  mainClass: HString,
  topicArn: HString,
  message: HString,
  subject: Option[HString],
  region: Option[HString],
  structuredMessage: HBoolean,
  attributes: Map[HString, (HString, HString)]
) extends BaseShellCommandActivity {

  type Self = SendSnsMessageActivity

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateActivityFields(fields: ActivityFields[Ec2Resource]) = copy(activityFields = fields)
  def updateShellCommandActivityFields(fields: ShellCommandActivityFields) = copy(shellCommandActivityFields = fields)

  def withSubject(subject: HString) = copy(subject = Option(subject))
  def withRegion(region: HString) = copy(region = Option(region))
  def withStructuredMessage = copy(structuredMessage = HBoolean.True)
  def withAttribute(key: HString, value: HString, dataType: HString = "String") = {
    val attribute = (key, (dataType, value))
    copy(attributes = attributes + attribute)
  }

  private def arguments: Seq[HString] = Seq(
    Option(Seq[HString]("--topic-arn", topicArn)),
    region.map(Seq[HString]("--region", _)),
    if (attributes.nonEmpty) Option(Seq[HString]("--attributes", attributes.toSeq.map { case (k, (t, v)) => s"$k:$t=$v"}.mkString(","))) else None,
    subject.map(Seq[HString]("--subject", _)),
    structuredMessage.exists(Seq[HString]("--json")),
    Option(Seq[HString]("--message", message))
  ).flatten.flatten

  override def scriptArguments = (jarUri.serialize: HString) +: mainClass +: arguments

}

object SendSnsMessageActivity extends RunnableObject {
  def apply(topicArn: String, message: String)(runsOn: Resource[Ec2Resource])(implicit hc: HyperionContext): SendSnsMessageActivity =
    new SendSnsMessageActivity(
      baseFields = BaseFields(PipelineObjectId(SendSnsMessageActivity.getClass)),
      activityFields = ActivityFields(runsOn),
      shellCommandActivityFields = ShellCommandActivityFields(S3Uri(s"${hc.scriptUri}activities/run-jar.sh")),
      jarUri = s"${hc.scriptUri}activities/hyperion-notification-activity-current-assembly.jar",
      mainClass = "com.krux.hyperion.contrib.activity.notification.SendSnsMessage",
      topicArn = topicArn,
      message = message,
      subject = None,
      region = None,
      structuredMessage = HBoolean.False,
      attributes = Map.empty
    )

}
