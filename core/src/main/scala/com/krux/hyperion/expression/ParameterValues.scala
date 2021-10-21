/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.expression

import scala.collection.mutable.{ Map => MutableMap }

class ParameterValues private (valueMap: MutableMap[String, Any]) {

  def this() = this(MutableMap.empty)

  def getValue[T](parameter: Parameter[T]): Option[T] = valueMap.get(parameter.id).map(_.asInstanceOf[T])

  def setValue[T](parameter: Parameter[T], value: T) = valueMap += (parameter.id -> value)

  def setValue[T](id: String, value: T) = valueMap += (id -> value)

}
