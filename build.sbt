lazy val scala212 = "2.12.10"
lazy val supportedScalaVersions = List(scala212)
ThisBuild / scalaVersion := scala212
ThisBuild / turbo := true

lazy val root = project.in(file(".")).
    aggregate(crossed.js, crossed.jvm).
    settings(
      crossScalaVersions := Nil,
      publish / skip := true
    )


lazy val crossed = crossProject(JSPlatform, JVMPlatform).in(file(".")).
    settings(
      name := "hmtcexbuilder",
      organization := "org.homermultitext",
      version := "3.6.0"
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),

      resolvers += Resolver.jcenterRepo,
      resolvers += Resolver.bintrayRepo("neelsmith", "maven"),
      libraryDependencies ++= Seq(
        "org.scalatest" %%% "scalatest" % "3.1.2" % "test",
        "org.wvlet.airframe" %%% "airframe-log" % "20.5.2",

        "edu.holycross.shot.cite" %% "xcite" % "4.3.0",
        "edu.holycross.shot" %% "ohco2" % "10.20.3",

        "edu.holycross.shot" %% "cex" % "6.5.0",
        "edu.holycross.shot" %% "dse" % "7.1.3",
        "edu.holycross.shot" %% "scm" % "7.3.0",
        "edu.holycross.shot" %% "citerelations" % "2.7.0",

        "edu.holycross.shot" %% "midvalidator" % "13.3.0",

        "org.homermultitext" %% "hmt-textmodel" % "8.0.0",

      )
    ).jvmSettings(
      libraryDependencies ++= Seq(
        "org.scala-js" %% "scalajs-stubs" % "1.0.0" % "provided",
        "com.github.pathikrit" %% "better-files" % "3.5.0",
        "edu.holycross.shot" %% "xmlutils" % "2.0.0",
      )
    ).
    jsSettings(
      // JS-specific settings:
      scalaJSUseMainModuleInitializer := true,
    )

    lazy val docs = project       // new documentation project
      .in(file("docs-build")) // important: it must not be docs/
      .dependsOn(crossed.jvm)
      .enablePlugins(MdocPlugin)
      .settings(
        mdocIn := file("guide"),
        mdocOut := file("docs"),
        mdocExtraArguments := Seq("--no-link-hygiene"),
        mdocVariables := Map(
          "VERSION" -> "3.6.0"
        )
      )
