
package  org.homermultitext


import edu.harvard.chs.cite.TextInventory

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestTokenize extends GroovyTestCase {

    File tabulatedDir = new File("testdata/tabulation/")
    File outputFile = new File("build/testtokens.ttl")

    @Test
    void testTokenizing() {
        File buildDir = new File ("build")
        if (! buildDir.exists()) {
            buildDir.mkdir()
        }

        Tabulator tab = new Tabulator(xmlDir,tiFile,outputDir)
        tab.tabulate()
        Integer expectedLines = 7
        Integer actualLines = 0
        results.eachLine {
            actualLines++
        }
        assert expectedLines == actualLines

    }

}
