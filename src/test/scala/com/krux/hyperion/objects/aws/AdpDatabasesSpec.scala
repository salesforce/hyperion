package com.krux.hyperion.objects.aws

import org.scalatest.WordSpec
import org.json4s.JsonDSL._

class AdpDatabasesSpec extends WordSpec {

  "AdpRedshiftDatabase" should {
    "converts to Json" in {
      val testObj = AdpRedshiftDatabase(
        "red",
        None,
        "datascience-redshift-id",
        None,
        Some("dbname"),
        None,
        "supersecretpassword",
        "notsosupersecretuser"
      )
      val objShoudBe = ("id" -> "red") ~
        ("clusterId" -> "datascience-redshift-id") ~
        ("databaseName" -> "dbname") ~
        ("*password" -> "supersecretpassword") ~
        ("username" -> "notsosupersecretuser") ~
        ("type" -> "RedshiftDatabase")

      assert(AdpJsonSerializer(testObj) === objShoudBe)
    }

  }

}
