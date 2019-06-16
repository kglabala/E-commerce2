name := "root"

version := "2.6.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"
libraryDependencies += "org.xerial"        %  "sqlite-jdbc" % "3.21.0"
//libraryDependencies += "slick.driver.SQLiteDriver" %%

//<<<<<<< HEAD
//libraryDependencies += "com.h2database" % "h2" % "1.4.196"
//=======
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
//>>>>>>> 63bb6af751ba5c9f5f8f2169fc4029269f2a53de

libraryDependencies += specs2 % Test

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
