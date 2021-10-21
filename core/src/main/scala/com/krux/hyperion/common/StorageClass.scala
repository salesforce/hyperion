/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.common

trait StorageClass

object StorageClass {
  object Standard extends StorageClass {
    override def toString = "STANDARD"
  }

  object ReducedRedundancy extends StorageClass {
    override def toString = "REDUCED_REDUNDANCY"
  }
}
