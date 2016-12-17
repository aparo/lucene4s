name := "lucene4s"
organization := "com.outr"
version := "1.4.0"
scalaVersion := "2.12.1"
crossScalaVersions := List("2.12.1", "2.11.8")
sbtVersion := "0.13.13"
parallelExecution in Test := false
fork := true
scalacOptions ++= Seq("-unchecked", "-deprecation")
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "com.outr" %% "scribe-slf4j" % "1.2.6"
libraryDependencies += "org.apache.lucene" % "lucene-core" % "6.3.0"
libraryDependencies += "org.apache.lucene" % "lucene-analyzers-common" % "6.3.0"
libraryDependencies += "org.apache.lucene" % "lucene-queryparser" % "6.3.0"
libraryDependencies += "org.apache.lucene" % "lucene-facet" % "6.3.0"
libraryDependencies += "org.apache.lucene" % "lucene-highlighter" % "6.3.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.14"
libraryDependencies += "org.typelevel" %% "squants"  % "1.0.0-SNAPSHOT"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oF")