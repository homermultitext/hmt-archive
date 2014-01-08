
package  org.homermultitext


import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.f1k.GreekNode
import edu.harvard.chs.cite.*

/**
*/
class TestCorpus extends GroovyTestCase {


    File xmlDir = new File("testdata/tabulation/src/xml")
    File tiFile = new File ("testdata/tabulation/src/testinventory.xml")
    TextArchive ta = new TextArchive(tiFile, xmlDir)        


    void testCorpus() {
        System.err.println "In archive: " + ta.corpus.filesInArchive()
        System.err.println "In inventory: " + ta.corpus.filesInInventory()
        assert ta.corpus.filesAndInventoryMatch()
    }
}
