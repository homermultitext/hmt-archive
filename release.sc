// A Scala script to build a HMT project release.

import org.homermultitext.hmtcexbuilder._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import org.homermultitext.edmodel._
import java.io.PrintWriter
import scala.io._



val scholiaXml = "archive/scholia"
val iliadXml = "archive/iliad"

val scholiaComposites = "archive/scholia-composites"
val iliadComposites = "archive/iliad-composites"

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

  val scholiaDocs = scholiaRepo.corpus.nodes.map(_.urn.work).distinct


  // WRITE CATALOG LIKE THIS:
  //
  for (s <- scholiaDocs) {
    if (s != "msAextra") {
      println("Create corpus for " + s)
      val subCorpusNodes = scholiaNodes.filter(_.urn.work == s)
      val c = Corpus(subCorpusNodes)
      val tokens = TeiReader.fromCorpus(c)
      println("Created " + tokens.size + " tokens.")
      val diplEdition = DiplomaticEditionFactory.corpusFromTokens(tokens)
      val diplByScholion = diplEdition.exemplarToVersion(s)

      val diplHeader = s"\n\n#!ctscatalog\nurn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang\nurn:cts:greekLit:tlg5026.${s}.va_dipl:#book,scholion, section#Scholia to the Iliad#Scholia ${s} in the Venetus A#HMT project diplomatic edition##true#grc\n\n#!ctsdata\n"
      new PrintWriter(s"${cexEditions}/${s}_diplomatic.cex") { write(diplHeader + diplByScholion.cex("#"));close }
    }
  }
  val xrefNodes = repo.corpus.nodes.filter(_.urn.passageComponent.endsWith("ref"))
}

def iliad = {
  // revisit this for making multiple Iliads...
  val fileBase = "va_iliad_"
  println("Creating CEX editions of Iliad...")
  IliadComposite.composite(iliadXml, iliadComposites)

  val catalog = s"${iliadComposites}/ctscatalog.cex"
  val citation = s"${iliadComposites}/citationconfig.cex"

  val repo = TextRepositorySource.fromFiles(catalog,citation,iliadComposites)
  new PrintWriter(s"${cexEditions}/va_iliad_xml.cex") { write(repo.cex("#"));close }


  val tokens = TeiReader.fromCorpus(repo.corpus)
  val diplIliad = DiplomaticEditionFactory.corpusFromTokens(tokens)
  val diplIliadByLine = diplIliad.exemplarToVersion("msA")

  val diplHeader = "\n\n#!ctscatalog\nurn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang\nurn:cts:greekLit:tlg0012.tlg001.msA:#book,line#Homeric epic#Iliad#HMT project diplomatic edition##true#grc\n\n#!ctsdata\n"

  new PrintWriter(s"${cexEditions}/va_iliad_diplomatic.cex") { write(diplHeader + diplIliadByLine.cex("#"));close }

}


def catAll: String = {
  val libLines = Source.fromFile("archive/library.cex").getLines.toVector

  val codices =  Source.fromFile("archive/codices/vapages.cex").getLines.toVector

  val iliad =  Source.fromFile("archive/editions/va_iliad_xml.cex").getLines.toVector


  val iliadDipl =  Source.fromFile("archive/editions/va_iliad_diplomatic.cex").getLines.toVector


  val scholia =  Source.fromFile("archive/editions/scholia_xml.cex").getLines.toVector

  val msA = Source.fromFile("archive/editions/msA_diplomatic.cex").getLines.toVector
  val msAext = Source.fromFile("archive/editions/msAext_diplomatic.cex").getLines.toVector
  val msAil = Source.fromFile("archive/editions/msAil_diplomatic.cex").getLines.toVector
  val msAim = Source.fromFile("archive/editions/msAim_diplomatic.cex").getLines.toVector
  val msAint = Source.fromFile("archive/editions/msAint_diplomatic.cex").getLines.toVector

  val vaimg =  Source.fromFile("archive/images/vaimgs.cex").getLines.toVector
  val vbimg =  Source.fromFile("archive/images/vbimgs.cex").getLines.toVector

  val arist =  Source.fromFile("archive/commentaries-annotations/aristarchansigns.cex").getLines.toVector

  val critsigns =  Source.fromFile("archive/commentaries-annotations/va_criticalsigns.cex").getLines.toVector


  val dse =  Source.fromFile("archive/dse/va-dse.cex").getLines.toVector

  libLines.mkString("\n") + "\n" + codices.mkString("\n") + "\n" + vaimg.mkString("\n") + "\n" + vbimg.mkString("\n") + "\n" + iliad.mkString("\n") +  scholia.mkString("\n") + "\n" + arist.mkString("\n") + "\n" + critsigns.mkString("\n") + "\n" + dse.mkString("\n") + "\n" + iliadDipl.mkString("\n") + "\n" + msA.mkString("\n") + "\n" + msAext.mkString("\n") + "\n" + msAil.mkString("\n") + "\n" + msAim.mkString("\n") + "\n" + msAint.mkString("\n") 

  /*archive/editions/msA_diplomatic.cex
  archive/editions/msAext_diplomatic.cex
  archive/editions/msAil_diplomatic.cex
  archive/editions/msAim_diplomatic.cex
  archive/editions/msAint_diplomatic.cex
  */
}


def tidy = {
  val scholiaCompositeFiles  = FileCollector.filesInDir(scholiaComposites, "xml")
  for (f <- scholiaCompositeFiles.toSeq) {
    f.delete()
  }
  val iliadCompositeFiles  = FileCollector.filesInDir(iliadComposites, "xml")
  for (f <- iliadCompositeFiles.toSeq) {
    f.delete()
  }
  val cexEditionFiles = FileCollector.filesInDir(cexEditions, "cex")
  for (f <- cexEditionFiles.toSeq) {
    f.delete()
  }
}


def release(releaseId: String) =  {
  // Generate intermediate files:
  scholia
  iliad
  // build single composite and write it:
  val allCex = catAll
  new PrintWriter(s"releases-cex/hmt-${releaseId}.cex") { write(allCex); close}
  // clean up all intermediate files:
  tidy

  println(s"\nRelease ${releaseId} is available in releases-cex/hmt-${releaseId}.cex\n")
}


println("\nBuild a release of the HMT archive:")
println("\n\trelease(RELEASE_ID)")
