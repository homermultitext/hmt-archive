name := "HMT archive library"

crossScalaVersions  :=  Seq("2.12.4") //Seq("2.11.8", "2.12.4")
scalaVersion := (crossScalaVersions ).value.last

name := "hmtcexbuilder"
organization := "org.homermultitext"
version := "3.5.0"
licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",

  "edu.holycross.shot.cite" %% "xcite" % "4.2.0",
  "edu.holycross.shot" %% "ohco2" % "10.18.2",

  "edu.holycross.shot" %% "cex" % "6.4.0",
  "edu.holycross.shot" %% "dse" % "7.0.0",
  "edu.holycross.shot" %% "scm" % "7.2.0",
  "edu.holycross.shot" %% "citerelations" % "2.6.0",

  "edu.holycross.shot" %% "midvalidator" % "12.2.2",

  "org.homermultitext" %% "hmt-textmodel" % "6.1.4",

  "org.wvlet.airframe" %% "airframe-log" % "19.8.10"
)

tutTargetDirectory := file("docs")
tutSourceDirectory := file("src/main/tut")


enablePlugins(TutPlugin)
