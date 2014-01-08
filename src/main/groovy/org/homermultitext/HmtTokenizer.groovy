package org.homermultitext

import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.Corpus

import org.apache.commons.io.FilenameUtils


/**
*/
class HmtTokenizer {

    /** Namespace object for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    /** Directory containing .txt files with tabulated representation of texts */
    File tabulatedDirectory

    /** Writable file where TTL output can be written. */
    File ttlFile


    /** Verbosity level 0-3 of debugging output */
    Integer debug = 1


    HmtTokenizer(File srcDir, File ttl) {
        this.tabulatedDirectory = srcDir
        this.ttlFile = ttl
    }

    void tokenize() {
        System.err.println "TOKENIZE HERE"
    }



    /** Creates an HmtTokenizer object and tokenizes tabular files
    */
    public static void main(String[] args) 
    throws Exception {
        if (args.size() != 2) {
            System.err.println "usage: HmtTokenizer TABDIR OUTPUTFILE"
            System.exit(-1)
        }

        File src
        File outFile

        try {
            src = new File (args[0])
            outFile = new File(args[1])

        } catch (Exception e) {
            System.err.println "Tabulator main method: Bad param or params: ${args}"
            throw e
        }
        HmtTokenizer ht = new HmtTokenizer(src,outFile)
        ht.tokenize()
    }
    /* end main method */

}
