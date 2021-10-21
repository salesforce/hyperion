/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.common

/**
 * Memory is a memory specification including an amount and a unit.
 *
 * @param n The amount of memory
 * @param unit The unit.
 */
case class Memory(n: Long, unit: String) {
  override val toString: String = s"$n$unit"
}
