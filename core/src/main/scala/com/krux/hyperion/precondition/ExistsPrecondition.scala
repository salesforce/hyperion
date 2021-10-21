/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.precondition

import com.krux.hyperion.aws.AdpExistsPrecondition
import com.krux.hyperion.common.{ PipelineObjectId, BaseFields }
import com.krux.hyperion.HyperionContext

/**
 * Checks whether a data node object exists.
 */
case class ExistsPrecondition private (
  baseFields: BaseFields,
  preconditionFields: PreconditionFields
) extends Precondition {

  type Self = ExistsPrecondition

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updatePreconditionFields(fields: PreconditionFields) = copy(preconditionFields = fields)

  lazy val serialize = AdpExistsPrecondition(
    id = id,
    name = name,
    role = role.serialize,
    preconditionTimeout = preconditionTimeout.map(_.serialize),
    maximumRetries = maximumRetries.map(_.serialize),
    onFail = seqToOption(onFail)(_.ref),
    onLateAction = seqToOption(onLateAction)(_.ref),
    onSuccess = seqToOption(onSuccess)(_.ref)
  )

}

object ExistsPrecondition {

  def apply()(implicit hc: HyperionContext) = new ExistsPrecondition(
    baseFields = BaseFields(PipelineObjectId(ExistsPrecondition.getClass)),
    preconditionFields = Precondition.defaultPreconditionFields
  )

}
