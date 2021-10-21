/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import scala.language.implicitConversions

import com.krux.hyperion.adt.HString

class MainClass private (name: String) {
  override def toString = name
  val fullName: String = name
  val simpleName: String = name.split('.').last
}

object MainClass {
  implicit def hStringToMainClass(s: HString): MainClass = MainClass(s.toString)
  implicit def stringToMainClass(s: String): MainClass = MainClass(s)
  implicit def classToMainClass(c: Class[_]): MainClass = MainClass(c)
  implicit def anyToMainClass(a: Any): MainClass = MainClass(a)

  def apply(mainClass: Any): MainClass = mainClass match {
    case s: String => new MainClass(s.stripSuffix("$"))
    case c: Class[_] => apply(c.getCanonicalName)
    case mc => apply(mc.getClass)
  }
}
