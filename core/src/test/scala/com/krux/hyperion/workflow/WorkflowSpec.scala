package com.krux.hyperion.workflow

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import com.krux.hyperion.DataPipelineDef
import com.krux.hyperion.Schedule.onceAtActivation
import com.krux.hyperion.activity.Script.string2Script
import com.krux.hyperion.activity.ShellCommandActivity
import com.krux.hyperion.resource.WorkerGroup

class WorkflowSpec extends AnyFlatSpec with Matchers {
  it should "not require logUri" in {
    object NoConfigWorkflow extends DataPipelineDef {
      override def schedule = onceAtActivation
      override def workflow = ShellCommandActivity("hello world")(WorkerGroup("foo"))
    }

    noException should be thrownBy NoConfigWorkflow.toJson
  }
}
