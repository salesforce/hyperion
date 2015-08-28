package com.krux.hyperion

import scala.language.implicitConversions

import com.krux.hyperion.activity.PipelineActivity
import com.krux.hyperion.util.WorkflowGraph

sealed abstract class WorkflowExpression {

  def toPipelineObjects: Iterable[PipelineActivity] = {

    def toWorkflowGraph(exp: WorkflowExpression): WorkflowGraph = {
      exp match {
        case WorkflowNoActivityExpression =>
          new WorkflowGraph()
        case WorkflowActivityExpression(act) =>
          new WorkflowGraph(act)
        case WorkflowArrowExpression(left, right) =>
          toWorkflowGraph(left) ~> toWorkflowGraph(right)
        case WorkflowPlusExpression(left, right) =>
          toWorkflowGraph(left) ++ toWorkflowGraph(right)
      }
    }

    toWorkflowGraph(this).toActivities
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

case class WorkflowActivityExpression(activity: PipelineActivity) extends WorkflowExpression

case class WorkflowArrowExpression(left: WorkflowExpression, right: WorkflowExpression) extends WorkflowExpression

case class WorkflowPlusExpression(left: WorkflowExpression, right: WorkflowExpression) extends WorkflowExpression

object WorkflowExpression {

  implicit def workflowIterable2WorkflowExpression(activities: Iterable[WorkflowExpression]): WorkflowExpression =
    activities.reduceLeft(_ + _)

  implicit def activityIterable2WorkflowExpression(activities: Iterable[PipelineActivity]): WorkflowExpression =
    activities.map(activity2WorkflowExpression).reduceLeft(_ + _)

  implicit def activity2WorkflowExpression(activity: PipelineActivity): WorkflowExpression =
    WorkflowActivityExpression(activity)

}
