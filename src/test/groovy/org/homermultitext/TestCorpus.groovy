
package  org.homermultitext


import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.f1k.GreekNode
import edu.harvard.chs.cite.*

/**
*/
class TestCorpus extends GroovyTestCase {

    void testCorpus() {
        File archiveDir  = new File("build/editions/archive")
        File invFile = new File("build/editions/archive/inventory.xml")
        TextArchive ta = new TextArchive(invFile, archiveDir)        

        // because we put inventory.xml inside the archive:
        shouldFail {
            assert ta.corpus.filesAndInventoryMatch()
        }
        def extraFileList = ["inventory.xml"]
        assert ta.corpus.inventoriedMissingFromInventory() == extraFileList
    }
}
