// A Scala script to build a HMT project release.

import org.homermultitext.hmtcexbuilder._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import org.homermultitext.edmodel._
import java.io.PrintWriter
import scala.io._

val scholiaXml = "archive/scholia"
val scholiaComposites = "archive/scholia-composites"
val cexEditions = "archive/editions"


/**  Write CEX editions of scholia.
*/
def scholia = {
  println("Creating composite XML editions of scholia...")
  ScholiaComposite.composite(scholiaXml, scholiaComposites)
  val catalog = s"${scholiaComposites}/ctscatalog.cex"
  val citation = s"${scholiaComposites}/citationconfig.cex"

  val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
  val scholiaNodes = repo.corpus.nodes.filterNot(_.urn.passageComponent.endsWith("ref"))

  val scholiaRepo = TextRepository( Corpus(scholiaNodes), repo.catalog)

  new PrintWriter(s"${cexEditions}/scholia_xml.cex") { write(scholiaRepo.cex("#"));close }

  val xrefNodes = repo.corpus.nodes.filter(_.urn.passageComponent.endsWith("ref"))
}

def iliad = {
  println("\n\nNEED TO BUILD ILIAD XML AUTOMATICALLY\n\n")
}


def catAll: String = {
  val libLines = Source.fromFile("archive/library.cex").getLines.toVector

  val codices =  Source.fromFile("archive/codices/vapages.cex").getLines.toVector

  val scholia =  Source.fromFile("archive/editions/scholia_xml.cex").getLines.toVector

  val vaimg =  Source.fromFile("archive/images/vaimgs.cex").getLines.toVector
  val vbimg =  Source.fromFile("archive/images/vbimgs.cex").getLines.toVector

  val arist =  Source.fromFile("archive/commentaries-annotations/aristarchansigns.cex").getLines.toVector

  val critsigns =  Source.fromFile("archive/commentaries-annotations/va_criticalsigns.cex").getLines.toVector

  libLines.mkString("\n") + "\n" + codices.mkString("\n") + "\n" + vaimg.mkString("\n") + "\n" + vbimg.mkString("\n") + "\n" + scholia.mkString("\n") + "\n" + arist.mkString("\n") + "\n" + critsigns.mkString("\n")
}


def tidy = {
  //rm:  scholia-composites/*xml.  NB: *cex cataloing files stay here!
  //rm:  editions/*cex.
}


def release(releaseId: String) =  {
  scholia
  iliad

  val allCex = catAll
  new PrintWriter(s"releases-cex/hmt-${releaseId}.cex") { write(allCex); close}
  println(s"\nRelease ${releaseId} should be published in releases-cex/hmt-${releaseId}.cex\n")
}


println("\n\nClean out all intermediate files built from archive:")
println("\n\ttidy")
println("\nBuild a release of the HMT archive:")
println("\n\trelease(RELEASE_ID)")
