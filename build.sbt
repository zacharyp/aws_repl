// basic setup stuff
lazy val scalaV = "2.11.6"

name := "aws_repl"
version := "1.1.0"
scalaVersion := scalaV

lazy val interpreter = project.in(file("interpreter"))
  .dependsOn(lib)
  .settings(
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaV,
      "org.scala-lang" % "scala-compiler" % scalaV,
      "jline" % "jline" % "2.11"
    )
)

lazy val lib = project.in(file("lib")).settings(
  scalaVersion := scalaV,
  libraryDependencies ++= Seq(
    "com.amazonaws" % "aws-java-sdk" % "1.9.17",
    "org.scala-lang" % "scala-compiler" % scalaV,
    "org.json4s" %% "json4s-jackson" % "3.2.10"
  )
)

lazy val repl = project.in(file("."))
  .dependsOn(lib)
  .dependsOn(interpreter)
  .settings(buildInfoSettings:_*)
  .settings(
    scalaVersion := scalaV,
    sourceGenerators in Compile <+= buildInfo,
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "org.zachary.aws_repl",
    libraryDependencies += "com.github.scopt" %% "scopt" % "3.2.0",
    mainClass in (Compile, run) := Some("org.zachary.aws_repl.Main")) //aggregate(lib)
