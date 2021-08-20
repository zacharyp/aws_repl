// basic setup stuff
lazy val scalaV = "2.13.6"

name := "aws_repl"
version := "2.0.0"
scalaVersion := scalaV

run / fork := true

lazy val awsVersion = "1.12.51"

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
    "org.scala-lang" % "scala-library" % scalaV,
    "org.json4s" %% "json4s-jackson" % "4.0.3"
  )
)

lazy val repl = project.in(file("."))
  .dependsOn(lib)
  .settings(
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % scalaV,
      "org.scala-lang" % "scala-library" % scalaV,
      "com.github.scopt" %% "scopt" % "3.7.1"
    ),
    Compile / run / mainClass := Some("org.zachary.aws_repl.Main")) //aggregate(lib)
