/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.database

import com.krux.hyperion.adt.HString
import com.krux.hyperion.aws.{ AdpDatabase, AdpRef }
import com.krux.hyperion.common.{ PipelineObject, NamedPipelineObject }

/**
 * The base trait of all database objects
 */
trait Database extends NamedPipelineObject {

  type Self <: Database

  def databaseFields: DatabaseFields
  def updateDatabaseFields(fields: DatabaseFields): Self

  def username: HString = databaseFields.username
  def withUserName(username: HString) = updateDatabaseFields(
    databaseFields.copy(username = username)
  )

  def `*password` = databaseFields.`*password`
  def withPassword(password: HString) = updateDatabaseFields(
    databaseFields.copy(`*password` = password)
  )

  def databaseName = databaseFields.databaseName
  def withDatabaseName(databaseName: HString) = updateDatabaseFields(
    databaseFields.copy(databaseName = Option(databaseName))
  )

  def serialize: AdpDatabase

  def ref: AdpRef[AdpDatabase] = AdpRef(serialize)

  def objects: Iterable[PipelineObject] = None

}
