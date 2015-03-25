package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpJsonSerializer, AdpSqlActivity, AdpRef,
  AdpRedshiftDatabase, AdpEc2Resource, AdpActivity}
import com.krux.hyperion.HyperionContext

/**
 * Redshift unload activity
 */
case class RedshiftUnloadActivity(
    id: String,
    database: RedshiftDatabase,
    script: String,
    s3Path: String,
    runsOn: Ec2Resource,
    unloadOptions: Seq[RedshiftUnloadOption] = Seq(),
    dependsOn: Seq[PipelineActivity] = Seq()
  )(
    implicit val hc: HyperionContext
  ) extends PipelineActivity {

  def dependsOn(activities: PipelineActivity*) = this.copy(dependsOn = activities)

  def unloadScript = s"""
    UNLOAD ('${script.replaceAll("'", "\\\\\\\\'")}')
    TO '$s3Path'
    WITH CREDENTIALS AS
    'aws_access_key_id=${hc.datapipelineAccessKeyId};aws_secret_access_key=${hc.datapipelineAccessKeySecret}'
    ${unloadOptions.map(_.repr).flatten.mkString(" ")}
  """

  def withUnloadOptions(opts: RedshiftUnloadOption*) = this.copy(unloadOptions = opts)

  override def objects: Iterable[PipelineObject] = Seq(database, runsOn) ++ dependsOn

  def serialize = AdpSqlActivity(
      id = id,
      name = Some(id),
      database = AdpRef[AdpRedshiftDatabase](database.id),
      script = unloadScript,
      scriptArgument = None,
      queue = None,
      dependsOn = dependsOn match {
        case Seq() => None
        case deps => Some(deps.map(act => AdpRef[AdpActivity](act.id)))
      },
      runsOn = AdpRef[AdpEc2Resource](runsOn.id)
    )

}
