
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")

scalaVersion := "2.12.4"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",

  "edu.holycross.shot.cite" %% "xcite" % "4.2.0",
  "edu.holycross.shot" %% "ohco2" % "10.18.1",
  "edu.holycross.shot" %% "dse" % "6.0.2",
  "edu.holycross.shot" %% "scm" % "7.2.0",
  "edu.holycross.shot" %% "citerelations" % "2.6.0",
  "edu.holycross.shot.mid" %% "validator" % "10.0.0",

  "org.homermultitext" %% "hmt-textmodel" % "5.2.1",
  "org.homermultitext" %% "hmtcexbuilder" % "3.5.0"

)
