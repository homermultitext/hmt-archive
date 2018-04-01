// A Scala script to build a HMT project release.

import org.homermultitext.hmtcexbuilder._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.scm._
import org.homermultitext.edmodel._
import java.io.PrintWriter
import scala.io._


// Predefine layout of archive's file directory:
val scholiaXml = "archive/scholia"
val iliadXml = "archive/iliad"

val scholiaComposites = "archive/scholia-composites"
val iliadComposites = "archive/iliad-composites"

val cexEditions = "archive/editions"

/**  Compose editions of scholia.  This includes
* an archival XML edition in CEX format;  a markdown
* document with corrigenda to the XML edition; and
* a pure diplomatic edition in CEX format.
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


  // Now compute corrigenda for each scholia document, and
  // generate automatically derived editions:
  val scholiaDocs = scholiaRepo.corpus.nodes.map(_.urn.work).distinct
  for (s <- scholiaDocs) {

    // omit this class for now:
    if (s != "msAextra") {
      println("Create corpus for " + s)
      val subCorpusNodes = scholiaNodes.filter(_.urn.work == s)
      val c = Corpus(subCorpusNodes)
      val tokens = TeiReader.fromCorpus(c)
      println("Created " + tokens.size + " tokens.")

      // Corrigenda to XML edition:
      val badtokens = tokens.filter(_.analysis.errors.size > 0)
      println(s"Found ${badtokens.size} errors in ${s}")
      val report = badtokens.map(err => "-   " + err.analysis.errorReport(" "))

      val corrHeader = s"\n\n## Corrigenda XML markup of scholia ${s}\n\n"
      new PrintWriter(s"${cexEditions}/va_${s}_corrigenda.md") { write(corrHeader + report.mkString("\n"));close }

      // Compose pure diplomatic edition:
      val diplEdition = DiplomaticEditionFactory.corpusFromTokens(tokens)
      val diplByScholion = diplEdition.exemplarToVersion("va_dipl")

      val diplHeader = s"\n\n#!ctscatalog\nurn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang\nurn:cts:greekLit:tlg5026.${s}.va_dipl:#book,scholion, section#Scholia to the Iliad#Scholia ${s} in the Venetus A#HMT project diplomatic edition##true#grc\n\n#!ctsdata\n"
      new PrintWriter(s"${cexEditions}/${s}_diplomatic.cex") { write(diplHeader + diplByScholion.cex("#"));close }
    }
  }

  // Write index file indexing scholia to Iliad passage they
  // comment on:
  val xrefNodes = repo.corpus.nodes.filter(_.urn.passageComponent.endsWith("ref"))


  val xrefUrns = for (n <- xrefNodes) yield {
    val scholion = n.urn
    val iliadUrnText = DataCollector.collectXmlText(n.text).trim
    try {

      val iliadUrn = CtsUrn(iliadUrnText)
      (scholion, Some(iliadUrn))
    } catch {
      case t: Throwable => {
        println("Could not parse " + iliadUrnText)
        (scholion, None)
      }
    }
  }
  val verb = "urn:cite2:cite:verbs.v1:commentsOn"
  val index = xrefUrns.map { case (sch,iliadOpt) =>
    iliadOpt match {
      case None => ""
      case u: Some[CtsUrn] => s"${sch}#${verb}#${u.get}"
    }
  }

  val hdr = "#!relations\n"
  new PrintWriter(s"${cexEditions}/commentaryIndex.cex") { write(hdr + index.mkString("\n") + "\n");close }
}


/**  Compose editions of Iliad.  This includes
* an archival XML edition in CEX format;  a markdown
* document with corrigenda to the XML edition; and
* a pure diplomatic edition in CEX format.
*/
def iliad = {
  // revisit this for making multiple Iliads...
  val fileBase = "va_iliad_"
  println("Creating editions of Iliad...")

  // create temporary composite XML file from source
  // documents organized by book:
  IliadComposite.composite(iliadXml, iliadComposites)
  val catalog = s"${iliadComposites}/ctscatalog.cex"
  val citation = s"${iliadComposites}/citationconfig.cex"
  val repo = TextRepositorySource.fromFiles(catalog,citation,iliadComposites)
  // write CEX-formatted version of archival XML:
  new PrintWriter(s"${cexEditions}/va_iliad_xml.cex") { write(repo.cex("#"));close }

  val tokens = TeiReader.fromCorpus(repo.corpus)
  // Compute corrigenda:
  val badtokens = tokens.filter(_.analysis.errors.size > 0)
  val report = badtokens.map(err => "-   " + err.analysis.errorReport(" "))
  val corrHeader = "\n\n## Corrigenda to XML markup of *Iliad*\n\n"
  new PrintWriter(s"${cexEditions}/va_iliad_corrigenda.md") { write(corrHeader + report.mkString("\n"));close }

  // Generate pure diplomatic edition:
  val diplIliad = DiplomaticEditionFactory.corpusFromTokens(tokens)
  val diplIliadByLine = diplIliad.exemplarToVersion("msA")

  val diplHeader = "\n\n#!ctscatalog\nurn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang\nurn:cts:greekLit:tlg0012.tlg001.msA:#book,line#Homeric epic#Iliad#HMT project diplomatic edition##true#grc\n\n#!ctsdata\n"

  new PrintWriter(s"${cexEditions}/va_iliad_diplomatic.cex") { write(diplHeader + diplIliadByLine.cex("#"));close }
}


