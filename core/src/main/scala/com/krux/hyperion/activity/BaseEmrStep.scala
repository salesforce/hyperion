/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.adt.HString
import com.krux.hyperion.common.Escapable


trait BaseEmrStep {

  def jarUri: HString

  def mainClass: Option[MainClass]

  def args: Seq[HString]

  lazy val serialize: String = (jarUri +: mainClass.map(_.toString).toSeq ++: args)
    .map(x => Escapable.escape(x.toString, ','))
    .mkString(",")

  override def toString = serialize

}
