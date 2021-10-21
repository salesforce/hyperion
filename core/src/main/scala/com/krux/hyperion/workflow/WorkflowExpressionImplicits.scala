/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.workflow

import scala.language.implicitConversions

import com.krux.hyperion.activity.PipelineActivity
import com.krux.hyperion.resource.ResourceObject

trait WorkflowExpressionImplicits {

  implicit def workflowIterable2WorkflowExpression(activities: Iterable[WorkflowExpression]): WorkflowExpression =
    activities.foldLeft(WorkflowExpression.empty)(_ + _)

  implicit def activityIterable2WorkflowExpression(activities: Iterable[PipelineActivity[_ <: ResourceObject]]): WorkflowExpression =
    activities.foldLeft(WorkflowExpression.empty)(_ + _)

  implicit def activity2WorkflowExpression(activity: PipelineActivity[_ <: ResourceObject]): WorkflowExpression =
    WorkflowActivityExpression(activity)

  implicit class activityIterable2WorkflowExpressionOps(activities: Iterable[PipelineActivity[_ <: ResourceObject]]) {
    def toWorkflowExpression(reducer: (WorkflowExpression, PipelineActivity[_ <: ResourceObject]) => WorkflowExpression = _ + _): WorkflowExpression =
      activities.foldLeft(WorkflowExpression.empty)(reducer)
  }

  implicit class activityWorkflowExpressionOps(activity: PipelineActivity[_ <: ResourceObject]) {
    def toWorkflowExpression: WorkflowExpression = activity
  }

}
