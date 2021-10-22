/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion

import com.krux.hyperion.expression.Parameter
import com.krux.hyperion.workflow.WorkflowExpression


/**
  * DataPipelineDefWrapper provides a way to wrap other DataPipelineDefs
  * in order to override aspects.
  */
case class DataPipelineDefWrapper private[hyperion] (
  override val hc: HyperionContext,
  override val pipelineName: String,
  schedule: Schedule,
  override val pipelineLifeCycle: PipelineLifeCycle,
  workflowFunc: () => WorkflowExpression,  // for delayed workflow execution
  override val tags: Map[String, Option[String]],
  override val parameters: Iterable[Parameter[_]]
) extends DataPipelineDef {

  def withName(name: String) = copy(pipelineName = name)
  def withSchedule(schedule: Schedule) = copy(schedule = schedule)
  def withPipelineLifeCycle(pipelineLifeCycle: PipelineLifeCycle) = copy(pipelineLifeCycle = pipelineLifeCycle)
  def withTags(tags: Map[String, Option[String]]) = copy(tags = this.tags ++ tags)
  def withParameters(parameters: Iterable[Parameter[_]]) = copy(parameters = parameters)

  def workflow = workflowFunc()

}

object DataPipelineDefWrapper {

  def apply(inner: DataPipelineDef): DataPipelineDefWrapper = DataPipelineDefWrapper(
    inner.hc,
    inner.pipelineName,
    inner.schedule,
    inner.pipelineLifeCycle,
    () => inner.workflow,
    inner.tags,
    inner.parameters
  )

}
