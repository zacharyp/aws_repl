// basic setup stuff
name := "aws_repl"
version := "1.0.1"
scalaVersion := "2.10.4"

lazy val lib = Project("lib", file("lib")).settings(
  libraryDependencies ++= Seq(
    "com.amazonaws" % "aws-java-sdk" % "1.9.17",
    "org.scala-lang" % "scala-compiler" % "2.10.4",
    "org.json4s" %% "json4s-jackson" % "3.2.10"
  )
)

lazy val repl = Project("repl", file("."))
  .dependsOn(lib)
  .settings(buildInfoSettings:_*)
  .settings(
    sourceGenerators in Compile <+= buildInfo,
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "org.zachary.aws_repl",
    libraryDependencies += "com.github.scopt" %% "scopt" % "3.2.0",
    mainClass in (Compile, run) := Some("org.zachary.aws_repl.Main"))
