package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpRedshiftDataNode, AdpJsonSerializer, AdpRef}

/**
 * The abstracted RedshiftDataNode
 */
case class RedshiftDataNode (
    id: String,
    database: RedshiftDatabase,
    tableName: String,
    createTableSql: Option[String] = None,
    schemaName: Option[String] = None,
    primaryKeys: Option[Seq[String]] = None
  ) extends PipelineObject {

  def withCreateTableSql(createSql: String) = this.copy(createTableSql = Some(createSql))
  def withSchema(theSchemaName: String) = this.copy(schemaName = Some(theSchemaName))
  def withPrimaryKeys(pks: String*) = this.copy(primaryKeys = Some(pks))

  override def objects: Iterable[PipelineObject] = Some(database)

  def serialize = AdpRedshiftDataNode(
      id,
      Some(id),
      createTableSql,
      AdpRef(database.id),
      schemaName,
      tableName,
      primaryKeys
    )

}
