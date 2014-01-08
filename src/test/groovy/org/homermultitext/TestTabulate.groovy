
package  org.homermultitext


import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestTabulate extends GroovyTestCase {

    File xmlDir = new File("testdata/Scholia")
    File outputDir = new File("build")
    String indexFileName = "testScholiaIndex.tsv"
    CtsUrn iliad = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA")
    String version = "hmt"

    @Test
    void testTabulating() {
        Tabulator tab = new Tabulator()
    }

}
