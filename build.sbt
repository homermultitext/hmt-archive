
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")

scalaVersion := "2.12.4"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",

  "edu.holycross.shot.cite" %% "xcite" % "4.1.0",
  "edu.holycross.shot" %% "ohco2" % "10.13.0",
  "edu.holycross.shot" %% "dse" % "4.6.0",
  "edu.holycross.shot" %% "scm" % "6.2.3",
  "org.homermultitext" %% "hmt-textmodel" % "6.0.1",
  "org.homermultitext" %% "hmtcexbuilder" % "3.4.0",
  "edu.holycross.shot" %% "citerelations" % "2.4.1"
)
