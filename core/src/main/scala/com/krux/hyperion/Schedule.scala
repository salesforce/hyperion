/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion

import java.time.{DayOfWeek, LocalTime, ZoneOffset, ZonedDateTime}

import com.krux.hyperion.Implicits._
import com.krux.hyperion.adt.{HDateTime, HDuration, HInt}
import com.krux.hyperion.aws.{AdpOnDemandSchedule, AdpRecurringSchedule, AdpRef}
import com.krux.hyperion.common.{PipelineObject, PipelineObjectId, ScheduleObjectId}
import com.krux.hyperion.expression.Duration

/**
 * Schedule defines how a pipeline is run.
 */
sealed trait Schedule extends PipelineObject {
  def scheduleType: ScheduleType
}

/**
 * Schedule that runs on-demand.
 */
case object OnDemandSchedule extends Schedule {

  val id: PipelineObjectId = ScheduleObjectId
  val scheduleType: ScheduleType = OnDemand

  def objects: Iterable[PipelineObject] = None

  lazy val serialize: AdpOnDemandSchedule = new AdpOnDemandSchedule(
    id = id,
    name = id.toOption
  )

  def ref: AdpRef[AdpOnDemandSchedule] = AdpRef(serialize)

}

/**
 * Schedule that runs at defined period.
 *
 * @note If start time given is a past time, data pipeline will perform back fill from the start.
 */
final case class RecurringSchedule private[hyperion] (
  id: PipelineObjectId = ScheduleObjectId,
  // if None, will use first activation datetime
  start: Option[HDateTime] = None,
  period: HDuration = 1.day,
  end: Option[Either[HInt, HDateTime]] = None,
  scheduleType: ScheduleType = Cron
) extends Schedule {

  def startAtActivation = copy(start = None)

  def startDateTime(dt: HDateTime) = copy(start = Option(dt))

  def startThisHourAt(minuteOfHour: Int, secondOfMinute: Int) = {
    val currentHour = ZonedDateTime.now.withZoneSameInstant(ZoneOffset.UTC).getHour
    startTodayAt(currentHour, minuteOfHour, secondOfMinute)
  }

  def startTodayAt(hourOfDay: Int, minuteOfHour: Int, secondOfMinute: Int) =
    startThisDayOfXAt(0, hourOfDay, minuteOfHour, secondOfMinute)((dt, _) => dt)

  def startThisWeekAt(dayOfWeek: Int, hourOfDay: Int, minuteOfHour: Int, secondOfMinute: Int) =
    startThisDayOfXAt(dayOfWeek, hourOfDay, minuteOfHour, secondOfMinute)((dt, int) => dt.`with`(DayOfWeek.of(int)))

  def startThisMonthAt(dayOfMonth: Int, hourOfDay: Int, minuteOfHour: Int, secondOfMinute: Int) =
    startThisDayOfXAt(dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute)(_.withDayOfMonth(_))

  private def startThisDayOfXAt(dayOfX: Int, hourOfDay: Int, minuteOfHour: Int,
      secondOfMinute: Int)(dayOfFunc: (ZonedDateTime, Int) => ZonedDateTime) = {
    val startDt = dayOfFunc(ZonedDateTime.now.withZoneSameInstant(ZoneOffset.UTC), dayOfX)
      .`with`(LocalTime.of(hourOfDay, minuteOfHour, secondOfMinute))
    copy(start = Option(startDt: HDateTime))
  }

  def every(p: HDuration) = this.copy(period = p)
  def until(dt: HDateTime) = this.copy(end = Option(Right(dt)))
  def stopAfter(occurrences: HInt) = this.copy(end = if(occurrences.value.left.forall(_ > 0)) Option(Left(occurrences)) else None)

  def objects: Iterable[PipelineObject] = None

  lazy val serialize: AdpRecurringSchedule = start match {
    case Some(dt) =>
      AdpRecurringSchedule(
        id = id,
        name = id.toOption,
        period = period.serialize,
        startAt = None,
        startDateTime = Option(dt.serialize),
        endDateTime = end.flatMap {
          case Right(d) => Option(d.serialize)
          case _ => None
        },
        occurrences = end.flatMap {
          case Left(occurrences) => Option(occurrences.serialize)
          case _ => None
        }
      )

    case None =>
      AdpRecurringSchedule(
        id = id,
        name = id.toOption,
        period = period.serialize,
        startAt = Option("FIRST_ACTIVATION_DATE_TIME"),
        startDateTime = None,
        endDateTime = end.flatMap {
          case Right(dt) => Option(dt.serialize)
          case _ => None
        },
        occurrences = end.flatMap {
          case Left(occurrences) => Option(occurrences.serialize)
          case _ => None
        }
      )
  }

  def ref: AdpRef[AdpRecurringSchedule] = AdpRef(serialize)

}

object Schedule {

  def ondemand: OnDemandSchedule.type = OnDemandSchedule

  def cron: RecurringSchedule = RecurringSchedule(scheduleType = Cron)

  def timeSeries: RecurringSchedule = RecurringSchedule(scheduleType = TimeSeries)

  def onceAtActivation: RecurringSchedule = RecurringSchedule(end = Option(Left(1)), scheduleType = Cron)

  def delay(schedule: Schedule, by: Duration, multiplier: Int): Schedule = {
    import com.krux.hyperion.common.DurationConverters._
    delay(schedule, by.asDurationMultiplied(multiplier))
  }

  def delay(schedule: Schedule, by: java.time.Duration): Schedule = schedule match {
    case s: RecurringSchedule =>
      s.start match {
        case None =>
          s
        case Some(dt) =>
          s.copy(start = Option(dt.value.fold[HDateTime](_ plus by, _ + by)))
      }
    case OnDemandSchedule =>
      OnDemandSchedule
  }

}
