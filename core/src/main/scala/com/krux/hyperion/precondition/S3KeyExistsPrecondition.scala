/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.precondition

import com.krux.hyperion.adt.HS3Uri
import com.krux.hyperion.aws.AdpS3KeyExistsPrecondition
import com.krux.hyperion.common.{ PipelineObjectId, BaseFields }
import com.krux.hyperion.HyperionContext

/**
 * Checks whether a key exists in an Amazon S3 data node.
 *
 * @param s3Key Amazon S3 key to check for existence.
 */
case class S3KeyExistsPrecondition private (
  baseFields: BaseFields,
  preconditionFields: PreconditionFields,
  s3Key: HS3Uri
) extends Precondition {

  type Self = S3KeyExistsPrecondition

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updatePreconditionFields(fields: PreconditionFields) = copy(preconditionFields = fields)

  lazy val serialize = AdpS3KeyExistsPrecondition(
    id = id,
    name = name,
    s3Key = s3Key.serialize,
    role = role.serialize,
    preconditionTimeout = preconditionTimeout.map(_.serialize),
    maximumRetries = maximumRetries.map(_.serialize),
    onFail = seqToOption(onFail)(_.ref),
    onLateAction = seqToOption(onLateAction)(_.ref),
    onSuccess = seqToOption(onSuccess)(_.ref)
  )

}

object S3KeyExistsPrecondition {

  def apply(s3Key: HS3Uri)(implicit hc: HyperionContext) = new S3KeyExistsPrecondition(
    baseFields = BaseFields(PipelineObjectId(S3KeyExistsPrecondition.getClass)),
    preconditionFields = Precondition.defaultPreconditionFields,
    s3Key = s3Key
  )

}
