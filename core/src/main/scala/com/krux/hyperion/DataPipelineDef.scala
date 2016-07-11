package com.krux.hyperion

import scala.language.implicitConversions

import com.amazonaws.services.datapipeline.model.{ ParameterObject => AwsParameterObject,
  PipelineObject => AwsPipelineObject }

import com.amazonaws.services.datapipeline.model.{ParameterObject => AwsParameterObject, PipelineObject => AwsPipelineObject}
import com.krux.hyperion.activity.MainClass
import com.krux.hyperion.aws.{AdpJsonSerializer, AdpParameterSerializer, AdpPipelineSerializer}
import com.krux.hyperion.common._
import com.krux.hyperion.expression.{Parameter, ParameterValues}
import com.krux.hyperion.workflow.WorkflowExpressionImplicits
import org.json4s.JsonDSL._
import org.json4s.{JArray, JValue}

import com.krux.hyperion.aws.{AdpJsonSerializer, AdpParameterSerializer, AdpPipelineSerializer}
import com.krux.hyperion.workflow.WorkflowExpression


/**
 * Base trait of all data pipeline definitions. All data pipelines needs to implement this trait
 */
trait DataPipelineDef extends DataPipelineDefGroup {

  /**
   * Workflow to be defined
   */
  def workflow: WorkflowExpression

  final def workflows = Map(EmptyKey -> workflow)

}

object DataPipelineDef {

  implicit def dataPipelineDef2Json(pd: DataPipelineDef): JValue =
    ("objects" -> JArray(
      (pd.defaultObject +: pd.schedule +: pd.workflow.toPipelineObjects.toList)
        .map(_.serialize).sortBy(_.id).map(o => AdpJsonSerializer(o)))) ~
    ("parameters" -> JArray(
      pd.parameters.flatMap(_.serialize).map(o => AdpJsonSerializer(o)).toList))

  implicit def dataPipelineDef2Aws(pd: DataPipelineDef): Seq[AwsPipelineObject] =
    (pd.defaultObject +: pd.schedule +: pd.workflow.toPipelineObjects.toList)
      .map(o => AdpPipelineSerializer(o.serialize))

}
