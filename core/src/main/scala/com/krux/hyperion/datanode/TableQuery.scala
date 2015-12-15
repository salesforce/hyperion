package com.krux.hyperion.datanode

sealed trait TableQuery {
  def table: String
  def columns: List[String]
  def columnsString = columns.mkString(", ")
  def sql: String
}

case class SelectTableQuery(table: String, columns: List[String], whereClause: Option[String]) extends TableQuery {

  val sql = s"select $columnsString from $table ${whereClause.map("where " + _).getOrElse("")}"

}

case class InsertTableQuery(table: String, columns: List[String]) extends TableQuery {

  val valuesString = columns.map(_ => "?").mkString(", ")

  val sql = s"insert into $table ($columnsString) values ($valuesString)"

}
