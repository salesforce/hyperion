package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpEmrCluster, AdpJsonSerializer}
import com.krux.hyperion.HyperionContext

/**
 * Launch a MapReduce cluster
 */
case class MapReduceCluster(
    id: String = "MapReduceCluster",
    taskInstanceCount: Int = 0
  )(
    implicit val hc: HyperionContext
  ) extends EmrCluster {

  assert(taskInstanceCount >= 0)

  val amiVersion = hc.emrAmiVersion
  val coreInstanceCount = 2

  def instanceCount = 1 + coreInstanceCount + taskInstanceCount

  val bootstrapAction = hc.emrEnvironmentUri.map(
    env => s"${hc.scriptUri}deploy-hyperion-emr-env.sh,$env").toList

  val instanceType = hc.emrInstanceType

  val terminateAfter = hc.emrTerminateAfter

  def withTaskInstanceCount(n: Int) = this.copy(taskInstanceCount = n)
  def forClient(client: String) = this.copy(id = s"${id}_${client}")

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
