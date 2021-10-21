/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.examples

import com.typesafe.config.ConfigFactory

import com.krux.hyperion.activity.{GoogleStorageDownloadActivity, ShellCommandActivity}
import com.krux.hyperion.expression.Parameter
import com.krux.hyperion.Implicits._
import com.krux.hyperion.resource.Ec2Resource
import com.krux.hyperion.{DataPipelineDef, HyperionCli, HyperionContext, Schedule}


object ExampleWorkflow extends DataPipelineDef with HyperionCli {

  override implicit val hc: HyperionContext = new HyperionContext(ConfigFactory.load("example"))

  override lazy val schedule = Schedule.cron
    .startAtActivation
    .every(1.day)

  val price = Parameter("SpotPrice", 2.3)

  override def parameters = Seq(price)

  val ec2 = Ec2Resource()
    .withSpotBidPrice(price) // Could also put 2.3 directly here

  // First activity
  val act1 = ShellCommandActivity("run act1")(ec2).named("act1")
  val act2 = ShellCommandActivity("run act2")(ec2).named("act2")
  val act3 = ShellCommandActivity("run act3")(ec2).named("act3")
  val act4 = ShellCommandActivity("run act4")(ec2).named("act4")
  val act5 = ShellCommandActivity("run act5")(ec2).named("act5")
  val act6 = ShellCommandActivity("run act6")(ec2).named("act6")
  val act7 = GoogleStorageDownloadActivity(s3 / "gsutil.config", "gs://input_location")(ec2).named("act7")
  val act8 = GoogleStorageDownloadActivity(s3 / "gsutil.config", "gs://input_location")(ec2).named("act8").ifExists

  // run act1 first, and then run act2 and act3 at the same time, and then run act4 and act5 the
  // same time, at last run act6
  // Anoternative syntax would be:
  // act1 andThen (act2 and act3) andThen (act4 and act5) andThen act6
  override def workflow = act1 ~> (act2 + act3) ~> (act4 + act5) ~> act6 ~> act7 ~> act8

}
