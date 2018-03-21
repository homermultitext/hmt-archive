
// must be at least 2.11 to use hmt_textmodel
scalaVersion := "2.12.4"



resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith","maven")
libraryDependencies ++=   Seq(
  "edu.holycross.shot.cite" %% "xcite" % "3.2.2",
  "edu.holycross.shot" %% "ohco2" % "10.5.3",
  "org.homermultitext" %% "hmt-textmodel" % "2.2.0"
)
