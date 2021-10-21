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

case class SendSlackMessageActivity private (
  baseFields: BaseFields,
  activityFields: ActivityFields[Ec2Resource],
  shellCommandActivityFields: ShellCommandActivityFields,
  jarUri: HString,
  mainClass: HString,
  continueOnError: HBoolean,
  webhookUrl: HString,
  message: Seq[HString],
  user: Option[HString],
  emoji: Option[HString],
  to: Option[HString]
) extends BaseShellCommandActivity {

  type Self = SendSlackMessageActivity

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateActivityFields(fields: ActivityFields[Ec2Resource]) = copy(activityFields = fields)
  def updateShellCommandActivityFields(fields: ShellCommandActivityFields) = copy(shellCommandActivityFields = fields)

  def continuingOnError = copy(continueOnError = HBoolean.True)
  def withUser(user: HString) = copy(user = Option(user))
  def withEmoji(emoji: HString) = copy(emoji = Option(emoji))
  def toUser(user: HString) = copy(to = Option(s"@$user": HString))
  def toChannel(channel: HString) = copy(to = Option(s"#$channel": HString))

  private def arguments: Seq[HString] = Seq(
    continueOnError.exists(Seq[HString]("--fail-on-error")),
    Option(Seq[HString]("--webhook-url", webhookUrl)),
    user.map(user => Seq[HString]("--user", user)),
    emoji.map(emoji => Seq[HString]("--emoji", emoji)),
    to.map(to => Seq[HString]("--to", to))
  ).flatten.flatten ++ message

  override def scriptArguments = (jarUri.serialize: HString) +: mainClass +: arguments

}

object SendSlackMessageActivity extends RunnableObject {

  def apply(webhookUrl: HString, message: HString*)(runsOn: Resource[Ec2Resource])(implicit hc: HyperionContext): SendSlackMessageActivity =
    new SendSlackMessageActivity(
      baseFields = BaseFields(PipelineObjectId(SendSlackMessageActivity.getClass)),
      activityFields = ActivityFields(runsOn),
      shellCommandActivityFields = ShellCommandActivityFields(S3Uri(s"${hc.scriptUri}activities/run-jar.sh")),
      jarUri = s"${hc.scriptUri}activities/hyperion-notification-activity-current-assembly.jar",
      mainClass = "com.krux.hyperion.contrib.activity.notification.SendSlackMessage",
      continueOnError = HBoolean.False,
      webhookUrl = webhookUrl,
      message = message,
      user = None,
      emoji = None,
      to = None
    )

}
