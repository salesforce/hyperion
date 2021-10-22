/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.dataformat

import com.krux.hyperion.adt.HString

case class DataFormatFields(columns: Seq[HString] = Seq.empty)
