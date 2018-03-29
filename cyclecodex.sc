import edu.holycross.shot.cite._
import edu.holycross.shot.scm._
import edu.holycross.shot.citeobj._
import org.homermultitext.hmtcexbuilder._

val libDir = "archive"
val tbsDir = "archive/codices"

val libHeader = DataCollector.compositeFiles(libDir, "cex")
val codicesCex = DataCollector.compositeFiles(tbsDir, "cex")

val codexLibCex = libHeader + "\n" + codicesCex
val codexLib = CiteLibrary(codexLibCex, "#", ",")
