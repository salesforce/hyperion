package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpDataFormat, AdpRef}

trait DataFormat extends PipelineObject {
  def serialize: AdpDataFormat
  def ref: AdpRef[AdpDataFormat] = AdpRef(serialize)
}
