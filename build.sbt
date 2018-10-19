// basic setup stuff
lazy val scalaV = "2.11.8"

name := "aws_repl"
version := "1.1.0"
scalaVersion := scalaV

lazy val awsVersion = "1.11.430"

lazy val lib = project.in(file("lib")).settings(
  scalaVersion := scalaV,
  libraryDependencies ++= Seq(
    "com.amazonaws" % "aws-java-sdk-core" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-autoscaling" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-cloudwatch" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-ec2" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-iam" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-rds" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-route53" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-s3" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-sns" % awsVersion,
    "com.amazonaws" % "aws-java-sdk-sqs" % awsVersion,
    "org.scala-lang" % "scala-compiler" % scalaV,
    "org.json4s" %% "json4s-jackson" % "3.2.10"
  )
)

lazy val repl = project.in(file("."))
  .dependsOn(lib)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    scalaVersion := scalaV,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "org.zachary.aws_repl",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % scalaV,
      "com.github.scopt" %% "scopt" % "3.2.0"
    ),
    mainClass in (Compile, run) := Some("org.zachary.aws_repl.Main")) //aggregate(lib)
