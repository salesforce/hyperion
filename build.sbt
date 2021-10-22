val hyperionVersion = "7.0.0-RC1"
val scala212Version = "2.12.12"
val scala213Version = "2.13.3"
val awsSdkVersion   = "1.11.+"
val mailVersion     = "1.6.1"
val slf4jVersion    = "1.7.+"

val jodaConvertArtifact     = "org.joda"               %  "joda-convert"              % "2.0"    % "provided"
val json4sJacksonArtifact   = "org.json4s"             %% "json4s-jackson"            % "3.6.10"
val scoptArtifact           = "com.github.scopt"       %% "scopt"                     % "4.0.0-RC2"
val jschArtifact            = "com.jcraft"             %  "jsch"                      % "0.1.54"
val configArtifact          = "com.typesafe"           %  "config"                    % "1.4.1"
val commonsIoArtifact       = "commons-io"             %  "commons-io"                % "2.6"
val commonsCompressArtifact = "org.apache.commons"     %  "commons-compress"          % "1.19"
val awsDatapipelineArtifact = "com.amazonaws"          %  "aws-java-sdk-datapipeline" % awsSdkVersion
val awsStsArtifact          = "com.amazonaws"          %  "aws-java-sdk-sts"          % awsSdkVersion
val awsS3Artifact           = "com.amazonaws"          %  "aws-java-sdk-s3"           % awsSdkVersion
val awsSqsArtifact          = "com.amazonaws"          %  "aws-java-sdk-sqs"          % awsSdkVersion
val awsSnsArtifact          = "com.amazonaws"          %  "aws-java-sdk-sns"          % awsSdkVersion
val mailArtifact            = "com.sun.mail"           %  "mailapi"                   % mailVersion
val smtpArtifact            = "com.sun.mail"           %  "smtp"                      % mailVersion
val slf4jApiArtifact        = "org.slf4j"              %  "slf4j-api"                 % slf4jVersion
val slf4jSimpleArtifact     = "org.slf4j"              %  "slf4j-simple"              % slf4jVersion
val scalatestArtifact       = "org.scalatest"          %% "scalatest"                 % "3.2.2"  % Test
val scalacheckArtifact      = "org.scalacheck"         %% "scalacheck"                % "1.14.3" % Test
val stubbornArtifact        = "com.krux"               %% "stubborn"                  % "2.0.0"
// tool to simplify cross build https://docs.scala-lang.org/overviews/core/collections-migration-213.html
val collectionCompact       = "org.scala-lang.modules" %% "scala-collection-compat"   % "2.2.0"

scalaVersion in ThisBuild := scala212Version

lazy val publishSettings = Seq(
  sonatypeProfileName := "com.krux",
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  pgpSecretRing := file("secring.asc"),
  pgpPublicRing := file("pubring.asc"),
  licenses := Seq("Apache-2.0" -> url("https://opensource.org/licenses/Apache-2.0")),
  homepage := Some(url("https://github.com/krux/hyperion")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/krux/hyperion"),
      "scm:git:git@github.com:krux/hyperion.git"
    )
  ),
  developers := List(
    Developer(id = "realstraw", name = "Kexin Xie", email = "kexin.xie@salesforce.com", url = url("https://github.com/realstraw")),
    Developer(id = "sethyates", name = "Seth Yates", email = "syates@salesforce.com", url = url("https://github.com/sethyates"))
  ),
  publishTo := {
    if (isSnapshot.value)
      Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
    else
      Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
  }
)

lazy val noPublishSettings = Seq(
  publishArtifact := false,
  publish := {},
  publishLocal := {},
  // to fix the problem of "Repository for publishing is not specified."
  publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))
)

lazy val commonSettings = Seq(
  organization := "com.krux",
  version := hyperionVersion,
  crossScalaVersions := Seq(
    scala212Version,
    scala213Version
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-Xlint",
    "-Xfatal-warnings",
    "-language:existentials"
  ),
  scalacOptions in (Compile, doc) ++= Seq(
    "-sourcepath", (baseDirectory in ThisBuild).value.toString,
    "-doc-source-url", s"https://github.com/krux/hyperion/tree/master/€{FILE_PATH}.scala"
  ),
  libraryDependencies ++= Seq(scalatestArtifact, scalacheckArtifact, collectionCompact),
  test in assembly := {} // skip test during assembly
)

