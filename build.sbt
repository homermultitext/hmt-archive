
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")

scalaVersion := "2.12.4"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  
  "edu.holycross.shot.cite" %% "xcite" % "3.2.2",
  "edu.holycross.shot" %% "ohco2" % "10.5.4",
  "org.homermultitext" %% "hmt-textmodel" % "2.2.0",
  "org.homermultitext" %% "hmtcexbuilder" % "0.0.2"
)
