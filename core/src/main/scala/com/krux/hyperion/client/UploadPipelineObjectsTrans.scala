/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.client

import scala.jdk.CollectionConverters._

import com.amazonaws.services.datapipeline.DataPipeline
import com.amazonaws.services.datapipeline.model.{CreatePipelineRequest, InvalidRequestException,
  PipelineObject, PutPipelineDefinitionRequest, Tag}
import org.slf4j.LoggerFactory

import com.krux.hyperion.DataPipelineDefGroup
import com.krux.hyperion.PipelineLifeCycle.Status
import com.krux.stubborn.policy.ExponentialBackoffAndJitter
import com.krux.stubborn.Retryable
import scala.collection.compat.immutable.LazyList

case class UploadPipelineObjectsTrans(
  client: DataPipeline,
  pipelineDef: DataPipelineDefGroup,
  override val maxRetry: Int
) extends Transaction[Option[Unit], AwsClientForId] with Retryable with ExponentialBackoffAndJitter {

  val log = LoggerFactory.getLogger(getClass)

  val parameterObjects = pipelineDef.toAwsParameters

  val keyObjectsMap = pipelineDef.toAwsPipelineObjects

  private def createAndUploadObjects(name: String, objects: Seq[PipelineObject]): Option[String] = {

    val pipelineId = client
      .createPipeline(
        new CreatePipelineRequest()
          .withUniqueId(name)
          .withName(name)
          .withTags(
            pipelineDef.tags.toSeq
              .map { case (k, v) => new Tag().withKey(k).withValue(v.getOrElse("")) }
              .asJava
          )
      )
      .retry()
      .getPipelineId

    log.info(s"Created pipeline $pipelineId ($name)")
    log.info(s"Uploading pipeline definition to $pipelineId")

    pipelineDef.pipelineLifeCycle.onCreated(pipelineId, name, Status.Success)

    try {

      val putDefinitionResult = client
        .putPipelineDefinition(
          new PutPipelineDefinitionRequest()
            .withPipelineId(pipelineId)
            .withPipelineObjects(objects.asJava)
            .withParameterObjects(parameterObjects.asJava)
        )
        .retry()

      putDefinitionResult.getValidationErrors.asScala
        .flatMap(err => err.getErrors.asScala.map(detail => s"${err.getId}: $detail"))
        .foreach(log.error)
      putDefinitionResult.getValidationWarnings.asScala
        .flatMap(err => err.getWarnings.asScala.map(detail => s"${err.getId}: $detail"))
        .foreach(log.warn)

      if (putDefinitionResult.getErrored) {
        log.error(s"Failed to upload pipeline definition to pipeline $pipelineId")
        log.error(s"Deleting the just created pipeline $pipelineId")
        AwsClientForId(client, Set(pipelineId), maxRetry).deletePipelines()
        //Pipeline Creation Failed. Update pipelineLifeCycle.
        pipelineDef.pipelineLifeCycle.onUploaded(name, pipelineId, Status.Fail)
        None
      } else if (putDefinitionResult.getValidationErrors.isEmpty
        && putDefinitionResult.getValidationWarnings.isEmpty) {
        log.info("Successfully created pipeline")
        //Pipeline Created Successfully. Update pipelineLifeCycle.
        pipelineDef.pipelineLifeCycle.onUploaded(name, pipelineId, Status.Success)
        Option(pipelineId)
      } else {
        log.warn("Successful with warnings")
        //Pipeline Created with warnings. Update pipelineLifeCycle.
        pipelineDef.pipelineLifeCycle.onUploaded(name, pipelineId, Status.SuccessWithWarnings)
        Option(pipelineId)
      }

    } catch {
      case e: InvalidRequestException =>
        log.error(s"InvalidRequestException (${e.getErrorCode}): ${e.getErrorMessage}")
        log.error("Deleting the just created pipeline")
        AwsClientForId(client, Set(pipelineId), maxRetry).deletePipelines()
        //Pipeline Creation Failed. Update pipelineLifeCycle.
        pipelineDef.pipelineLifeCycle.onUploaded(name, pipelineId, Status.Fail)
        None
    }

  }

  def action() = AwsClientForId(
    client,
    keyObjectsMap
      .to(LazyList)  // there is no need to keep perform createAndUploadObojects if one failed
      .map { case (key, objects) =>
        log.info(s"Creating pipeline and uploading ${objects.size} objects")
        createAndUploadObjects(pipelineDef.nameForKey(key), objects)
      }
      .takeWhile(_.nonEmpty)
      .flatten
      .toSet,
    maxRetry
  )

  def validate(result: AwsClientForId) = result.pipelineIds.size == keyObjectsMap.size

  def rollback(result: AwsClientForId) = result.deletePipelines()

}