// for modules that are not intended to be published as libraries but executable jars
lazy val artifactSettings = commonSettings ++ Seq(
  artifact in (Compile, assembly) := {
    val art = (artifact in (Compile, assembly)).value
    art.withClassifier(Some("assembly"))
  },
  addArtifact(artifact in (Compile, assembly), assembly)
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(publishSettings: _*).
  enablePlugins(ScalaUnidocPlugin).
  enablePlugins(SiteScaladocPlugin).
  enablePlugins(GhpagesPlugin).
  settings(
    name := "hyperion",
    siteSubdirName in ScalaUnidoc := "latest/api",
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc),
    git.remoteRepo := "git@github.com:krux/hyperion.git"
  ).
  dependsOn(
    core,
    contribActivityDefinition
  ).
  aggregate(
    core,
    examples,
    contribActivityDefinition,
    contribActivitySftp,
    contribActivityEmail,
    contribActivityNotification,
    contribActivityFile,
    contribActivityS3
  )

lazy val core = (project in file("core")).
  enablePlugins(BuildInfoPlugin).
  settings(commonSettings: _*).
  settings(publishSettings: _*).
  settings(
    name := "hyperion-core",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.krux.hyperion",
    libraryDependencies ++= Seq(
      awsDatapipelineArtifact,
      awsStsArtifact,
      json4sJacksonArtifact,
      scoptArtifact,
      configArtifact,
      slf4jApiArtifact,
      stubbornArtifact
    )
  )

lazy val examples = (project in file("examples")).
  settings(commonSettings: _*).
  settings(noPublishSettings: _*).
  settings(
    name := "hyperion-examples",
    libraryDependencies += slf4jSimpleArtifact
  ).
  dependsOn(core, contribActivityDefinition)

lazy val contribActivityDefinition = (project in file("contrib/activity/definition")).
  settings(commonSettings: _*).
  settings(publishSettings: _*).
  settings(
    name := "hyperion-activities"
  ).
  dependsOn(core)

lazy val contribActivitySftp = (project in file("contrib/activity/sftp")).
  settings(artifactSettings: _*).
  settings(publishSettings: _*).
  settings(
    name := "hyperion-sftp-activity",
    libraryDependencies ++= Seq(
      scoptArtifact,
      awsS3Artifact,
      jschArtifact,
      jodaConvertArtifact
    )
  )

lazy val contribActivityFile = (project in file("contrib/activity/file")).
  settings(artifactSettings: _*).
  settings(publishSettings: _*).
  settings(
    name := "hyperion-file-activity",
    libraryDependencies ++= Seq(
      scoptArtifact,
      commonsIoArtifact,
      commonsCompressArtifact
    )
  )

lazy val contribActivityEmail = (project in file("contrib/activity/email")).
  settings(artifactSettings: _*).
  settings(publishSettings: _*).
  settings(
    name := "hyperion-email-activity",
    libraryDependencies ++= Seq(
      scoptArtifact,
      mailArtifact,
      smtpArtifact
    )
  )

lazy val contribActivityNotification = (project in file("contrib/activity/notification")).
  settings(artifactSettings: _*).
  settings(publishSettings: _*).
  settings(
    name := "hyperion-notification-activity",
    libraryDependencies ++= Seq(
      scoptArtifact,
      json4sJacksonArtifact,
      awsSnsArtifact,
      awsSqsArtifact,
      smtpArtifact
    )
  )

lazy val contribActivityS3 = (project in file("contrib/activity/s3")).
  settings(artifactSettings: _*).
  settings(publishSettings: _*).
  settings(
    name := "hyperion-s3-activity",
    libraryDependencies ++= Seq(
      scoptArtifact,
      awsS3Artifact
    )
  )
