
import org.homermultitext.hmtcexbuilder._

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.scm._
import org.homermultitext.edmodel._

import edu.holycross.shot.cex._

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


def diplBook(group: String, bknum: String) : Corpus = {
  println(s"Trying to read ${bknum} diplomatically...")
  val repo =  group match {
    case "tlg0012" => iliadXmlRepo
    case "tlg5026" =>    scholiaXmlRepo

    case _ => throw new Exception("Don't understand group " + group)
  }
  val bkcorpus = repo.corpus ~~ CtsUrn(s"urn:cts:greekLit:${group}:${bknum}")
  diplCorpus(bkcorpus)
}

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

/*
val sxml = scholiaXmlRepo
val s20 = CtsUrn("urn:cts:greekLit:tlg5026:20")
val s16 = CtsUrn("urn:cts:greekLit:tlg5026:16")
val s9 = CtsUrn("urn:cts:greekLit:tlg5026:9")

val ok = sxml.corpus.nodes.filterNot(_.urn ~~ s20).filterNot(_.urn ~~ s16).filterNot(_.urn ~~ s9)
val okCorpus = Corpus(ok)
*/



// ILIAD 22 IS SERIOUSLY MESSED UP
//
// Scholia to 20 are messed up
// Scholia to 16 need work
//
// Scholia to 9 being separately worked on
