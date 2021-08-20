resolvers += "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases/"
resolvers += "Maven Central Server" at "https://repo1.maven.org/maven2"

logLevel := Level.Warn

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.0.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")

