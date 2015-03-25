package com.krux.hyperion.objects

import com.krux.hyperion.objects.aws.{AdpTsvDataFormat, AdpJsonSerializer}

/**
 * TSV data format
 */
case class TsvDataFormat (
    id: String,
    column: Option[Seq[String]] = None
  ) extends DataFormat {

  def withColumns(cols: Seq[String]) = this.copy(column = Some(cols))
  def serialize = AdpTsvDataFormat(id, Some(id), column, None)

}
