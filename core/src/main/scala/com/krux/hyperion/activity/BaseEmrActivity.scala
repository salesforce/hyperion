/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.activity

import com.krux.hyperion.resource.BaseEmrCluster

/**
 * The base trait for activities that run on an Amazon EMR cluster
 */
trait BaseEmrActivity[A <: BaseEmrCluster] extends PipelineActivity[A] {

  type Self <: BaseEmrActivity[A]

}
