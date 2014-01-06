package org.homermultitext

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.f1k.GreekNode
import org.apache.commons.io.FilenameUtils


/** Class for indexing scholia to text they comment on.
*/
class ScholiaIndexer {

    /** Directory containing TEI XML files, named
    * following HMT project conventions.
    */
    File xmlSourceDirectory

    /** Writable directory where composite edition will be created.
    */
    File outputDirectory

    /** Name to use for output file. */
    String indexFileName

    /** CTS URN for version of Iliad indexed. */
    CtsUrn iliadUrn 

    String version 

    /** Verbosity level 0-3 of debugging output */
    Integer debug = 1



    ScholiaIndexer(File inputDir, File outDir, String outFileName, CtsUrn iliad, String versionStr) {
        this.xmlSourceDirectory = inputDir        
        this.outputDirectory = outDir
        this.indexFileName = outFileName
        this.iliadUrn = iliad
        this.version = versionStr
    }


    void writeIndex() {
        System.err.println "Here is where you write an index (.tsv) file"
    }



    /** Creates a ScholiaIndexer object and indexes scholia to Iliad passages.
    */
    public static void main(String[] args) 
    throws Exception {

        if (args.size() != 5) {
            System.err.println "usage: ScholiaIndexer SRCDIR OUTPUTDIR OUTFILENAME ILIADURN VERSION "
            System.exit(-1)
        }
        
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
        indexer.writeIndex()
    }
    /* end main method */

}
