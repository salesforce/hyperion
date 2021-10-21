package com.krux.hyperion.examples

import org.scalatest.wordspec.AnyWordSpec
import org.json4s.JsonDSL._
import org.json4s._

class ExampleSparkSpec extends AnyWordSpec {

  "ExampleSparkSpec" should {

    "produce correct pipeline JSON" in {

      val pipelineJson = ExampleSpark.toJson
      val objectsField = pipelineJson.children.head.children.sortBy(o => (o \ "name").toString)

      // have the correct number of objects
      assert(objectsField.size === 7)

      // the first object should be Default
      val defaultObj = objectsField.head
      val defaultObjShouldBe = ("id" -> "Default") ~
        ("name" -> "Default") ~
        ("scheduleType" -> "cron") ~
        ("failureAndRerunMode" -> "CASCADE") ~
        ("pipelineLogUri" -> "s3://your-bucket/datapipeline-logs/") ~
        ("role" -> "DataPipelineDefaultRole") ~
        ("resourceRole" -> "DataPipelineDefaultResourceRole") ~
        ("schedule" -> ("ref" -> "PipelineSchedule"))
      assert(defaultObj === defaultObjShouldBe)

      val pipelineSchedule = objectsField(2)
      val pipelineScheduleShouldBe =
        ("id" -> "PipelineSchedule") ~
        ("name" -> "PipelineSchedule") ~
        ("period" -> "1 days") ~
        ("startAt" -> "FIRST_ACTIVATION_DATE_TIME") ~
        ("occurrences" -> "3") ~
        ("type" -> "Schedule")
      assert(pipelineSchedule === pipelineScheduleShouldBe)

      val dataNode = objectsField(3)
      val dataNodeId = (dataNode \ "id").values.toString
      assert(dataNodeId.startsWith("S3Folder_"))
      val dataNodeShouldBe =
        ("id" -> dataNodeId) ~
        ("name" -> dataNodeId) ~
        ("directoryPath" -> "s3://some-bucket/some-path/") ~
        ("type" -> "S3DataNode")
      assert(dataNode == dataNodeShouldBe)

      val snsAlarm = objectsField(4)
      val snsAlarmId = (snsAlarm \ "id").values.toString
      assert(snsAlarmId.startsWith("SnsAlarm"))
      val snsAlarmShouldBe =
        ("id" -> snsAlarmId) ~
          ("name" -> snsAlarmId) ~
          ("subject" -> "Something happened at #{node.@scheduledStartTime}") ~
          ("message" -> "Some message #{my_InstanceCount} x #{my_InstanceType} @ #{my_InstanceBid} for #{my_S3Location}") ~
          ("topicArn" -> "arn:aws:sns:us-east-1:28619EXAMPLE:ExampleTopic") ~
          ("role" -> "DataPipelineDefaultResourceRole") ~
          ("type" -> "SnsAlarm")
      assert(snsAlarm === snsAlarmShouldBe)

      val sparkCluster = objectsField(1)
      val sparkClusterId = (sparkCluster \ "id").values.toString
      assert(sparkClusterId.startsWith("EmrCluster_"))
      val sparkClusterShouldBe =
        ("id" -> sparkClusterId) ~
        ("name" -> sparkClusterId) ~
        ("bootstrapAction" -> Seq.empty[String]) ~
        ("keyPair" -> "your-aws-key-pair") ~
        ("masterInstanceType" -> "m3.xlarge") ~
        ("coreInstanceType" -> "m3.xlarge") ~
        ("coreInstanceCount" -> "2") ~
        ("taskInstanceType" -> "#{my_InstanceType}") ~
        ("taskInstanceCount" -> "#{my_InstanceCount}") ~
        ("region" -> "us-east-1") ~
        ("resourceRole" -> "DataPipelineDefaultResourceRole") ~
        ("role" -> "DataPipelineDefaultRole") ~
        ("initTimeout" -> "5 hours") ~
        ("terminateAfter" -> "8 hours") ~
        ("releaseLabel" -> "emr-5.12.0") ~
        ("applications" -> Seq("Spark")) ~
        ("type" -> "EmrCluster")
      assert(sparkCluster === sparkClusterShouldBe)

      val filterActivity = objectsField(5)
      val filterActivityId = (filterActivity \ "id").values.toString
      assert(filterActivityId.startsWith("SparkTaskActivity_"))
      val filterActivityShouldBe =
        ("id" -> filterActivityId) ~
        ("name" -> "filterActivity") ~
        ("jarUri" -> "s3://elasticmapreduce/libs/script-runner/script-runner.jar") ~
        ("argument" -> List("s3://your-bucket/datapipeline/scripts/run-spark-step-release-label.sh",
          "s3://sample-jars/sample-jar-assembly-current.jar",
          "com.krux.hyperion.FilterJob",
          "the-target",
          "#{format(minusDays(@scheduledStartTime,3),\"yyyy-MM-dd\")}")) ~
        ("runsOn" -> ("ref" -> sparkClusterId)) ~
        ("input" -> List("ref" -> dataNodeId)) ~
        ("onFail" -> List("ref" -> snsAlarmId)) ~
        ("type" -> "HadoopActivity")
      assert(filterActivity === filterActivityShouldBe)

      val scoreActivity = objectsField(6)
      val scoreActivityId = (scoreActivity \ "id").values.toString
      assert(scoreActivityId.startsWith("EmrActivity_"))
      val scoreActivityShouldBe =
        ("id" -> scoreActivityId) ~
        ("name" -> "scoreActivity") ~
        ("step" -> List(
          "s3://elasticmapreduce/libs/script-runner/script-runner.jar,s3://your-bucket/datapipeline/scripts/run-spark-step-release-label.sh,s3://sample-jars/sample-jar-assembly-current.jar,com.krux.hyperion.ScoreJob1,the-target,#{format(minusDays(@scheduledStartTime,3),\"yyyy-MM-dd\")},denormalized",
          "s3://elasticmapreduce/libs/script-runner/script-runner.jar,s3://your-bucket/datapipeline/scripts/run-spark-step-release-label.sh,s3://sample-jars/sample-jar-assembly-current.jar,com.krux.hyperion.ScoreJob2,the-target,#{format(minusDays(@scheduledStartTime,3),\"yyyy-MM-dd\")}",
          "s3://elasticmapreduce/libs/script-runner/script-runner.jar,s3://your-bucket/datapipeline/scripts/run-spark-step-release-label.sh,s3://sample-jars/sample-jar-assembly-current.jar,com.krux.hyperion.ScoreJob3,the-target,#{format(minusDays(@scheduledStartTime,3),\"yyyy-MM-dd\")},value1\\\\,value2",
          "s3://elasticmapreduce/libs/script-runner/script-runner.jar,s3://your-bucket/datapipeline/scripts/run-spark-step-release-label.sh,s3://sample-jars/sample-jar-assembly-current.jar,com.krux.hyperion.ScoreJob4,the-target,#{format(minusDays(@scheduledStartTime,3),\"yyyy-MM-dd\")},value1\\\\,value2\\\\,#{format(minusDays(@scheduledStartTime,3),\"yyyy-MM-dd\")}"
          )) ~
        ("runsOn" -> ("ref" -> sparkClusterId)) ~
        ("dependsOn" -> List("ref" -> filterActivityId)) ~
        ("onSuccess" -> List("ref" -> snsAlarmId)) ~
        ("type" -> "EmrActivity")
      assert(scoreActivity === scoreActivityShouldBe)

    }
  }

}
