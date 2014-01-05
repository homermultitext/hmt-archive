
package  org.homermultitext


import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.f1k.GreekNode
import edu.harvard.chs.cite.*
import edu.holycross.shot.hocuspocus.Corpus

/**
*/
class TestTabGen extends GroovyTestCase {

    void testTabulateAll() {
        File archiveDir = new File("build/editions/archive")
        File invFile = new File("build/editions/archive/inventory.xml")
        Corpus c = new Corpus(invFile, archiveDir)
        
        File tabulation = new File("build/tabulated")
        if (! tabulation.exists()) {
            tabulation.mkdir()
        }
        c.tabulateInventory(tabulation)
    }
    
}
