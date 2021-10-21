/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.dataformat

import com.krux.hyperion.adt.HString
import com.krux.hyperion.aws.AdpCsvDataFormat
import com.krux.hyperion.common.{ BaseFields, PipelineObjectId }

/**
 * A comma-delimited data format where the column separator is a comma and the record separator is
 * a newline character.
 */
case class CsvDataFormat private (
  baseFields: BaseFields,
  dataFormatFields: DataFormatFields,
  escapeChar: Option[HString]
) extends DataFormat {

  type Self = CsvDataFormat

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateDataFormatFields(fields: DataFormatFields) = copy(dataFormatFields = fields)

  def withEscapeChar(escapeChar: HString) = copy(escapeChar = Option(escapeChar))

  lazy val serialize = AdpCsvDataFormat(
    id = id,
    name = name,
    column = columns.map(_.serialize),
    escapeChar = escapeChar.map(_.serialize)
  )

}

object CsvDataFormat {

  def apply() = new CsvDataFormat(
    baseFields = BaseFields(PipelineObjectId(CsvDataFormat.getClass)),
    dataFormatFields = DataFormatFields(),
    escapeChar = None
  )

}
