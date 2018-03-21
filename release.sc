// A Scala script to build a HMT project release.

import org.homermultitext.hmtcexbuilder._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import org.homermultitext.edmodel._
import java.io.PrintWriter
import scala.io._

val scholiaXml = "archive/scholia"
val scholiaComposites = "archive/scholia-composites"
val scholiaCex = "archive/scholia-cex"


/**  Write CEX editions of scholia.
*/
def scholia = {
  println("Creating composite XML editions of scholia...")
  ScholiaComposite.composite(scholiaXml, scholiaComposites)
  val catalog = s"${scholiaComposites}/ctscatalog.cex"
  val citation = s"${scholiaComposites}/citationconfig.cex"

  val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
  val scholiaNodes = repo.corpus.nodes.filterNot(_.urn.passageComponent.endsWith("ref"))
  new PrintWriter(s"${scholiaCex}/scholia_xml.cex") { write(repo.cex("#"));close }

  val xrefNodes = repo.corpus.nodes.filter(_.urn.passageComponent.endsWith("ref"))
}

def iliad = {
  println("\n\nNEED TO BUILD ILIAD XML AUTOMATICALLY\n\n")
}


def catAll: String = {
  val libLines = Source.fromFile("archive/library.cex").getLines.toVector

  val codices =  Source.fromFile("archive/codices/vapages.cex").getLines.toVector

  val scholia =  Source.fromFile("archive/scholia-cex/scholia_xml.cex").getLines.toVector

  val vaimg =   val scholia =  Source.fromFile("archive/images/vaimgs.cex").getLines.toVector
  val vbimg =   val scholia =  Source.fromFile("archive/images/vbimgs.cex").getLines.toVector

  libLines.mkString("\n") + "\n" + codices.mkString("\n") + "\n" + vaimg.mkString("\n") + "\n" + vbimg.mkString("\n") + "\n" + scholia.mkString("\n")
}


def tidy = {
  //rm:  scholia-composites/*xml
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