/** Concatenate all CEX source into a single string.
*/
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

  // Concatenate into a single string:
  List(libraryCex, tbsCex, textCex, imageCex, annotationCex, dseCex, indexCex ).mkString("\n\n") + "\n"
}

/** Remove all temporary files created in process of composing
* a release.
*/
def tidy = {
  val scholiaCompositeFiles  = DataCollector.filesInDir(scholiaComposites, "xml")
  for (f <- scholiaCompositeFiles.toSeq) {
    f.delete()
  }
  val iliadCompositeFiles  = DataCollector.filesInDir(iliadComposites, "xml")
  for (f <- iliadCompositeFiles.toSeq) {
    f.delete()
  }

  val cexEditionFiles = DataCollector.filesInDir(cexEditions, "cex")
  for (f <- cexEditionFiles.toSeq) {
    f.delete()
  }

  val corrigendaFiles = DataCollector.filesInDir(cexEditions, "corrigenda.md")
  for (f <- corrigendaFiles.toSeq) {
    f.delete()
  }
}


/** Publish a release of the Homer Multitext project archive.
*
* @param releaseId Identifier for the release.
* The value should be the version identifier for this release's URN
* as given in `library.cex`.  E.g., to publish release
* `urn:cite2:hmt:publications.cex.2018a:all`, use `2018a`
* as the value for releaseId.
*/
def release(releaseId: String) =  {
  // Generate intermediate files:
  scholia
  iliad
  // build single CEX composite and write it out to a file:
  val allCex = catAll
  new PrintWriter(s"releases-cex/hmt-${releaseId}.cex") { write(allCex); close}

  // build a single markdown file with all corrigenda, and
  // write it out to a file:
  val hdr = s"# All corrigenda for HMT release ${releaseId}\n\n"
  val corrigenda = DataCollector.compositeFiles("archive/editions", "corrigenda.md")
  new PrintWriter(s"releases-cex/hmt-${releaseId}-corrigenda.md") { write(hdr + corrigenda); close}

  // clean up intermediate files:
  tidy

  println(s"\nRelease ${releaseId} is available in releases-cex/hmt-${releaseId}.cex with accompanying list of corrigenda in releases-cex/hmt-${releaseId}-corrigenda.md\n")

  println("Now preparing user guide...")
  val lib = CiteLibrarySource.fromFile(s"releases-cex/hmt-${releaseId}.cex")
  val surveyor = ReleaseSurveyor(lib, "releases-cex" ,  releaseId)
  surveyor.overview(6, 300)
}




println("\nBuild a release of the HMT archive:")
println("\n\trelease(RELEASE_ID)")
