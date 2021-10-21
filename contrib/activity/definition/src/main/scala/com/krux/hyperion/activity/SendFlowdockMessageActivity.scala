/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.HyperionContext
import com.krux.hyperion.adt.{ HBoolean, HString }
import com.krux.hyperion.common.{ BaseFields, PipelineObjectId, S3Uri }
import com.krux.hyperion.expression.RunnableObject
import com.krux.hyperion.resource.{ Ec2Resource, Resource }

case class SendFlowdockMessageActivity private (
  baseFields: BaseFields,
  activityFields: ActivityFields[Ec2Resource],
  shellCommandActivityFields: ShellCommandActivityFields,
  jarUri: HString,
  mainClass: HString,
  flowApiToken: HString,
  message: HString,
  user: HString,
  continueOnError: HBoolean,
  tags: Seq[HString]
) extends BaseShellCommandActivity {

  type Self = SendFlowdockMessageActivity

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateActivityFields(fields: ActivityFields[Ec2Resource]) = copy(activityFields = fields)
  def updateShellCommandActivityFields(fields: ShellCommandActivityFields) = copy(shellCommandActivityFields = fields)

  def continuingOnError = copy(continueOnError = HBoolean.True)
  def withUser(user: HString) = copy(user = user)
  def withTags(tag: HString*) = copy(tags = this.tags ++ tag)

  private def arguments: Seq[HString] = Seq(
    continueOnError.exists("--fail-on-error": HString).toSeq,
    Seq[HString]("--api-key", flowApiToken),
    Seq[HString]("--user", user),
    if (tags.isEmpty) Seq.empty else Seq[HString]("--tags", tags.mkString(",")),
    Seq[HString](message)
  ).flatten

  override def scriptArguments = (jarUri.serialize: HString) +: mainClass +: arguments

}

object SendFlowdockMessageActivity extends RunnableObject {

  def apply(flowApiToken: HString, message: HString)(runsOn: Resource[Ec2Resource])(implicit hc: HyperionContext): SendFlowdockMessageActivity =
    new SendFlowdockMessageActivity(
      baseFields = BaseFields(PipelineObjectId(SendFlowdockMessageActivity.getClass)),
      activityFields = ActivityFields(runsOn),
      shellCommandActivityFields = ShellCommandActivityFields(S3Uri(s"${hc.scriptUri}activities/run-jar.sh")),
      jarUri = s"${hc.scriptUri}activities/hyperion-notification-activity-current-assembly.jar",
      mainClass = "com.krux.hyperion.contrib.activity.notification.SendFlowdockMessage",
      flowApiToken = flowApiToken,
      message = message,
      user = "hyperion",
      continueOnError = HBoolean.False,
      tags = Seq.empty
    )

}
