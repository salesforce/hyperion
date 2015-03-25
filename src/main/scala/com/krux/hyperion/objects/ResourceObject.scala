package com.krux.hyperion.objects
import com.krux.hyperion.HyperionContext

trait ResourceObject extends PipelineObject {
  implicit val hc: HyperionContext
  val keyPair = hc.keyPair
}
