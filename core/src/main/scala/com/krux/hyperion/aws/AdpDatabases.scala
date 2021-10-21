/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.hyperion.aws

/**
 * AWS Data Pipeline database objects.
 *
 * Ref: http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-object-databases.html
 */
trait AdpDatabase extends AdpDataPipelineObject {

  /** The name of the logical database. */
  def databaseName: Option[String]

  /** The properties of the JDBC connections for this database. */
  def jdbcProperties: Option[Seq[String]]

  /** The user name to connect to the database. */
  def username: String

  /** The password to connect to the database. */
  def `*password`: String
}

/**
 * Defines an Amazon Redshift database.
 *
 * @param clusterId The identifier provided by the user when the Amazon Redshift cluster was
 *   created. For example, if the endpoint for your Amazon Redshift cluster is
 *   mydb.example.us-east-1.redshift.amazonaws.com, the correct clusterId value is mydb. In the
 *   Amazon Redshift console, this value is "Cluster Name".
 * @param connectionString The JDBC endpoint for connecting to an Amazon Redshift instance owned by
 *   an account different than the pipeline.
 */
case class AdpRedshiftDatabase(
  id: String,
  name: Option[String],
  clusterId: String,
  connectionString: Option[String],
  databaseName: Option[String],
  username: String,
  `*password`: String,
  jdbcProperties: Option[Seq[String]]
) extends AdpDatabase {

  val `type` = "RedshiftDatabase"

}

/**
 * Defines a JDBC database.
 *
 * @param connectionString The JDBC connection string to access the database.
 * @param jdbcDriverClass The driver class to load before establishing the JDBC connection.
 */
case class AdpJdbcDatabase(
  id: String,
  name: Option[String],
  connectionString: String,
  databaseName: Option[String],
  username: String,
  `*password`: String,
  jdbcDriverJarUri: Option[String],
  jdbcDriverClass: String,
  jdbcProperties: Option[Seq[String]]
) extends AdpDatabase {

  val `type` = "JdbcDatabase"

}

/**
 * Defines an Amazon RDS database.
 */
case class AdpRdsDatabase(
  id: String,
  name: Option[String],
  databaseName: Option[String],
  jdbcProperties: Option[Seq[String]],
  username: String,
  `*password`: String,
  rdsInstanceId: String,
  region: Option[String],
  jdbcDriverJarUri: Option[String]
) extends AdpDatabase {

  val `type` = "RdsDatabase"

}
