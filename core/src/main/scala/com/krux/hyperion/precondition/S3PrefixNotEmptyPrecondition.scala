/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.precondition

import com.krux.hyperion.adt.HS3Uri
import com.krux.hyperion.aws.AdpS3PrefixNotEmptyPrecondition
import com.krux.hyperion.common.{ PipelineObjectId, BaseFields }
import com.krux.hyperion.HyperionContext

/**
 * A precondition to check that the Amazon S3 objects with the given prefix (represented as a URI) are present.
 *
 * @param s3Prefix  The Amazon S3 prefix to check for existence of objects.
 */
case class S3PrefixNotEmptyPrecondition private (
  baseFields: BaseFields,
  preconditionFields: PreconditionFields,
  s3Prefix: HS3Uri
) extends Precondition {

  type Self = S3PrefixNotEmptyPrecondition

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updatePreconditionFields(fields: PreconditionFields) = copy(preconditionFields = fields)

  lazy val serialize = AdpS3PrefixNotEmptyPrecondition(
    id = id,
    name = name,
    s3Prefix = s3Prefix.serialize,
    role = role.serialize,
    preconditionTimeout = preconditionTimeout.map(_.serialize),
    maximumRetries = maximumRetries.map(_.serialize),
    onFail = seqToOption(onFail)(_.ref),
    onLateAction = seqToOption(onLateAction)(_.ref),
    onSuccess = seqToOption(onSuccess)(_.ref)
  )

}

object S3PrefixNotEmptyPrecondition {

  def apply(s3Prefix: HS3Uri)(implicit hc: HyperionContext) = new S3PrefixNotEmptyPrecondition(
    baseFields = BaseFields(PipelineObjectId(S3PrefixNotEmptyPrecondition.getClass)),
    preconditionFields = Precondition.defaultPreconditionFields,
    s3Prefix = s3Prefix
  )

}
