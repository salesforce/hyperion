/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.common

import scala.language.implicitConversions

import com.krux.hyperion.aws.{ AdpDataPipelineAbstractObject, AdpRef }

/**
 * The base trait of krux data pipeline objects.
 */
trait PipelineObject extends Ordered[PipelineObject] {

  type Self <: PipelineObject

  implicit def uniquePipelineId2String(id: PipelineObjectId): String = id.toString
  implicit def seq2Option[A](anySeq: Seq[A]): Option[Seq[A]] = seqToOption(anySeq)(x => x)

  def id: PipelineObjectId

  def objects: Iterable[PipelineObject]

  def serialize: AdpDataPipelineAbstractObject
  def ref: AdpRef[AdpDataPipelineAbstractObject]

  def seqToOption[A, B](anySeq: Seq[A])(transform: A => B) = {
    anySeq match {
      case Seq() => None
      case other => Option(anySeq.map(transform))
    }
  }

  def compare(that: PipelineObject): Int = id.compare(that.id)

}
