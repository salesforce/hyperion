package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpEmrCluster, AdpJsonSerializer}
import com.krux.hyperion.HyperionContext

/**
 * Launch a Spark cluster
 */
case class SparkCluster (
    id: String,
    taskInstanceCount: Int,
    coreInstanceCount: Int,
    instanceType: String,
    amiVersion: String,
    sparkVersion: String,
    terminateAfter: String
  )(
    implicit val hc: HyperionContext
  ) extends EmrCluster {

  assert(taskInstanceCount >= 0)

  def instanceCount = 1 + coreInstanceCount + taskInstanceCount

  val bootstrapAction = s"s3://support.elasticmapreduce/spark/install-spark,-v,$sparkVersion,-x" ::
      hc.emrEnvironmentUri.map(env => s"${hc.scriptUri}deploy-hyperion-emr-env.sh,$env").toList

  def forClient(client: String) = this.copy(id = s"${id}_${client}")
  def withTaskInstanceCount(n: Int) = this.copy(taskInstanceCount = n)

  def runSpark(name: String) = SparkActivity(name, this)
  def runMapReduce(name: String) = MapReduceActivity(name, this)

  def serialize = AdpEmrCluster(
      id,
      Some(id),
      bootstrapAction,
      Some(amiVersion),
      Some(instanceType),
      Some(instanceType),
      Some(coreInstanceCount.toString),
      Some(instanceType),
      Some(taskInstanceCount.toString),
      terminateAfter,
      keyPair
    )

}

object SparkCluster {
  def apply()(implicit hc: HyperionContext) = {
    new SparkCluster(
      id = "SparkCluster",
      taskInstanceCount = 0,
      coreInstanceCount = 2,
      instanceType = hc.emrInstanceType,
      amiVersion = hc.emrAmiVersion,
      sparkVersion = hc.sparkVersion,
      terminateAfter = hc.emrTerminateAfter
    )
  }
}
