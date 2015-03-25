package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpRedshiftCopyActivity, AdpRef, AdpJsonSerializer,
  AdpActivity, AdpS3DataNode, AdpRedshiftDataNode, AdpEc2Resource}

/**
 * Redshift copy activity
 */
case class RedshiftCopyActivity(
    id: String,
    input: S3DataNode,
    insertMode: RedshiftCopyActivity.InsertMode,
    runsOn: Ec2Resource,
    output: RedshiftDataNode,
    transformSql: Option[String] = None,
    commandOptions: Seq[RedshiftCopyOption] = Seq(),
    dependsOn: Seq[PipelineActivity] = Seq()
  ) extends PipelineActivity {

  def dependsOn(activities: PipelineActivity*) = this.copy(dependsOn = activities)

  def withCopyOptions(opts: RedshiftCopyOption*) = this.copy(commandOptions = opts)

  def withTransformSql(sql: String) = this.copy(transformSql = Some(sql))

  override def objects: Iterable[PipelineObject] = Seq(input, runsOn, output) ++ dependsOn

  def serialize = AdpRedshiftCopyActivity(
      id,
      Some(id),
      AdpRef[AdpS3DataNode](input.id),
      insertMode.toString,
      AdpRef[AdpRedshiftDataNode](output.id),
      AdpRef[AdpEc2Resource](runsOn.id),
      transformSql,
      commandOptions match {
        case Seq() => None
        case opts => Some(opts.map(_.repr).flatten)
      },
      None,
      dependsOn match {
        case Seq() => None
        case deps => Some(deps.map(act => AdpRef[AdpActivity](act.id)))
      }
    )
}

object RedshiftCopyActivity extends Enumeration with RunnableObject {

  type InsertMode = Value
  val KeepExisting = Value("KEEP_EXISTING")
  val OverwriteExisting = Value("OVERWRITE_EXISTING")
  val Truncate = Value("TRUNCATE")

}
