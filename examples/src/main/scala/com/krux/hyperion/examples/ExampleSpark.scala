/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.examples

import scala.language.postfixOps

import com.typesafe.config.ConfigFactory

import com.krux.hyperion.action.SnsAlarm
import com.krux.hyperion.activity.{EmrActivity, SparkStep, SparkTaskActivity}
import com.krux.hyperion.common.S3Uri
import com.krux.hyperion.datanode.S3DataNode
import com.krux.hyperion.expression.{Parameter, RuntimeNode}
import com.krux.hyperion.Implicits._
import com.krux.hyperion.resource.{EmrCluster, EmrApplication}
import com.krux.hyperion.{DataPipelineDef, HyperionCli, HyperionContext, Schedule}


object ExampleSpark extends DataPipelineDef with HyperionCli {

  val target = "the-target"
  val jar = s3 / "sample-jars" / "sample-jar-assembly-current.jar"

  override implicit val hc: HyperionContext = new HyperionContext(ConfigFactory.load("example"))

  override lazy val tags = Map("example" -> None, "ownerGroup" -> Some("spark"))

  override lazy val schedule = Schedule.cron
    .startAtActivation
    .every(1.day)
    .stopAfter(3)

  val location = Parameter[S3Uri]("S3Location").withValue(s3"your-location")
  val instanceType = Parameter[String]("InstanceType").withValue("c3.8xlarge")
  val instanceCount = Parameter("InstanceCount", 8)
  val instanceBid = Parameter("InstanceBid", 3.40)

  override def parameters: Iterable[Parameter[_]] = Seq(location, instanceType, instanceCount, instanceBid)

  val dataNode = S3DataNode(s3 / "some-bucket" / "some-path" /)

  // Actions
  val mailAction = SnsAlarm("arn:aws:sns:us-east-1:28619EXAMPLE:ExampleTopic")
    .withSubject(s"Something happened at ${RuntimeNode.ScheduledStartTime}")
    .withMessage(s"Some message $instanceCount x $instanceType @ $instanceBid for $location")
    .withRole("DataPipelineDefaultResourceRole")

  // Resources
  val sparkCluster = EmrCluster()
    .withApplications(EmrApplication.Spark)
    .withTaskInstanceCount(instanceCount)
    .withTaskInstanceType(instanceType)
    .withInitTimeout(5.hours)

  // First activity
  val filterActivity = SparkTaskActivity(jar)(sparkCluster)
    .withMainClass("com.krux.hyperion.FilterJob")
    .named("filterActivity")
    .onFail(mailAction)
    .withInput(dataNode)
    .withArguments(
      target,
      (EmrActivity.ScheduledStartTime - 3.days).format("yyyy-MM-dd")
    )

  // Second activity
  val scoreStep1 = SparkStep(jar)
    .withMainClass("com.krux.hyperion.ScoreJob1")
    .withArguments(
      target,
      (EmrActivity.ScheduledStartTime - 3.days).format("yyyy-MM-dd"),
      "denormalized"
    )

  val scoreStep2 = SparkStep(jar)
    .withMainClass("com.krux.hyperion.ScoreJob2")
    .withArguments(target, (EmrActivity.ScheduledStartTime - 3.days).format("yyyy-MM-dd"))

  val scoreStep3 = SparkStep(jar)
    .withMainClass("com.krux.hyperion.ScoreJob3")
    .withArguments(
      target,
      (EmrActivity.ScheduledStartTime - 3.days).format("yyyy-MM-dd"),
      "value1,value2"
    )

  val scoreStep4 = SparkStep(jar)
    .withMainClass("com.krux.hyperion.ScoreJob4")
    .withArguments(
      target,
      (EmrActivity.ScheduledStartTime - 3.days).format("yyyy-MM-dd"),
      "value1,value2," + (EmrActivity.ScheduledStartTime - 3.days).format("yyyy-MM-dd")
    )

  val scoreActivity = EmrActivity(sparkCluster)
    .named("scoreActivity")
    .withSteps(scoreStep1, scoreStep2, scoreStep3, scoreStep4)
    .onSuccess(mailAction)

  override def workflow = filterActivity ~> scoreActivity

}
