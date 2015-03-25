package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpEmrActivity, AdpJsonSerializer, AdpRef, AdpEmrCluster,
  AdpActivity}

/**
 * Defines a MapReduce activity
 */
case class MapReduceActivity(
    id: String,
    runsOn: EmrCluster,
    steps: Seq[MapReduceStep] = Seq(),
    dependsOn: Seq[PipelineActivity] = Seq()
  ) extends EmrActivity {

  def withStepSeq(steps: Seq[MapReduceStep]) = this.copy(steps = steps)
  def withSteps(steps: MapReduceStep*) = this.copy(steps = steps)

  def dependsOn(activities: EmrActivity*) = this.copy(dependsOn = activities)
  def forClient(client: String) = this.copy(id = s"${id}_${client}")

  override def objects: Iterable[PipelineObject] = Seq(runsOn) ++ dependsOn

  def serialize = AdpEmrActivity(
      id,
      Some(id),
      None,
      None,
      None,
      None,
      AdpRef[AdpEmrCluster](runsOn.id),
      steps.map(_.toStepString),
      dependsOn match {
        case Seq() => None
        case deps => Some(deps.map(d => AdpRef[AdpActivity](d.id)))
      }
    )
}

object MapReduceActivity extends RunnableObject
