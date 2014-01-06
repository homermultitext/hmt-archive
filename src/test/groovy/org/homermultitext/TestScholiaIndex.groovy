
package  org.homermultitext


import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestScholiaIndex extends GroovyTestCase {

    File xmlDir = new File("testdata/Scholia")
    File outputDir = new File("build")
    String indexFileName = "testScholiaIndex.tsv"
    CtsUrn iliad = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA")
    String version = "hmt"

    @Test
    void testIndexing() {
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }
        ScholiaIndexer indexer = new ScholiaIndexer(xmlDir, outputDir,indexFileName, iliad,version)

        indexer.writeIndex()

    }

}
