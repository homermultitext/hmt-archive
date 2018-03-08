
// must be at least 2.11 to use hmt_textmodel
scalaVersion := "2.12.4"



resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith","maven")
libraryDependencies ++=   Seq(
  "edu.holycross.shot.cite" %% "xcite" % "2.1.0"
)
