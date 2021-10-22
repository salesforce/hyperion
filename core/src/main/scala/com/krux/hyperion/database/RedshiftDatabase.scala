/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.database

import com.krux.hyperion.adt.HString
import com.krux.hyperion.aws.{ AdpRedshiftDatabase, AdpRef }
import com.krux.hyperion.common.{ PipelineObjectId, BaseFields }

/**
 * Redshift database trait, to use this please extend with an object.
 */
case class RedshiftDatabase private (
  baseFields: BaseFields,
  databaseFields: DatabaseFields,
  clusterId: HString
) extends Database {

  type Self = RedshiftDatabase

  def updateBaseFields(fields: BaseFields): Self = copy(baseFields = fields)
  def updateDatabaseFields(fields: DatabaseFields): Self = copy(databaseFields = fields)

  lazy val serialize = AdpRedshiftDatabase(
    id = id,
    name = name,
    clusterId = clusterId.serialize,
    connectionString = None,
    databaseName = databaseName.map(_.serialize),
    username = username.serialize,
    `*password` = `*password`.serialize,
    jdbcProperties = None
  )

  override def ref: AdpRef[AdpRedshiftDatabase] = AdpRef(serialize)

}

object RedshiftDatabase {

  def apply(
    username: HString,
    password: HString,
    clusterId: HString
  ) = new RedshiftDatabase(
    baseFields = BaseFields(PipelineObjectId(RdsDatabase.getClass)),
    databaseFields = DatabaseFields(username = username, `*password` = password),
    clusterId = clusterId
  )

}
