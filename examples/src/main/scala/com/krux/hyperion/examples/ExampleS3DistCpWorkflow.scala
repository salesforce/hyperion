/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.examples

import com.typesafe.config.ConfigFactory

import com.krux.hyperion.activity.S3DistCpActivity
import com.krux.hyperion.activity.S3DistCpActivity.OutputCodec
import com.krux.hyperion.common.HdfsUri
import com.krux.hyperion.expression.Parameter
import com.krux.hyperion.Implicits._
import com.krux.hyperion.resource.EmrCluster
import com.krux.hyperion.{DataPipelineDef, HyperionCli, HyperionContext, Schedule}


object ExampleS3DistCpWorkflow extends DataPipelineDef with HyperionCli {

  val source = "the-source"
  val jar = s3 / "sample-jars" / "sample-jar-assembly-current.jar"

  override implicit val hc: HyperionContext = new HyperionContext(ConfigFactory.load("example"))

  override lazy val tags = Map("example" -> None, "ownerGroup" -> Some("s3DistCP"))

  override lazy val schedule = Schedule.cron
    .startAtActivation
    .every(1.day)
    .stopAfter(3)

  val target = Parameter[HdfsUri]("HdfsLocation").withValue(hdfs"the-target")
  val instanceType = Parameter[String]("InstanceType").withValue("c3.8xlarge")
  val instanceCount = Parameter("InstanceCount", 8)
  val instanceBid = Parameter("InstanceBid", 3.40)

  override def parameters: Iterable[Parameter[_]] = Seq(target, instanceType, instanceCount, instanceBid)

  // Resources
  val emrCluster = EmrCluster()
    .withTaskInstanceCount(instanceCount)
    .withTaskInstanceType(instanceType)
    .withReleaseLabel("emr-4.4.0")
    .named("Cluster with release label")

  val s3DistCPActivity = S3DistCpActivity(emrCluster)
    .named("s3DistCpActivity")
    .withSource(
      s3 / source
    )
    .withDestination(
      target
    )
    .withOutputCodec(
      OutputCodec.Gz
    )

  override def workflow = s3DistCPActivity.toWorkflowExpression
}
