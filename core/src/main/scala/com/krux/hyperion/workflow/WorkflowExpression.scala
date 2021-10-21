/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.workflow

import com.krux.hyperion.activity.PipelineActivity
import com.krux.hyperion.common.PipelineObject
import com.krux.hyperion.resource.ResourceObject

sealed abstract class WorkflowExpression {

  def toActivities: Iterable[PipelineActivity[_ <: ResourceObject]] = WorkflowGraph(this).toActivities

  def toPipelineObjects: Iterable[PipelineObject] = {

    def flattenPipelineObjects(r: Map[String, PipelineObject], po: PipelineObject): Map[String, PipelineObject] =
      if (r.contains(po.id.toString)) r
      else r ++ Map(po.id.toString -> po) ++ po.objects.foldLeft(r)(flattenPipelineObjects)

    toActivities
      .foldLeft(Map.empty[String, PipelineObject])(flattenPipelineObjects)
      .values
  }

  def andThen(right: WorkflowExpression): WorkflowExpression = right match {
    case WorkflowNoActivityExpression => this
    case r if this == WorkflowNoActivityExpression => r
    case r => WorkflowArrowExpression(this, r)
  }

  def ~>(right: WorkflowExpression): WorkflowExpression = this.andThen(right)

  def and(right: WorkflowExpression): WorkflowExpression = right match {
    case WorkflowNoActivityExpression => this
    case r if this == WorkflowNoActivityExpression => r
    case r => WorkflowPlusExpression(this, right)
  }

  def +(right: WorkflowExpression): WorkflowExpression = this.and(right)
}

case object WorkflowNoActivityExpression extends WorkflowExpression

case class WorkflowActivityExpression(activity: PipelineActivity[_ <: ResourceObject]) extends WorkflowExpression

case class WorkflowArrowExpression(left: WorkflowExpression, right: WorkflowExpression) extends WorkflowExpression

case class WorkflowPlusExpression(left: WorkflowExpression, right: WorkflowExpression) extends WorkflowExpression

object WorkflowExpression extends WorkflowExpressionImplicits {
  def empty: WorkflowExpression = WorkflowNoActivityExpression
}
