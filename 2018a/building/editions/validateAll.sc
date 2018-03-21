import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import org.homermultitext.edmodel._
import java.io.PrintWriter


val catalog = "src/ctscatalog.cex"
val citation = "src/citationconfig.cex"

val repo = TextRepositorySource.fromFiles(catalog,citation,"src")
