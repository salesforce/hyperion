package com.krux.hyperion

import com.typesafe.config.{ConfigFactory, Config}
import scala.util.Try

/**
 * Basic configurations
 */
class HyperionContext(config: Config) {

  def this() = {
    this(ConfigFactory.load)
  }

  val keyPair = Try(config.getString("hyperion.aws.keypair")).toOption
  val region = config.getString("hyperion.aws.region")
  val datapipelineAccessKeyId = config.getString("hyperion.aws.access_key_id")
  val datapipelineAccessKeySecret = config.getString("hyperion.aws.access_key_secret")
  val scriptUri = config.getString("hyperion.script.uri")
  val logUri = config.getString("hyperion.log.uri")

  val role = config.getString("hyperion.role")
  val resourceRole = config.getString("hyperion.resource.role")

  val failureRerunMode = config.getString("hyperion.failure_rerun_mode")

  val ec2SecurityGroup = config.getString("hyperion.aws.ec2.securitygroup")
  val ec2InstanceType = config.getString("hyperion.aws.ec2.instance.type")
  val ec2ImageId = config.getString(s"hyperion.aws.ec2.image.$region")

  val emrAmiVersion = config.getString("hyperion.aws.emr.ami.version")
  val emrInstanceType = config.getString("hyperion.aws.emr.instance.type")

  val ec2TerminateAfter = config.getString("hyperion.aws.ec2.terminate")
  val emrTerminateAfter = config.getString("hyperion.aws.emr.terminate")
  val emrEnvironmentUri = Try(config.getString("hyperion.aws.emr.env.uri")).toOption

  val sparkVersion = config.getString("hyperion.aws.emr.spark.version")

}
