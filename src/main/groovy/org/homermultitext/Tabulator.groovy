package org.homermultitext

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.f1k.GreekNode
import org.apache.commons.io.FilenameUtils


/**
*/
class Tabulator {

    /** Namespace object for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")


    CtsUrn scholiaUrn = new CtsUrn("urn:cts:greekLit:tlg5026")

    /** Verbosity level 0-3 of debugging output */
    Integer debug = 1



    Tabulator() {
    }




    /** Creates a Tabulator object and creates tabular representation of all texts.
    */
    public static void main(String[] args) 
    throws Exception {

        if (args.size() != 0) {
            System.err.println "usage: ScholiaIndexer SRCDIR OUTPUTDIR OUTFILENAME ILIADURN VERSION "
            System.exit(-1)
        }
 /*        
        File src
        File outputDir 
        CtsUrn iliad

        try {
            src = new File (args[0])
            outputDir = new File(args[1])
            if (! outputDir.exists()) {
                outputDir.mkdir()
            }
        } catch (Exception e) {
            System.err.println "ScholiaIndexer main method: Bad param or params: ${args}"
            throw e
        }

        try {
            iliad = new CtsUrn(args[3])
        } catch (Exception e) {
            System.err.println "ScholiaIndexer main method: Bad param or params: ${args[3]}"
            throw e
        }
        ScholiaIndexer indexer = new ScholiaIndexer(src,outputDir, args[2], iliad, args[4])
        indexer.writeIndex()*/
    }
    /* end main method */

}
