package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpEmrCluster, AdpRef}

trait EmrCluster extends ResourceObject {
  def serialize: AdpEmrCluster
  def ref: AdpRef[AdpEmrCluster] = AdpRef(serialize)
}
