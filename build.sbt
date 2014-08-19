name := "aws_repl"

version := "1.0"

scalaVersion := "2.10.4"

mainClass in (Compile, run) := Some("org.zachary.aws_repl.Main")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.8.9.1",
  "org.scala-lang" % "scala-compiler" % "2.10.4"
)