/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.common

/**
 * The base fields of all pipeline objects.
 */
case class BaseFields(
  id: PipelineObjectId,
  // note: use String instead of HString because name field does not accept expressions
  name: Option[String] = None
)
