
package  org.homermultitext


import edu.harvard.chs.cite.TextInventory

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestTabulate extends GroovyTestCase {

    File xmlDir = new File("testdata/tabulation/src")
    File outputDir = new File("build/tabtest")
    File tiFile = new File ("testdata/tabulation/src/testinventory.xml")

    File results  = new File("build/tabtest/VenetusA-Hadrian-Epigram-00001.txt")

    @Test
    void testTabulating() {
        File buildDir = new File ("build")
        if (! buildDir.exists()) {
            buildDir.mkdir()
        }
        if (! outputDir.exists()) {
            outputDir.mkdir()
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
