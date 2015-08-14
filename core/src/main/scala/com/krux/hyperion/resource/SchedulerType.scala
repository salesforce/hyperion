package com.krux.hyperion.resource

trait SchedulerType

case object ParallelFairScheduler extends SchedulerType {
  override val toString = "PARALLEL_FAIR_SCHEDULING"
}

case object ParallelCapacityScheduler extends SchedulerType {
  override val toString = "PARALLEL_CAPACITY_SCHEDULING"
}

case object DefaultScheduler extends SchedulerType {
  override val toString = "DEFAULT_SCHEDULER"
}
