/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.aws

trait AdpObject {
  def id: String
}

abstract class AdpDataPipelineAbstractObject extends AdpObject {
  def name: Option[String]
}

/**
 * Each data pipeline can have a default object
 */
trait AdpDataPipelineDefaultObject extends AdpDataPipelineAbstractObject {
  val id: String = "Default"
  val name: Option[String] = Option("Default")
  def fields: Map[String, Either[String, AdpRef[AdpDataPipelineAbstractObject]]]
}

/**
 * The base class of all AWS Data Pipeline objects.
 *
 * ref: http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-pipeline-objects.html
 */
trait AdpDataPipelineObject extends AdpDataPipelineAbstractObject {

  /**
   * The ID of the object, IDs must be unique within a pipeline definition
   */
  def id: String

  /**
   * The optional, user-defined label of the object. If you do not provide a name for an object in
   * a pipeline definition, AWS Data Pipeline automatically duplicates the value of id.
   */
  def name: Option[String]

  /**
   * The type of object. Use one of the predefined AWS Data Pipeline object types.
   */
  def `type`: String

}
