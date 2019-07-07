name := "software-store-application"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
  
libraryDependencies += jdbc
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3"

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.11"
libraryDependencies += "com.h2database" % "h2" % "1.4.192"
