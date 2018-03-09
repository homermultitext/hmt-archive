import edu.holycross.shot.ohco2._
import java.io.PrintWriter

val iliad = "src/va-iliad-1-12.xml"
val catalog = "src/ctscatalog.cex"
val citation = "src/citationconfig.cex"

// Publish Iliad in one line:
new PrintWriter("va_iliad_xml.cex") { write(TextRepositorySource.fromFiles(catalog,citation,"src").cex("#"));close }
