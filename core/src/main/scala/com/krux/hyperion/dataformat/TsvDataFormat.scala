/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.dataformat

import com.krux.hyperion.adt.HString
import com.krux.hyperion.aws.AdpTsvDataFormat
import com.krux.hyperion.common.{ BaseFields, PipelineObjectId }

/**
 * A tab-delimited data format where the record separator is a newline character.
 */
case class TsvDataFormat private (
  baseFields: BaseFields,
  dataFormatFields: DataFormatFields,
  escapeChar: Option[HString]
) extends DataFormat {

  type Self = TsvDataFormat

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateDataFormatFields(fields: DataFormatFields) = copy(dataFormatFields = fields)

  def withEscapeChar(escapeChar: HString) = copy(escapeChar = Option(escapeChar))

  lazy val serialize = AdpTsvDataFormat(
    id = id,
    name = name,
    column = columns.map(_.serialize),
    escapeChar = escapeChar.map(_.serialize)
  )

}

object TsvDataFormat {

  def apply() = new TsvDataFormat(
    baseFields = BaseFields(PipelineObjectId(TsvDataFormat.getClass)),
    dataFormatFields = DataFormatFields(),
    escapeChar = None
  )

}
