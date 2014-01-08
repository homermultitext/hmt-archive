
package  org.homermultitext



import edu.holycross.shot.hocuspocus.HmtGreekTokenization
//import edu.holycross.shot.hocuspocus.Tabulator

import static org.junit.Assert.*
import org.junit.Test

/**
*/
class TestTokenize extends GroovyTestCase {

    File xmlDir = new File("testdata/tabulation/src/xml")
    File tiFile = new File ("testdata/tabulation/src/testinventory.xml")
    File outputDir = new File("build")
    File outputFile = new File("build/testtokens.ttl")


    File tabResults  = new File("build/VenetusA-Hadrian-Epigram-00001.txt")


    @Test
    void testTokenizingSoupToNuts() {
        File buildDir = new File ("build")
        if (! buildDir.exists()) {
            buildDir.mkdir()
        }
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }

        /* Generate from source files a tabulate file to tokenize: */
        HmtTabulator tab = new HmtTabulator(xmlDir,tiFile,outputDir)
        tab.tabulate()


        Integer expectedLines = 7
        assert tabResults.readLines().size() == expectedLines

        /* Tokenize with HMT Greek tokenizer: */
        HmtTokenizer tokenizer = new HmtTokenizer(xmlDir,tiFile,outputDir, outputFile)
        tokenizer.tokenize()


        Integer expectedTokens = 39
        File results = new File("${outputDir}/tokens.txt")
        assert results.readLines().size() == expectedTokens
    }

}
