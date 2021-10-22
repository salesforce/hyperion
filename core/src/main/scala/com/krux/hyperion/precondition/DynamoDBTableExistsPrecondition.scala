/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.precondition

import com.krux.hyperion.adt.HString
import com.krux.hyperion.aws.AdpDynamoDBTableExistsPrecondition
import com.krux.hyperion.common.{ BaseFields, PipelineObjectId }
import com.krux.hyperion.HyperionContext

/**
 * A precondition to check that the DynamoDB table exists.
 *
 * @param tableName The DynamoDB table to check.
 */
case class DynamoDBTableExistsPrecondition private (
  baseFields: BaseFields,
  preconditionFields: PreconditionFields,
  tableName: HString
) extends Precondition {

  type Self = DynamoDBTableExistsPrecondition

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updatePreconditionFields(fields: PreconditionFields) = copy(preconditionFields = fields)

  lazy val serialize = AdpDynamoDBTableExistsPrecondition(
    id = id,
    name = name,
    tableName = tableName.serialize,
    role = role.serialize,
    preconditionTimeout = preconditionTimeout.map(_.serialize),
    maximumRetries = maximumRetries.map(_.serialize),
    onFail = seqToOption(onFail)(_.ref),
    onLateAction = seqToOption(onLateAction)(_.ref),
    onSuccess = seqToOption(onSuccess)(_.ref)
  )

}

object DynamoDBTableExistsPrecondition {

  def apply(tableName: HString)(implicit hc: HyperionContext) = new DynamoDBTableExistsPrecondition(
    baseFields = BaseFields(PipelineObjectId(DynamoDBTableExistsPrecondition.getClass)),
    preconditionFields = Precondition.defaultPreconditionFields,
    tableName = tableName
  )

}
