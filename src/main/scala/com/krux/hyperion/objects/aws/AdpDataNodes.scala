package com.krux.hyperion.objects.aws


/**
 * AWS Data Pipeline DataNode objects
 *
 * ref: http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-object-datanodes.html
 */
trait AdpDataNode extends AdpDataPipelineObject

/**
 * Defines a data node using Amazon S3.
 */
trait AdpS3DataNode extends AdpDataNode {

  /** The type of compression for the data described by the S3DataNode. none is no compression and
   * gzip is compressed with the gzip algorithm. This field is only supported for use with Amazon
   * Redshift and when you use S3DataNode with CopyActivit
   */
  def compression: Option[String]

  /** The format of the data described by the S3DataNode.
   */
  def dataFormat: Option[AdpRef[AdpDataFormat]]

  /** The Amazon S3 path to a manifest file in the format supported by Amazon Redshift. AWS Data
   * Pipeline uses the manifest file to copy the specified Amazon S3 files into the Amazon Redshift
   * table. This field is valid only when a RedshiftCopyActivity references the S3DataNode. For
   * more information, see Using a manifest to specify data files.
   */
  def manifestFilePath: Option[String]

  val `type` = "S3DataNode"

}

/** You must provide either a filePath or directoryPath value.
 */
case class AdpS3DirectoryDataNode(
    id: String,
    name: Option[String],
    compression: Option[String],
    dataFormat: Option[AdpRef[AdpDataFormat]],
    directoryPath: String,
    manifestFilePath: Option[String]
  ) extends AdpS3DataNode

/** You must provide either a filePath or directoryPath value.
 */
case class AdpS3FileDataNode(
    id: String,
    name: Option[String],
    compression: Option[String],
    dataFormat: Option[AdpRef[AdpDataFormat]],
    filePath: String,
    manifestFilePath: Option[String]
  ) extends AdpS3DataNode

/**
 * Defines a data node using Amazon Redshift.
 *
 * @param primaryKeys If you do not specify primaryKeys for a destination table in
 * RedShiftCopyActivity, you can specify a list of columns using primaryKeys which will act as a
 * mergeKey. However, if you have an existing primaryKey defined in a Redshift table, this setting
 * overrides the existing key.
 */
case class AdpRedshiftDataNode(
    id: String,
    name: Option[String],
    createTableSql: Option[String],
    database: AdpRef[AdpRedshiftDatabase],
    schemaName: Option[String],
    tableName: String,
    primaryKeys: Option[Seq[String]]
  ) extends AdpDataNode {
  val `type` = "RedshiftDataNode"
}
