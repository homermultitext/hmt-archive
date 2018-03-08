
val iliad = "src/va-iliad-1-12.xml"
val catalog = "src/ctscatalog.cex"
val citation = "src/citationconfig.cex"

import edu.holycross.shot.ohco2._

val repo = TextRepositorySource.fromFiles(catalog,citation,"src")
