package com.krux.hyperion.objects.aws

import org.scalatest.WordSpec
import org.json4s.JsonDSL._

class AdpDataNodesSpec extends WordSpec {

  val tsvFormat = AdpTsvDataFormat("tsv", None, None, None)

  "AdpS3DirectoryDataNode" should {
    "converts to Json" in {
      val testObj = AdpS3DirectoryDataNode(
          "s3dn",
          None,
          Some("gzip"),
          Some(AdpRef(tsvFormat)),
          "s3://blah/blah",
          None
        )
      val objShouldBe = ("id" -> "s3dn") ~
        ("compression" -> "gzip") ~
        ("dataFormat" -> ("ref" -> "tsv")) ~
        ("directoryPath" -> "s3://blah/blah") ~
        ("type" -> "S3DataNode")
      assert(AdpJsonSerializer(testObj) === objShouldBe)
    }
  }

  "AdpS3FileDataNode" should {
    "converts to Json" in {
      val testObj = AdpS3FileDataNode(
          "s3dn",
          None,
          Some("gzip"),
          Some(AdpRef(tsvFormat)),
          "s3://blah/blah/dir/",
          None
        )
      val objShouldBe = ("id" -> "s3dn") ~
        ("compression" -> "gzip") ~
        ("dataFormat" -> ("ref" -> "tsv")) ~
        ("filePath" -> "s3://blah/blah/dir/") ~
        ("type" -> "S3DataNode")
      assert(AdpJsonSerializer(testObj) === objShouldBe)

    }
  }

  "AdpRedshiftDataNode" should {
    "converts to Json" in {
      val testObj = AdpRedshiftDataNode(
          "redshiftTable",
          None,
          None,
          AdpRef[AdpRedshiftDatabase]("myRedshift"),
          Some("public"),
          "myTable",
          None
        )
      val objShouldBe = ("id" -> "redshiftTable") ~
        ("database" -> ("ref" -> "myRedshift")) ~
        ("schemaName" -> "public") ~
        ("tableName" -> "myTable") ~
        ("type" -> "RedshiftDataNode")

      assert(AdpJsonSerializer(testObj) === objShouldBe)
    }
  }
}
