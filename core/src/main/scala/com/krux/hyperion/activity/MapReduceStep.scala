/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.adt.{HString, HS3Uri}
import com.krux.hyperion.common.Escapable

/**
 * A MapReduce step that runs on MapReduce Cluster
 */
@deprecated("Use HadoopStep instead", "5.0.0")
case class MapReduceStep private (
  jarUri: HString,
  mainClass: Option[MainClass],
  args: Seq[HString]
) {

  def withMainClass(mainClass: MainClass) = copy(mainClass = Option(mainClass))
  def withArguments(arg: HString*) = copy(args = args ++ arg)

  lazy val serialize: String = (jarUri +: mainClass.map(_.toString).toSeq ++: args)
    .map(x => Escapable.escape(x.toString, ','))
    .mkString(",")

  override def toString = serialize

}

@deprecated("Use HadoopStep instead", "5.0.0")
object MapReduceStep {

  def apply(jarUri: HS3Uri): MapReduceStep = apply(jarUri.serialize)

  def apply(jarUri: HString): MapReduceStep = MapReduceStep(
    jarUri = jarUri,
    mainClass = None,
    args = Seq.empty
  )

}

