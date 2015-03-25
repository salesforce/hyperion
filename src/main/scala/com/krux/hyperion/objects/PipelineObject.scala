package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpDataPipelineAbstractObject, AdpJsonSerializer,
  AdpPipelineSerializer}
import com.amazonaws.services.datapipeline.model.{PipelineObject => AwsPipelineObject}


/**
 * The base trait of krux data pipeline objects.
 */
trait PipelineObject {

  def id: String
  def objects: Iterable[PipelineObject] = None
  def serialize: AdpDataPipelineAbstractObject

}
