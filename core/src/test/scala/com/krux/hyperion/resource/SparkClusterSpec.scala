package com.krux.hyperion.resource

import com.krux.hyperion.HyperionContext
import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

@deprecated("Use EmrCluster instead", "5.0.0")
class SparkClusterSpec extends AnyFlatSpec with Matchers {
  implicit val hc = new HyperionContext(ConfigFactory.load("example"))

  it should "handle old spark versions" in {
    val cluster = SparkCluster()
    cluster.standardBootstrapAction.length shouldBe 2
    cluster.applications.length shouldBe 0
  }
}
