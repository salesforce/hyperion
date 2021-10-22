/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.dataformat

import com.krux.hyperion.aws.AdpDynamoDBDataFormat
import com.krux.hyperion.common.{ BaseFields, PipelineObjectId }

/**
 * Applies a schema to a DynamoDB table to make it accessible by a Hive query. DynamoDBDataFormat
 * is used with a HiveActivity object and a DynamoDBDataNode input and output. DynamoDBDataFormat
 * requires that you specify all columns in your Hive query.
 */
case class DynamoDBDataFormat private (
  baseFields: BaseFields,
  dataFormatFields: DataFormatFields
) extends DataFormat {

  type Self = DynamoDBDataFormat

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateDataFormatFields(fields: DataFormatFields) = copy(dataFormatFields = fields)

  lazy val serialize = AdpDynamoDBDataFormat(
    id = id,
    name = name,
    column = columns.map(_.serialize)
  )

}

object DynamoDBDataFormat {

  def apply() = new DynamoDBDataFormat(
    baseFields = BaseFields(PipelineObjectId(DynamoDBDataFormat.getClass)),
    dataFormatFields = DataFormatFields()
  )

}
