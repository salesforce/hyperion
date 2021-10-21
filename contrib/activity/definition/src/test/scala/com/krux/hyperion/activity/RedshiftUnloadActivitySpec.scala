package com.krux.hyperion.activity

import com.typesafe.config.ConfigFactory
import org.scalatest.wordspec.AnyWordSpec

import com.krux.hyperion.common.S3Uri._
import com.krux.hyperion.database.RedshiftDatabase
import com.krux.hyperion.expression.{Parameter, ParameterValues}
import com.krux.hyperion.HyperionContext
import com.krux.hyperion.resource.Ec2Resource


class RedshiftUnloadActivitySpec extends AnyWordSpec {

  "RedshiftUnloadActivity" should {

    implicit val hc: HyperionContext = new HyperionContext(ConfigFactory.load("example"))
    implicit val pv: ParameterValues = new ParameterValues()

    val ec2 = Ec2Resource()

    val awsAccessKeyId = Parameter("AwsAccessKeyId", "someId").encrypted
    val awsAccessKeySecret = Parameter.encrypted("AwsAccessKeySecret", "someSecret")

    val mockRedshift = RedshiftDatabase("mockuser", "mockpass", "mock-redshift")
      .named("_MockRedshift")
      .withDatabaseName("mock_db")

    "Produce the correct unload script" in {
      val testingQuery = """
        |select * from t where
        |id = 'myid'
        |and {tim'e} = #{format(@actualRunTime, 'yyyy-MM-dd')}
        |and some{OtherWeird'Forma}t = #{"{ } a'dfa {" + ' { ex"aef { }'}
        |and name = 'abcdefg'
        |limit 10""".stripMargin

      val escapedUnloadScript = """
        |UNLOAD ('
        |select * from t where
        |id = \\'myid\\'
        |and {tim\\'e} = #{format(@actualRunTime, 'yyyy-MM-dd')}
        |and some{OtherWeird\\'Forma}t = #{"{ } a'dfa {" + ' { ex"aef { }'}
        |and name = \\'abcdefg\\'
        |limit 10')
        |TO 's3://not-important/'
        |WITH CREDENTIALS AS
        |'aws_access_key_id=#{*my_AwsAccessKeyId};aws_secret_access_key=#{*my_AwsAccessKeySecret}'
      """.stripMargin

      val act = RedshiftUnloadActivity(
          mockRedshift, testingQuery, s3 / "not-important/", awsAccessKeyId, awsAccessKeySecret
        )(ec2)

      assert(act.unloadScript.trim === escapedUnloadScript.trim)

    }
  }
}
