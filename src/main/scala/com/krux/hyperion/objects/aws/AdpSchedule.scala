package com.krux.hyperion.objects.aws

import com.github.nscala_time.time.Imports.DateTime


/**
 * Defines the timing of a scheduled event, such as when an activity runs.
 *
 * ref: http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-object-schedule.html
 */
trait AdpSchedule extends AdpDataPipelineObject {

  /**
   * How often the pipeline should run. The format is "N [minutes|hours|days|weeks|months]",
   * where N is a number followed by one of the time specifiers. For example, "15 minutes", runs
   * the pipeline every 15 minutes.
   *
   * The minimum period is 15 minutes and the maximum period is 3 years.
   */
  def period: String

  /**
   * The number of times to execute the pipeline after it's activated. You can't use occurrences
   * with endDateTime.
   */
  def occurrences: Option[String]

  val `type`: String = "Schedule"

}


/**
 * @param startAt The date and time at which to start the scheduled pipeline runs. Valid value is
 * FIRST_ACTIVATION_DATE_TIME. FIRST_ACTIVATION_DATE_TIME is assumed to be the current date and
 * time.
 */
case class AdpStartAtSchedule (
  id: String,
  name: Option[String],
  period: String,
  startAt: String,
  occurrences: Option[String]
) extends AdpSchedule


/**
 * @param startDateTime The date and time to start the scheduled runs. You must use either
 * startDateTime or startAt but not both.
 */
case class AdpStartDateTimeSchedule (
  id: String,
  name: Option[String],
  period: String,
  startDateTime: DateTime,
  occurrences: Option[String]
) extends AdpSchedule
