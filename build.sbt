name := "OpenDataSearcher"

version := "1.0"

lazy val `opendatasearcher` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc ,
  cache ,
  ws   ,
  specs2 % Test ,
  "eu.trentorise.opendata" % "jackan" % "0.4.2" ,
  "org.webjars" %% "webjars-play" % "2.5.0" ,
  "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B4-SNAPSHOT",
  "com.github.tototoshi" %% "scala-csv" % "1.3.3"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers ++= Seq("" +
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)
