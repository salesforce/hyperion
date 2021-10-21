/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion

import scala.jdk.CollectionConverters._
import scala.util.{Random, Try}

import com.typesafe.config.{Config, ConfigFactory}

import com.krux.hyperion.expression.Duration

/**
 * Basic configurations
 */
class HyperionContext(config: Config) {

  def this() = this(ConfigFactory.load)

  lazy val scriptUri = config.getString("hyperion.script.uri")
  lazy val logUri = Try(config.getString("hyperion.log.uri")).toOption

  //
  // Default values
  //
  lazy val failureRerunMode = config.getString("hyperion.failure_rerun_mode")
  lazy val role = config.getString("hyperion.role")
  lazy val resourceRole = config.getString("hyperion.resource.role")
  lazy val region = config.getString("hyperion.aws.region")
  lazy val keypair = Try(config.getString("hyperion.aws.keypair")).toOption

  //
  // EC2 default configuration
  //
  lazy val ec2Region = Try(config.getString("hyperion.aws.ec2.region")).toOption.getOrElse(region)
  lazy val ec2KeyPair = Try(config.getString("hyperion.aws.ec2.keypair")).toOption.orElse(keypair)
  lazy val ec2AvailabilityZone = Try(config.getString("hyperion.aws.ec2.availability_zone")).toOption
  lazy val ec2SubnetId =
    Try(pickOneRandom(config.getStringList("hyperion.aws.ec2.subnets").asScala)).toOption

  lazy val ec2Role = Try(config.getString("hyperion.aws.ec2.role")).toOption.getOrElse(role)
  lazy val ec2ResourceRole = Try(config.getString("hyperion.aws.ec2.resource.role")).toOption.getOrElse(resourceRole)

  lazy val ec2SecurityGroup = Try(config.getString("hyperion.aws.ec2.securitygroup")).toOption
  lazy val ec2SecurityGroupId = Try(config.getString("hyperion.aws.ec2.securitygroupid")).toOption
  lazy val ec2InstanceType = config.getString("hyperion.aws.ec2.instance.type")
  lazy val ec2ImageId = config.getString(s"hyperion.aws.ec2.image.$ec2Region")
  lazy val ec2TerminateAfter = Try(config.getString("hyperion.aws.ec2.terminate")).toOption.map(Duration(_))
  lazy val ec2EnvironmentUri = Try(config.getString("hyperion.aws.ec2.env.uri")).toOption
  lazy val ec2InitTimeout = Try(config.getString("hyperion.aws.ec2.inittimeout")).toOption.map(Duration(_))

  //
  // EMR default configuration
  //
  lazy val emrRegion = Try(config.getString("hyperion.aws.emr.region")).toOption.getOrElse(region)
  lazy val emrKeyPair = Try(config.getString("hyperion.aws.emr.keypair")).toOption.orElse(keypair)
  lazy val emrAvailabilityZone = Try(config.getString("hyperion.aws.emr.availability_zone")).toOption
  lazy val emrSubnetId = Try(config.getString("hyperion.aws.emr.subnet")).toOption
  lazy val emrRole = Try(config.getString("hyperion.aws.emr.role")).toOption.getOrElse(role)
  lazy val emrResourceRole = Try(config.getString("hyperion.aws.emr.resource.role")).toOption.getOrElse(resourceRole)

  lazy val emrAmiVersion = Try(config.getString("hyperion.aws.emr.ami.version")).toOption
  lazy val emrReleaseLabel = Try(config.getString("hyperion.aws.emr.release_label")).toOption
  lazy val emrInstanceType = config.getString("hyperion.aws.emr.instance.type")

  // Only supported by LegacyEmrCluster
  lazy val emrEnvironmentUri = Try(config.getString("hyperion.aws.emr.env.uri")).toOption

  lazy val emrTerminateAfter = Try(config.getString("hyperion.aws.emr.terminate")).toOption.map(Duration(_))
  lazy val emrSparkVersion = Try(config.getString("hyperion.aws.emr.spark.version")).toOption
  lazy val emrInitTimeout = Try(config.getString("hyperion.aws.emr.inittimeout")).toOption.map(Duration(_))

  //
  // SNS default configuration
  //
  lazy val snsRole = Try(config.getString("hyperion.aws.sns.role")).toOption
  lazy val snsTopic = Try(config.getString("hyperion.aws.sns.topic")).toOption

  //
  // Graphviz styles
  //
  lazy val graphStyles = config.getConfig("hyperion.graphviz.styles")

  //
  // Aws client configuration
  //
  lazy val maxRetry = config.getInt("hyperion.aws.client.max_retry")

  private def pickOneRandom[T](elems: collection.Seq[T]): T = elems(new Random().nextInt(elems.size))

}
