package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpDatabase, AdpRef}

trait Database extends PipelineObject {
  def serialize: AdpDatabase
  def ref: AdpRef[AdpDatabase] = AdpRef(serialize)
}
