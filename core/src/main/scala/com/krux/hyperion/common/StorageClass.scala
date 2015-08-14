package com.krux.hyperion.common

trait StorageClass

object StorageClass {
  object Standard extends StorageClass {
    override val toString = "STANDARD"
  }

  object ReducedRedundancy extends StorageClass {
    override val toString = "REDUCED_REDUNDANCY"
  }
}
