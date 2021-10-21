/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.adt.HS3Uri
import com.krux.hyperion.common.{ BaseFields, PipelineObjectId }
import com.krux.hyperion.expression.RunnableObject
import com.krux.hyperion.resource.{ Ec2Resource, Resource }

/**
 * Activity to recursively delete files in an S3 path.
 */
case class DeleteS3PathActivity private (
  baseFields: BaseFields,
  activityFields: ActivityFields[Ec2Resource],
  shellCommandActivityFields: ShellCommandActivityFields,
  s3Path: HS3Uri
) extends BaseShellCommandActivity {

  type Self = DeleteS3PathActivity

  def updateBaseFields(fields: BaseFields) = copy(baseFields = fields)
  def updateActivityFields(fields: ActivityFields[Ec2Resource]) = copy(activityFields = fields)
  def updateShellCommandActivityFields(fields: ShellCommandActivityFields) = copy(shellCommandActivityFields = fields)

  def ifExists(existsS3Path: HS3Uri) = updateShellCommandActivityFields(
    shellCommandActivityFields.copy(script =
        s"""count=`aws s3 ls $existsS3Path | wc -l`;
            if [[ $$count -gt 0 ]];
            then
              aws s3 rm --recursive $s3Path;
            fi
        """
      )
    )
}

object DeleteS3PathActivity extends RunnableObject {

  def apply(s3Path: HS3Uri)(runsOn: Resource[Ec2Resource]): DeleteS3PathActivity =
    new DeleteS3PathActivity(
      baseFields = BaseFields(PipelineObjectId(DeleteS3PathActivity.getClass)),
      activityFields = ActivityFields(runsOn),
      shellCommandActivityFields = ShellCommandActivityFields(s"aws s3 rm --recursive $s3Path"),
      s3Path = s3Path
    )

}
