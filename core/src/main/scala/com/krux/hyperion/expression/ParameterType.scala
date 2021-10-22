/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.expression

/**
 * AWS Data Pipeline parameter supports the following types, custom types in most cases should be
 * of type StringType
 */
object ParameterType extends Enumeration {

  type ParameterType = Value

  val StringType = Value("String")
  val IntegerType = Value("Integer")
  val DoubleType = Value("Double")
  val S3KeyType = Value("AWS::S3::ObjectKey")
}
