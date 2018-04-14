import edu.holycross.shot.cite._
import edu.holycross.shot.dse._
import edu.holycross.shot.scm._
import org.homermultitext.hmtcexbuilder._
import edu.holycross.shot.cex._
import java.io.PrintWriter
import edu.holycross.shot.citeobj._

val libDir = "archive"
val dseDir = "archive/dse"
val tbsDir = "archive/codices"

val libHeader = DataCollector.compositeFiles(libDir, "cex")
val dseCex = DataCollector.compositeFiles(dseDir, "cex")
val codicesCex = DataCollector.compositeFiles(tbsDir, "cex")

val codexLibCex = libHeader + "\n" + codicesCex
val codexLib = CiteLibrary(codexLibCex, "#", ",")

val dse = Dse(libHeader + "\n" + dseCex )
val codexRepo =  CiteCollectionRepository(codicesCex)
val codexUrns = codexRepo.collections.toSeq


/** Writes a markdown file with a link to ICT2
* view of a requested page.  The output file is named
* "dse-COLLECTION-OBJEct.md".  Example:
*
*   pageView("urn:cite2:hmt:msA.v1:12r")
*
*  writes its output to "dse-msA-12r.md"
*
* @param pageUrn URN of page
*/
def pageView(pageUrn: String ) = {
  val u = Cite2Urn(pageUrn)
  val md = s"# DSE relations of page ${pageUrn}\n\nDSE relations of [${u.objectComponent}](${dse.ictForSurface(u)})"
  new PrintWriter("dse-" + u.collection + "-" + u.objectComponent + ".md"){ write (md); close}
}


println("\n\nCreate a view for a given page:")
println("\n\tpageView(PAGEURN)\n\n")
