/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.client

import com.amazonaws.auth.{DefaultAWSCredentialsProviderChain, STSAssumeRoleSessionCredentialsProvider}
import com.amazonaws.regions.Regions
import com.amazonaws.services.datapipeline.{DataPipelineClientBuilder, DataPipeline}
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import org.slf4j.LoggerFactory

import com.krux.hyperion.DataPipelineDefGroup
import com.krux.stubborn.policy.ExponentialBackoffAndJitter
import com.krux.stubborn.Retryable


trait AwsClient extends Retryable with ExponentialBackoffAndJitter {

  lazy val log = LoggerFactory.getLogger(getClass)

  def client: DataPipeline

  override def base: Int = 3000

  override def cap: Int = 24000 // theoretical max retry delay with 3 retries

}

object AwsClient {

  def getClient(regionId: Option[String] = None, roleArn: Option[String] = None): DataPipeline = {

    val region: Option[Regions] = regionId.map(r => Regions.fromName(r))

    lazy val defaultProvider = new DefaultAWSCredentialsProviderChain()

    lazy val stsProvider =
      roleArn.map { r =>
        new STSAssumeRoleSessionCredentialsProvider.Builder(r, "hyperion")
          .withStsClient(
            AWSSecurityTokenServiceClientBuilder
              .standard()
              .withCredentials(defaultProvider)
              .build()
          )
          .build()
      }

    val defaultBuilder = DataPipelineClientBuilder
      .standard()
      .withCredentials(stsProvider.getOrElse(defaultProvider))

    region.map(defaultBuilder.withRegion(_)).getOrElse(defaultBuilder).build()
  }

  def apply(
      pipelineDef: DataPipelineDefGroup,
      regionId: Option[String],
      roleArn: Option[String]
    ): AwsClientForDef =
    new AwsClientForDef(getClient(regionId, roleArn), pipelineDef)

}
