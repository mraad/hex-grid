name := "hex-grid"

version := "1.1"

organization := "com.esri"

scalaVersion := "2.10.4"

resolvers += "Local Maven Repository" at "file:///" + Path.userHome + "/.m2/repository"

publishMavenStyle := true

pomExtra := (
  <url>https://github.com/mraad/hex-grid</url>
    <licenses>
      <license>
        <name>Apache License, Verision 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:mraad/hex-grid.git</url>
      <connection>scm:git:git@github.com:mraad/hex-grid.git</connection>
    </scm>
    <developers>
      <developer>
        <id>mraad</id>
        <name>Mansour Raad</name>
        <url>https://github.com/mraad</url>
        <email>mraad@esri.com</email>
      </developer>
    </developers>)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"
