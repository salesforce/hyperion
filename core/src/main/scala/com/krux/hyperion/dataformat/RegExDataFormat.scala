/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.dataformat

import com.krux.hyperion.adt.HString
import com.krux.hyperion.aws.AdpRegExDataFormat
import com.krux.hyperion.common.{ BaseFields, PipelineObjectId }

/**
 * A custom data format defined by a regular expression.
 */
case class RegExDataFormat private (
  baseFields: BaseFields,
  dataFormatFields: DataFormatFields,
  inputRegEx: HString,
  outputFormat: HString
) extends DataFormat {

  type Self = RegExDataFormat

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateDataFormatFields(fields: DataFormatFields) = copy(dataFormatFields = fields)

  lazy val serialize = AdpRegExDataFormat(
    id = id,
    name = name,
    column = columns.map(_.serialize),
    inputRegEx = inputRegEx.serialize,
    outputFormat = outputFormat.serialize
  )

}

object RegExDataFormat {

  def apply(inputRegEx: HString, outputFormat: HString) = new RegExDataFormat(
    baseFields = BaseFields(PipelineObjectId(CsvDataFormat.getClass)),
    dataFormatFields = DataFormatFields(),
    inputRegEx = inputRegEx,
    outputFormat = outputFormat
  )

}
