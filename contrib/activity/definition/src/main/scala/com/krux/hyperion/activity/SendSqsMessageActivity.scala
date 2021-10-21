/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.adt.{HInt, HString, HType}
import com.krux.hyperion.common.{BaseFields, PipelineObjectId, S3Uri}
import com.krux.hyperion.expression.RunnableObject
import com.krux.hyperion.HyperionContext
import com.krux.hyperion.resource.{Ec2Resource, Resource}

case class SendSqsMessageActivity private (
  baseFields: BaseFields,
  activityFields: ActivityFields[Ec2Resource],
  shellCommandActivityFields: ShellCommandActivityFields,
  jarUri: HString,
  mainClass: HString,
  queue: HString,
  message: HString,
  region: Option[HString],
  delay: Option[HInt],
  attributes: Map[HString, (HString, HString)]
) extends BaseShellCommandActivity {

  type Self = SendSqsMessageActivity

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateActivityFields(fields: ActivityFields[Ec2Resource]) = copy(activityFields = fields)
  def updateShellCommandActivityFields(fields: ShellCommandActivityFields) = copy(shellCommandActivityFields = fields)

  def withRegion(region: HString) = copy(region = Option(region))
  def withDelaySeconds(delay: HInt) = copy(delay = Option(delay))
  def withAttribute(key: HString, value: HString, dataType: HString = "String") = {
    val attribute = (key, (dataType, value))
    copy(attributes = attributes + attribute)
  }

  private def arguments: Seq[HType] = Seq(
    Option(Seq[HString]("--queue", queue)),
    region.map(Seq[HString]("--region", _)),
    if (attributes.nonEmpty) Option(Seq[HString]("--attributes", attributes.toSeq.map { case (k, (t, v)) => s"$k:$t=$v"}.mkString(","))) else None,
    delay.map(d => Seq[HType]("--delay", d)),
    Option(Seq[HString]("--message", message))
  ).flatten.flatten

  override def scriptArguments = (jarUri.serialize: HString) +: mainClass +: arguments

}

object SendSqsMessageActivity extends RunnableObject {

  def apply(queue: String,
    message: String)(runsOn: Resource[Ec2Resource])(implicit hc: HyperionContext): SendSqsMessageActivity =
    new SendSqsMessageActivity(
      baseFields = BaseFields(PipelineObjectId(SendSqsMessageActivity.getClass)),
      activityFields = ActivityFields(runsOn),
      shellCommandActivityFields = ShellCommandActivityFields(S3Uri(s"${hc.scriptUri}activities/run-jar.sh")),
      jarUri = s"${hc.scriptUri}activities/hyperion-notification-activity-current-assembly.jar",
      mainClass = "com.krux.hyperion.contrib.activity.notification.SendSqsMessage",
      queue = queue,
      message = message,
      region = None,
      delay = None,
      attributes = Map.empty
    )

}
