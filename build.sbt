
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")

scalaVersion := "2.12.4"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",

  "edu.holycross.shot.cite" %% "xcite" % "3.4.0",
  "edu.holycross.shot" %% "ohco2" % "10.8.1",
  "edu.holycross.shot" %% "dse" % "3.1.0",
  "edu.holycross.shot" %% "scm" % "6.1.0",
  "org.homermultitext" %% "hmt-textmodel" % "2.2.3",
  "org.homermultitext" %% "hmtcexbuilder" % "3.1.1",
  "edu.holycross.shot" %% "citerelations" % "2.2.0"
)
