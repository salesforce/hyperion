/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.aws

trait AdpAction extends AdpDataPipelineObject

/**
 * An action to trigger the cancellation of a pending or unfinished activity, resource,
 * or data node. AWS Data Pipeline attempts to put the activity, resource, or data node
 * into the CANCELLED state if it does not finish by the lateAfterTimeout value.
 */
class AdpTerminate extends AdpAction {
  val id = "TerminateAction"
  val name = Option("TerminateTasks")
  val `type` = "Terminate"
}

/**
 * Sends an Amazon SNS notification message when an activity fails or finishes successfully.
 *
 * @param message The body text of the Amazon SNS notification. String  Yes
 * @param role  The IAM role to use to create the Amazon SNS alarm. String  Yes
 * @param subject The subject line of the Amazon SNS notification message.  String  Yes
 * @param topicArn  The destination Amazon SNS topic ARN for the message. String  Yes
 */
case class AdpSnsAlarm (
  id: String,
  name: Option[String],
  subject: String,
  message: String,
  topicArn: String,
  role: String
) extends AdpAction {

  val `type` = "SnsAlarm"

}
