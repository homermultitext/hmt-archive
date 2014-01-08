package org.homermultitext

import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.Corpus
import edu.holycross.shot.hocuspocus.HmtGreekTokenization

import org.apache.commons.io.FilenameUtils


/**
*/
class HmtTokenizer {

    /** Namespace object for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    File archiveDirectory
    File textInventory

    /** Directory containing .txt files with tabulated representation of texts */
    File tabulatedDirectory

    /** Writable file where TTL output can be written. */
    File ttlFile


    /** Verbosity level 0-3 of debugging output */
    Integer debug = 1


    HmtTokenizer(File srcDir, File textInventory, File outDir, File ttlOutput) {
        this.archiveDirectory = srcDir
        this.textInventory = textInventory
        this.tabulatedDirectory = outDir
        this.ttlFile = ttlOutput
    }

    void tokenize() 
    throws Exception {
        Corpus c = new Corpus(textInventory, archiveDirectory)
        try  {
            if (! tabulatedDirectory.exists()) {
                tabulatedDirectory.mkdir()
            } 
        } catch (Exception e) {
            System.err.println "HmtTabulator:  could not make output directory ${tabulatedDirectory}"
            throw e
        }
        c.tokenizeInventory(new HmtGreekTokenization(), tabulatedDirectory)


    }



    /** Creates an HmtTokenizer object and tokenizes tabular files
    */
    public static void main(String[] args) 
    throws Exception {
        if (args.size() != 4) {
            System.err.println "usage: HmtTokenizer ARCHIVEDIR TEXTINVENTORY OUTPUTDIR TTLFILE"
            System.exit(-1)
        }

        File src
        File tiFile
        File outputDir 
        File ttl

        try {
            src = new File (args[0])
            tiFile = new File(args[1])
            outputDir = new File(args[2])
            if (! outputDir.exists()) {
                outputDir.mkdir()
            }
            ttl = new File(args[3])
        } catch (Exception e) {
            System.err.println "HmtTabulator main method: Bad param or params: ${args}"
            throw e
        }



        HmtTokenizer ht = new HmtTokenizer(src,tiFile,outputDir,ttl)
        ht.tokenize()
    }
    /* end main method */

}
