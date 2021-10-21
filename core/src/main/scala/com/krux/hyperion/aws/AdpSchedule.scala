/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.aws

/**
 * Defines the timing of a scheduled event, such as when an activity runs.
 *
 * ref: http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-object-schedule.html
 *
 */
case class AdpOnDemandSchedule(
  id: String,
  name: Option[String]
) extends AdpDataPipelineObject {

  val `type`: String = "Schedule"

}

/**
  * Defines the timing of a scheduled event, such as when an activity runs.
  *
  * ref: http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-object-schedule.html
  *
  * @param period How often the pipeline should run. The format is "N [minutes|hours|days|weeks|months]",
  *               where N is a number followed by one of the time specifiers. For example,
  *               "15 minutes", runs the pipeline every 15 minutes.  The minimum period is 15 minutes
  *               and the maximum period is 3 years.
  * @param startAt The date and time at which to start the scheduled pipeline runs. Valid value is
  *                FIRST_ACTIVATION_DATE_TIME. FIRST_ACTIVATION_DATE_TIME is assumed to be the
  *                current date and time.
  * @param startDateTime The date and time to start the scheduled runs. You must use either
  *                      startDateTime or startAt but not both.
  * @param occurrences The number of times to execute the pipeline after it's activated. You can't use
  *                    occurrences with endDateTime.
  * @param endDateTime The date and time to end the scheduled runs. Must be a date and time later than
  *                    the value of startDateTime or startAt. The default behavior is to schedule runs
  *                    until the pipeline is shut down.
  */

case class AdpRecurringSchedule(
  id: String,
  name: Option[String],
  period: String,
  startAt: Option[String],
  startDateTime: Option[String],
  endDateTime: Option[String],
  occurrences: Option[String]
) extends AdpDataPipelineObject {

  val `type`: String = "Schedule"

}
