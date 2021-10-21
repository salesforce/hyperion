/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.expression

import java.time.ZonedDateTime

import scala.language.implicitConversions

/**
 * Expression. Expressions are delimited by: "#{" and "}" and the contents of the braces are
 * evaluated by AWS Data Pipeline.
 */
trait Expression {

  def content: String

  lazy val serialize: String = s"#{$content}"

  override def toString: String = serialize

}

object Expression {

  implicit def expression2String(exp: Expression): String = exp.toString

}

trait TypedExpression extends Expression

object TypedExpression {
  implicit def int2IntConstantExp(num: Int): IntExp = IntConstantExp(num)
  implicit def string2StringConstantExp(raw: String): StringExp = StringConstantExp(raw)
  implicit def double2DoubleConstantExp(num: Double): DoubleExp = DoubleConstantExp(num)
  implicit def dateTime2DateTimeConstantExp(dt: ZonedDateTime): DateTimeExp = DateTimeConstantExp(dt)
}

trait IntExp extends TypedExpression { self =>

  def + (e: IntExp) = new IntExp {
    def content = s"${self.content} + ${e.content}"
  }

  def + (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} + ${e.content}"
  }

  def - (e: IntExp) = new IntExp {
    def content = s"${self.content} - ${e.content}"
  }

  def - (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} - ${e.content}"
  }

  def * (e: IntExp) = new IntExp {
    def content = s"${self.content} * ${e.content}"
  }

  def * (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} * ${e.content}"
  }

  def / (e: IntExp) = new IntExp {
    def content = s"${self.content} / ${e.content}"
  }

  def / (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} / ${e.content}"
  }

  /**
   * @note for ^ case, it always returns DoubleExp regardless of the type of the paramenter
   */
  def ^ (e: IntExp) = new DoubleExp {
    def content = s"${self.content} ^ ${e.content}"
  }

  def ^ (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} ^ ${e.content}"
  }

}

trait LongExp extends TypedExpression

trait DoubleExp extends TypedExpression { self =>

  def + (e: IntExp) = new DoubleExp {
    def content = s"${self.content} + ${e.content}"
  }

  def + (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} + ${e.content}"
  }

  def - (e: IntExp) = new DoubleExp {
    def content = s"${self.content} - ${e.content}"
  }

  def - (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} - ${e.content}"
  }

  def * (e: IntExp) = new DoubleExp {
    def content = s"${self.content} * ${e.content}"
  }

  def * (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} * ${e.content}"
  }

  def / (e: IntExp) = new DoubleExp {
    def content = s"${self.content} / ${e.content}"
  }

  def / (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} / ${e.content}"
  }

  def ^ (e: IntExp) = new DoubleExp {
    def content = s"${self.content} ^ ${e.content}"
  }

  def ^ (e: DoubleExp) = new DoubleExp {
    def content = s"${self.content} ^ ${e.content}"
  }

}

trait StringExp extends TypedExpression { self =>

  def + (e: StringExp) = new StringExp {
    def content = s"${self.content} + ${e.content}"
  }

}

// Amazon Datapipeline does not have pipeline server side evaluated boolean expressions, so we
// could support evaluate in client side (mainly for parameters)
trait BooleanExp extends TypedExpression with Evaluatable[Boolean]

trait DateTimeExp extends TypedExpression {

  // java.time.Duration converted to minutes which is the lowest granularity
  // supported by Amazon Datapipeline expression
  def + (duration: java.time.Duration): DateTimeExp =
    this + Minute(duration.toMinutes.toInt)

  def + (period: Duration): DateTimeExp = period match {
    case Minute(n) => PlusMinutes(this, IntConstantExp(n))
    case Hour(n) => PlusHours(this, IntConstantExp(n))
    case Day(n) => PlusDays(this, IntConstantExp(n))
    case Week(n) => PlusWeeks(this, IntConstantExp(n))
    case Month(n) => PlusMonths(this, IntConstantExp(n))
    case Year(n) => PlusYears(this, IntConstantExp(n))
  }

  def - (period: Duration): DateTimeExp = period match {
    case Minute(n) => MinusMinutes(this, IntConstantExp(n))
    case Hour(n) => MinusHours(this, IntConstantExp(n))
    case Day(n) => MinusDays(this, IntConstantExp(n))
    case Week(n) => MinusWeeks(this, IntConstantExp(n))
    case Month(n) => MinusMonths(this, IntConstantExp(n))
    case Year(n) => MinusYears(this, IntConstantExp(n))
  }

  def year: IntExp = ExtractYear(this)
  def month: IntExp = ExtractMonth(this)
  def day: IntExp = ExtractDay(this)
  def dayOfYear: IntExp = DayOfYear(this)
  def hour: IntExp = ExtractHour(this)
  def minute: IntExp = ExtractMinute(this)

  def firstOfMonth: DateTimeExp = FirstOfMonth(this)
  def midnight: DateTimeExp = Midnight(this)
  def sunday: DateTimeExp = Sunday(this)
  def yesterday: DateTimeExp = Yesterday(this)
  def inTimeZone(zone: String): DateTimeExp = InTimeZone(this, StringConstantExp(zone))

  def format(myFormat: StringExp): StringExp = Format(this, myFormat)

}

trait DurationExp extends TypedExpression

trait S3UriExp extends TypedExpression

trait HdfsUriExp extends TypedExpression
