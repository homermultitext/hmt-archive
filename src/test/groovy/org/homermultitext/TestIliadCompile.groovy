
package  org.homermultitext


import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestIliadCompile extends GroovyTestCase {

    File xmlDir = new File("testdata/Iliad")
    File header = new File("testdata/headers/test-Iliad.xml")
    File outputDir = new File("build")
    

    @Test
    void testCompilation() {
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }

        IliadCompiler compiler = new IliadCompiler(xmlDir, header, outputDir)
        compiler.compileTexts()
        assert compiler.compilationParses()
    }
}
