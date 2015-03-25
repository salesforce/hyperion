package com.krux.hyperion.objects.aws

/**
 * References to an existing aws data pipeline object
 *
 * more details:
 * http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-pipeline-expressions.html
 */
case class AdpRef[+T <: AdpDataPipelineObject] (objId: String)

object AdpRef {
  def apply[T <: AdpDataPipelineObject](obj: T) = new AdpRef[T](obj.id)
}
