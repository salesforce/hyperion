/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.expression

/**
 * Expression that references a run time field
 */
trait ReferenceExpression extends Expression {

  def objectName: Option[String] = None

  def referenceName: String

  def isRuntime: Boolean

  def content: String =
    objectName.map(_ + ".").getOrElse("") + (if (isRuntime) "@" + referenceName else referenceName)

}
