
import org.homermultitext.hmtcexbuilder._

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.scm._
import org.homermultitext.edmodel._

import edu.holycross.shot.cex._

import java.io.PrintWriter
import scala.io._
import scala.xml._

// Predefine layout of archive's file directory:
// 1. src files
val scholiaXml = "archive/scholia"
val iliadXml = "archive/iliad"
// 2. composite xml output
val scholiaComposites = "archive/scholia-xml-composites"
val iliadComposites = "archive/iliad-xml-composites"
// 3. CEX editions
val cexEditions = "archive/editions"



/**  Compile editions of Iliad into a TextRepository.
*/
def iliadXmlRepo  : TextRepository = {
  // revisit this for making multiple Iliads...
  val fileBase = "va_iliad_"
  println("Creating editions of Iliad...")

  // create temporary composite XML file from source
  // documents organized by book:
  IliadComposite.composite(iliadXml, iliadComposites)
  val catalog = s"${iliadComposites}/ctscatalog.cex"
  val citation = s"${iliadComposites}/citationconfig.cex"
  TextRepositorySource.fromFiles(catalog,citation,iliadComposites)
}

def scholiaXmlRepo: TextRepository = {
  println("Creating composite XML editions of scholia...")
  ScholiaComposite.composite(scholiaXml, scholiaComposites, "va_composite_scholia")
  val catalog = s"${scholiaComposites}/ctscatalog.cex"
  val citation = s"${scholiaComposites}/citationconfig.cex"

  val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
  repo
}





// Construct HMT diplomatic corpus from XML corpus.
def diplCorpus(c: Corpus): Corpus = {
  val nodeOpts = for (n <- c.nodes.filterNot(_.urn.passageComponent.contains("ref"))) yield {
    try {
      Some(DiplomaticReader.editedNode(n))
      // yay
    } catch {
      case uoe : java.lang.UnsupportedOperationException => {
        if (n.urn.passageComponent.contains("lemma")) {
          // ok
          None
        } else {
          println("EMPTY NODE: " + n.urn)
          None
        }
      }
      case t: Throwable => {
        println(s"What happend on on ${n.urn}? " + t)
        None
      }
    }
  }
  Corpus(nodeOpts.toVector.flatten)
}

// Create CEX relations for commentary commenting on text.
def commentaryIndex(scholiaRepo: TextRepository) : String = {
  val verb = "urn:cite2:cite:verbs.v1:commentsOn"
  val xrefNodes = scholiaRepo.corpus.nodes.filter(_.urn.passageComponent.endsWith("ref"))
  val links = for (n <- xrefNodes) yield {
    val xn = XML.loadString(n.text)
    n.urn.collapsePassageBy(1) +  s"#${verb}#" + xn.text.trim
  }
  val hdr = "#!relations\n"
  hdr + links.mkString
}

def catAll: String = {
  // The archive's root directory has the library definition
  // for a given release in the file "library.cex".
  val libraryCex = DataCollector.compositeFiles("archive", "cex")

  // Four subdirectories of the archive root contain all archival
  // data in CEX format:
  val tbsCex = DataCollector.compositeFiles("archive/codices", "cex")
  val textCex = DataCollector.compositeFiles( "archive/editions", "cex")
  val imageCex = DataCollector.compositeFiles("archive/images", "cex")
  val annotationCex = DataCollector.compositeFiles("archive/annotations","cex")
  val dseCex = DataCollector.compositeFiles("archive/dse", "cex")
  val indexCex = DataCollector.compositeFiles("archive/relations", "cex")
  val authlistsCex = DataCollector.compositeFiles("archive/authlists", "cex")


  val scholiaCommentsIndex = commentaryIndex(scholiaXmlRepo)

  // Concatenate into a single string:
  List(libraryCex, tbsCex, textCex, imageCex, annotationCex, dseCex, indexCex, authlistsCex ).mkString("\n\n") + "\n"
}
