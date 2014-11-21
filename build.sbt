import AssemblyKeys._

buildInfoSettings

name := "aws_repl"

version := "1.0.1"

scalaVersion := "2.10.4"

mainClass in (Compile, run) := Some("org.zachary.aws_repl.Main")

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](name, version)

buildInfoPackage := "org.zachary.aws_repl"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.9.0",
  "org.scala-lang" % "scala-compiler" % "2.10.4",
  "com.github.scopt" %% "scopt" % "3.2.0",
  "org.json4s" %% "json4s-jackson" % "3.2.10"
)

assemblySettings
