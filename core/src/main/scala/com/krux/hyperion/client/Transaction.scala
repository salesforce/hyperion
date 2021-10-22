/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.client


trait Transaction[F, S] {

  def action(): S

  def validate(result: S): Boolean

  def rollback(result: S): F

  def apply(): Either[F, S] = {
    val result = action()
    if (validate(result)) Right(result) else Left(rollback(result))
  }

}
