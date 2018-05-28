// A Scala script to build a HMT project release.
import org.homermultitext.hmtcexbuilder._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.scm._
import org.homermultitext.edmodel._
import java.io.PrintWriter
import scala.io._



// Predefine layout of archive's file directory:
// 1. src files
val scholiaXml = "archive/scholia"
val iliadXml = "archive/iliad"
// 2. composite xml output
val scholiaComposites = "archive/scholia-xml-composites"
val iliadComposites = "archive/iliad-xml-composites"
// 3. CEX editions
val cexEditions = "archive/editions"

/** Write "corrigenda" report and CEX diplomatic edition for a
* Corpus of scholia.
*
* @param c Text corpus.
* @param siglum Version identifier for this set of scholia.
* @param editionsDir Directory where output should be written.
*/
def publishScholiaCorpus(tokens: Vector[TokenAnalysis], siglum: String, editionsDir: String) : Unit = {

  println("Created " + tokens.size + " tokens.")
  // Corrigenda to XML edition:
  val badtokens = tokens.filter(_.analysis.errors.size > 0)
  println(s"Found ${badtokens.size} errors in ${siglum}")
  val report = badtokens.map(err => "-   " + err.analysis.errorReport(" "))

  val corrHeader = s"\n\n## Corrigenda XML markup of scholia ${siglum}\n\n"
  new PrintWriter(s"${editionsDir}/va_${siglum}_corrigenda.md") { write(corrHeader + report.mkString("\n"));close }

  // Compose pure diplomatic edition:
  val diplEdition = DiplomaticEditionFactory.corpusFromTokens(tokens)
  val diplByScholion = diplEdition.exemplarToVersion("va_dipl")

  val diplHeader = s"\n\n#!ctscatalog\nurn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang\nurn:cts:greekLit:tlg5026.${siglum}.va_dipl:#book,scholion, section#Scholia to the Iliad#Scholia ${siglum} in the Venetus A#HMT project diplomatic edition##true#grc\n\n#!ctsdata\n"
  new PrintWriter(s"${editionsDir}/${siglum}_diplomatic.cex") { write(diplHeader + diplByScholion.cex("#"));close }
}


def indexAuthlists(tokens: Vector[TokenAnalysis], siglum: String, editionsDir: String) = {
  val persons = tokens.filter(_.analysis.lexicalDisambiguation.collection == "pers")
  val hdr = "#!relations\n"
  val persRelations = for (p <- persons) yield {
    p.textNode + "#urn:cite2:hmt:verbs.v1:appearsIn#" + p.analysis.lexicalDisambiguation
  }
  new PrintWriter(s"${editionsDir}/${siglum}_personrelations.cex") { write(hdr + persRelations.mkString("\n") + "\n"); close; }


  val places = tokens.filter(_.analysis.lexicalDisambiguation.collection == "place")
  val placeRelations = for (p <- places) yield {
    p.textNode + "#urn:cite2:hmt:verbs.v1:appearsIn#" + p.analysis.lexicalDisambiguation
  }

  new PrintWriter(s"${editionsDir}/${siglum}_placerelations.cex") { write(hdr + placeRelations.mkString("\n") + "\n"); close; }
}

