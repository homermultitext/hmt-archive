// create .tsv iliad for use with external metrical parser
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.scm._

val releaseFile = "release-candidates/hmt-2019_1_rc2.cex"


// Create simple two-column string for
// diplomatic text of Iliad in a CEX library
def iliad(src: String =  releaseFile ) = {
  val lib = CiteLibrarySource.fromFile(src)
  val corpus = lib.textRepository.get.corpus
  val iliadCorpus = corpus ~~ CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
  val lines = iliadCorpus.nodes.map(n => n.urn.passageComponent + "#" + n.text)
  lines.map(_.replaceAll("[\t ]+", " ")).mkString("\n").replaceAll("#", "\t")
}
