package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpJsonSerializer, AdpDataPipelineDefaultObject, AdpDataPipelineObject, AdpRef, AdpSchedule}
import com.krux.hyperion.HyperionContext

case class DefaultObject(schedule: Schedule)(implicit val hc: HyperionContext)
    extends AdpDataPipelineDefaultObject with PipelineObject {

  val fields = Map[String, Either[String, AdpRef[AdpDataPipelineObject]]](
    "scheduleType" -> Left(schedule.scheduleType.toString),
    "failureAndRerunMode" -> Left(hc.failureRerunMode),
    "pipelineLogUri" -> Left(hc.logUri),
    "role" -> Left(hc.role),
    "resourceRole" -> Left(hc.resourceRole),
    "schedule" -> Right(AdpRef[AdpSchedule](schedule.id))
  )

  def serialize = this

  override def objects = Seq(schedule)

}
