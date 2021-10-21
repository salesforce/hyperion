/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.client

import com.amazonaws.services.datapipeline.DataPipeline
import com.amazonaws.services.datapipeline.model.{DeactivatePipelineRequest,
  DeletePipelineRequest, ActivatePipelineRequest}


case class AwsClientForId(
  client: DataPipeline,
  pipelineIds: Set[String],
  override val maxRetry: Int
) extends AwsClient {

  def deletePipelines(): Option[Unit] = {
    pipelineIds.foreach { id =>
      log.info(s"Deleting pipeline $id")
      client.deletePipeline(new DeletePipelineRequest().withPipelineId(id)).retry()
    }
    Option(())
  }

  def activatePipelines(): Option[AwsClientForId] = {
    pipelineIds.foreach { id =>
      log.info(s"Activating pipeline $id")
      client.activatePipeline(new ActivatePipelineRequest().withPipelineId(id)).retry()
    }
    Option(this)
  }

  def deactivatePipelines(): Option[AwsClientForId] = {
    pipelineIds.foreach { id =>
      log.info(s"Deactivating pipeline $id")

      client
        .deactivatePipeline(
          new DeactivatePipelineRequest().withPipelineId(id).withCancelActive(true)
        )
        .retry()
    }
    Option(this)
  }

}
