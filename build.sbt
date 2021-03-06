import NativePackagerHelper._

name := "Amazon-Fine-Foods"

version := "2.4.11"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.16"
  ,"net.sf.opencsv" % "opencsv" % "2.1"
  ,"org.scalatest" % "scalatest_2.11" % "2.2.6"
  ,"com.typesafe.akka" %% "akka-testkit" % "2.4.16"
)

enablePlugins(JavaServerAppPackaging)

mainClass in Compile := Some("com.taintech.aff.main.AmazonFineFoodApp")

mappings in Universal ++= {
  // optional example illustrating how to copy additional directory
  directory("scripts") ++
  // copy configuration files to config directory
  contentOf("src/main/resources").toMap.mapValues("config/" + _)
}

// add 'config' directory first in the classpath of the start script,
// an alternative is to set the config file locations via CLI parameters
// when starting the application
scriptClasspath := Seq("../config/") ++ scriptClasspath.value

licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0")))
