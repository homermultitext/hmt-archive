import edu.holycross.shot.cite._
import edu.holycross.shot.dse._
import org.homermultitext.hmtcexbuilder._
import java.io.PrintWriter

val libDir = "archive"
val dseDir = "archive/dse"

val libHeader = DataCollector.compositeFiles(libDir, "cex")
val dseCex = DataCollector.compositeFiles(dseDir, "cex")


val dse = Dse(libHeader + "\n" + dseCex )



def pageView(pageUrn: String ) = {
  val u = Cite2Urn(pageUrn)
  val md = s"# DSE relations of page ${pageUrn}\n\nDSE relations of [${u.objectComponent}](${dse.ictForSurface(u)})"
  new PrintWriter("dse-" + u.collection + "-" + u.objectComponent + ".md"){ write (md); close}
}


println("\n\nCreate a view for a given page:")
println("\n\tpageView(PAGEURN)\n\n")
