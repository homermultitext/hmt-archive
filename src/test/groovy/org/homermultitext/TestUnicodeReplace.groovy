
package  org.homermultitext


import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.f1k.GreekNode
import edu.harvard.chs.cite.*

/**
*/
class TestUnicodeReplace extends GroovyTestCase {
    String tstToken = "ἔθηκεν·"
    def highStop = "\u0387"

    void testReplace() {
        OutputStreamWriter osw = new OutputStreamWriter(System.out)
        osw.write "REPLACE: ${highStop} in ${tstToken} : \n"
        osw.write tstToken.replaceAll(highStop,'') + "\n"
        osw.close()
    }
    
}
