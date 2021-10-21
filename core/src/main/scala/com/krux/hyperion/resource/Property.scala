/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.resource

import com.krux.hyperion.adt.HString
import com.krux.hyperion.aws.{ AdpProperty, AdpRef }
import com.krux.hyperion.common.{ PipelineObject, PipelineObjectId }

case class Property private (
  id: PipelineObjectId,
  key: Option[HString],
  value: Option[HString]
) extends PipelineObject {

  def objects: Iterable[PipelineObject] = None

  lazy val serialize = AdpProperty(
    id = id,
    name = id.toOption,
    key = key.map(_.serialize),
    value = value.map(_.serialize)
  )

  def ref: AdpRef[AdpProperty] = AdpRef(serialize)
}

object Property {

  def apply(key: HString, value: HString): Property = Property(
    id = PipelineObjectId(Property.getClass),
    key = Option(key),
    value = Option(value)
  )

}
