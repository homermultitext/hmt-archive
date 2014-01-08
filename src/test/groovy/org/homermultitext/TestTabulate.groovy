
package  org.homermultitext


import edu.harvard.chs.cite.TextInventory

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestTabulate extends GroovyTestCase {

    File xmlDir = new File("testdata/tabulation/src")
    File outputDir = new File("build")
    File tiFile = new File ("testdata/tabulation/src/testinventory.xml")


    //TextInventory ti 

    @Test
    void testTabulating() {
      
//        ti = new TextInventory(tiFile)

        if (!outputDir.exists()) {
            outputDir.mkdir()
        }
        Tabulator tab = new Tabulator(xmlDir,tiFile,outputDir)
        tab.tabulate()
    }

}
