/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.resource

import org.slf4j.LoggerFactory

import com.krux.hyperion.adt.HString
import com.krux.hyperion.common.{PipelineObjectId, BaseFields}
import com.krux.hyperion.HyperionContext


/**
 * Launch a Spark cluster (pre EMR release label 4.0.0)
 */
@deprecated("Use EmrCluster with Spark Application instead", "5.0.0")
case class SparkCluster private (
  baseFields: BaseFields,
  resourceFields: ResourceFields,
  emrClusterFields: EmrClusterFields,
  sparkVersion: Option[HString]
) extends BaseEmrCluster {

  type Self = SparkCluster

  val logger = LoggerFactory.getLogger(SparkCluster.getClass)

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateResourceFields(fields: ResourceFields) = copy(resourceFields = fields)
  def updateEmrClusterFields(fields: EmrClusterFields) = copy(emrClusterFields = fields)

  def withAmiVersion(version: HString): Self = updateEmrClusterFields(
    emrClusterFields.copy(amiVersion = Option(version), releaseLabel = None)
  )

  def withSparkVersion(sparkVersion: HString) = copy(sparkVersion = Option(sparkVersion))

  override def applications = if (releaseLabel.nonEmpty)
    EmrApplication.Spark +: super.applications
  else
    super.applications

  override def standardBootstrapAction = if (releaseLabel.nonEmpty)
    super.standardBootstrapAction
  else
    sparkVersion.map(version => s"s3://support.elasticmapreduce/spark/install-spark,-v,${version},-x": HString).toSeq ++ super.standardBootstrapAction

}

@deprecated("Use EmrCluster with Spark Application instead", "5.0.0")
object SparkCluster {

  def apply()(implicit hc: HyperionContext): SparkCluster = new SparkCluster(
    baseFields = BaseFields(PipelineObjectId(SparkCluster.getClass)),
    resourceFields = BaseEmrCluster.defaultResourceFields(hc),
    emrClusterFields = LegacyEmrCluster.defaultEmrClusterFields(hc),
    sparkVersion = hc.emrSparkVersion
  )

}
