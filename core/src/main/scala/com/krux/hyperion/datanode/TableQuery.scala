/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.datanode

sealed trait TableQuery {
  def table: String
  def columns: List[String]
  def columnsString: String = columns.mkString(", ")
  def sql: String
}

case class SelectTableQuery(table: String, columns: List[String], whereClause: Option[String], distinct: Boolean = false) extends TableQuery {

  val sql = s"select ${if (distinct) "distinct" else ""} $columnsString from $table ${whereClause.fold("")("where " + _)}"

}

case class InsertTableQuery(table: String, columns: List[String]) extends TableQuery {

  val valuesString = columns.map(_ => "?").mkString(", ")

  val sql = s"insert into $table ($columnsString) values ($valuesString)"

}
