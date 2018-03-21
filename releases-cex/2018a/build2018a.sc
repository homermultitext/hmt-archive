// build a release

import org.homermultitext.hmtcexbuilder._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import org.homermultitext.edmodel._
import java.io.PrintWriter



val scholiaXml = "archive/scholia"
val scholiaComposites = "archive/scholia-composites"


def scholia = {
  println("Creating composite XML editions of scholia...")
  ScholiaComposite.composite(scholiaXml, scholiaComposites)
  val catalog = s"${scholiaComposites}/ctscatalog.cex"
  val citation = s"${scholiaComposites}/citationconfig.cex"

  val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
  println("Converted to a CTS REPO!")
}

def iliad = {
  println("\n\nNEED TO BUILD ILIAD XML AUTOMATICALLY\n\n")
}

def release =  {
  scholia
  iliad
}


println("\n\n\nBuild an entire release of HMT 2018a:")
println("\n\trelease")
