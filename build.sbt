val scalatestArtifact = "org.scalatest" %% "scalatest" % "2.2.4" % "test"
val awsDatapipelineArtifact = "com.amazonaws" % "aws-java-sdk-datapipeline" % "1.9.19"
val nscalaTimeArtifact = "com.github.nscala-time" %% "nscala-time" % "1.8.0"
val json4sJacksonArtifact = "org.json4s" %% "json4s-jackson" % "3.2.11"
val scoptArtifact = "com.github.scopt" %% "scopt" % "3.3.0"
val configArtifact = "com.typesafe" % "config" % "1.2.1"

lazy val commonSettings = Seq(
  organization := "com.krux",
  scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint", "-Xfatal-warnings"),
  version := "1.0.0",
  scalaVersion := "2.11.6",
  crossScalaVersions := Seq("2.10.4", "2.11.6"),
  libraryDependencies += scalatestArtifact,
  test in assembly := {},  // skip test during assembly
  assemblyJarName in assembly := "hyperion",
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(
    Seq(
      """#!/usr/bin/env bash
        |if [[ $# -ge 2 && $1 == "-jar" ]]; then
        |    shift
        |    ext_jar=$1":"; shift
        |else
        |    ext_jar=""
        |fi
        |exec java -cp $ext_jar$0 com.krux.hyperion.Hyperion $@""".stripMargin
  )))

)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "hyperion",
    libraryDependencies ++= Seq(
      awsDatapipelineArtifact,
      nscalaTimeArtifact,
      json4sJacksonArtifact,
      scoptArtifact,
      configArtifact
    )
  )