/** Write index of scholia to Iliadic text they comment on.
*
* @param xrefNodes Vector of citation nodes.
* @param editionsDir Directory where output should be written.
*/
def indexScholiaCommentary(xrefNodes: Vector[CitableNode], editionsDir: String) : Unit = {

  val xrefUrns = for (n <- xrefNodes) yield {
    val scholion = n.urn.collapsePassageTo(2)
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
  val versionId = "va_dipl"
  val index = xrefUrns.map { case (sch,iliadOpt) =>
    iliadOpt match {
      case None => ""
      case u: Some[CtsUrn] => {
        s"${sch.dropVersion.addVersion(versionId)}#${verb}#${u.get}"
      }
    }
  }

  val hdr = "#!relations\n"
  new PrintWriter(s"${cexEditions}/commentaryIndex.cex") { write(hdr + index.mkString("\n") + "\n");close }

}

/**  Compose editions of scholia.  This includes
* an archival XML edition in CEX format;  a markdown
* document with corrigenda to the XML edition; and
* a pure diplomatic edition in CEX format.
*/
def scholia: Unit = {
  println("Creating composite XML editions of scholia...")
  ScholiaComposite.composite(scholiaXml, scholiaComposites)
  val catalog = s"${scholiaComposites}/ctscatalog.cex"
  val citation = s"${scholiaComposites}/citationconfig.cex"

  val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
  val scholiaNodes = repo.corpus.nodes.filterNot(_.urn.passageComponent.endsWith("ref"))

  val scholiaRepo = TextRepository( Corpus(scholiaNodes), repo.catalog)

  val tokens = TeiReader.fromCorpus(scholiaRepo.corpus)

  // Compute corrigenda for each scholia document, and
  // generate automatically derived editions:
  val scholiaDocs = scholiaRepo.corpus.nodes.map(_.urn.work).distinct
  for (s <- scholiaDocs) {
    if (s != "msAextra") {
    //if (s == "msAil") {
      println("Get tokens for " + s)
      val subCorpusTokens = tokens.filter(_.textNode.work == s)
      publishScholiaCorpus(subCorpusTokens, s, cexEditions)
      indexAuthlists(subCorpusTokens, s, cexEditions)
    }
  }


  // Write index file indexing scholia to Iliad passage they
  // comment on:
  val refNodes = repo.corpus.nodes.filter(_.urn.passageComponent.endsWith("ref"))
  indexScholiaCommentary (refNodes, cexEditions)

  // Now index all personal names and place names...
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
  new PrintWriter(s"${iliadComposites}/va_iliad_xml.cex") { write(repo.cex("#"));close }

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

  val authlistsCex = DataCollector.compositeFiles("archive/authlists", "cex")


  // Concatenate into a single string:
  List(libraryCex, tbsCex, textCex, imageCex, annotationCex, dseCex, indexCex, authlistsCex ).mkString("\n\n") + "\n"
}

/** Remove all temporary files created in process of composing
* a release.
*/
def tidy = {
  /*val scholiaCompositeFiles  = DataCollector.filesInDir(scholiaComposites, "xml")
  for (f <- scholiaCompositeFiles.toSeq) {
    f.delete()
  }
  val iliadCompositeFiles  = DataCollector.filesInDir(iliadComposites, "xml")
  for (f <- iliadCompositeFiles.toSeq) {
    f.delete()
  }*/

  val cexEditionFiles = DataCollector.filesInDir(cexEditions, "cex")
  for (f <- cexEditionFiles.toSeq) {
    f.delete()
  }

  val corrigendaFiles = DataCollector.filesInDir(cexEditions, "corrigenda.md")
  for (f <- corrigendaFiles.toSeq) {
    f.delete()
  }
}



/** Concatenate all CEX source into a single string.
*/
def catTextThings: String = {
  // The archive's root directory has the library definition
  // for a given release in the file "library.cex".
  val libraryCex = DataCollector.compositeFiles("archive", "cex")

  // Four subdirectories of the archive root contain all archival
  // data in CEX format:
  val textCex = DataCollector.compositeFiles( "archive/editions", "cex")
  val indexCex = DataCollector.compositeFiles("archive/relations", "cex")

  // Concatenate into a single string:
  List(libraryCex,  textCex,  indexCex ).mkString("\n\n") + "\n"
}

def releaseTexts(releaseId: String) =  {
  // build single CEX composite and write it out to a file:
  val allCex = catTextThings
  new PrintWriter(s"release-candidates/hmt-${releaseId}-texts.cex") { write(allCex); close}
}

def userGuide(releaseId: String) = {
  val lib = CiteLibrarySource.fromFile(s"release-candidates/hmt-${releaseId}.cex")
  val surveyor = ReleaseSurveyor(lib, "release-candidates" ,  releaseId)
  surveyor.overview(4, 200)
}

def updateAuthlists = {
  println("Retrieving personal names data from github...")
  val persnamesUrl = "https://raw.githubusercontent.com/homermultitext/hmt-authlists/master/data/hmtnames.cex"
  val personLines = Source.fromURL(persnamesUrl).getLines.toVector
  new PrintWriter("archive/authlists/hmtnames.cex") {write(personLines.mkString("\n") + "\n"); close;}

  println("Retrieving place names data from github...")
  val placenamesUrl = "https://raw.githubusercontent.com/homermultitext/hmt-authlists/master/data/hmtnames.cex"
  val placeLines = Source.fromURL(placenamesUrl).getLines.toVector
  new PrintWriter("archive/authlists/hmtplaces.cex") {write(placeLines.mkString("\n") + "\n"); close;}
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
  // Collect remote data:
  updateAuthlists

  // build single CEX composite and write it out to a file:
  val allCex = catAll
  new PrintWriter(s"release-candidates/hmt-${releaseId}.cex") { write(allCex); close}

  // From the composite, select material for a text-only release
  releaseTexts(releaseId)

  // build a single markdown file with all corrigenda, and
  // write it out to a file:
  val hdr = s"# All corrigenda for HMT release ${releaseId}\n\n"
  val corrigenda = DataCollector.compositeFiles("archive/editions", "corrigenda.md")
  new PrintWriter(s"release-candidates/hmt-${releaseId}-corrigenda.md") { write(hdr + corrigenda); close}

  // clean up intermediate files:
  tidy

  println(s"\nRelease ${releaseId} is available in release-candidates/hmt-${releaseId}.cex with accompanying list of corrigenda in release-candidates/hmt-${releaseId}-corrigenda.md\n")

  println("Now preparing user guide...")
  userGuide(releaseId)

}



println("\nBuild a release of the HMT archive:")
println("\n\trelease(RELEASE_ID)")
