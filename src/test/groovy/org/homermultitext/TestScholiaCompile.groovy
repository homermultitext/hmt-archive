
package  org.homermultitext


import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestScholiaCompile extends GroovyTestCase {

    File xmlDir = new File("testdata/Scholia")
    File header = new File("testdata/headers/test-Scholia.xml")
    File outputDir = new File("build")
    String siglum = "msA"

    @Test
    void testCompilation() {
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }
        ScholiaCompiler sc = new ScholiaCompiler(xmlDir, header, outputDir, siglum)
        assert sc
        sc.compileTexts()
    }
}
