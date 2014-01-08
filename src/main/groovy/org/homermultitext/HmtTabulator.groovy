package org.homermultitext

import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.Corpus

import org.apache.commons.io.FilenameUtils


/**
*/
class Tabulator {

    /** Namespace object for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")


    File archiveDirectory
    File textInventory
    File outputDirectory


    /** Verbosity level 0-3 of debugging output */
    Integer debug = 1


    Tabulator(File srcDir, File textInventory, File outDir) {
        this.archiveDirectory = srcDir
        this.textInventory = textInventory
        this.outputDirectory = outDir
    }


    /** Uses a hocuspocus Corpus to tabulate the entire repository.
    *  @throws Exception if outputDirectory does not exist and cannot
    *  be created.
    */
    void tabulate()  
    throws Exception {
        Corpus c = new Corpus(textInventory, archiveDirectory)
        try  {
            if (! outputDirectory.exists()) {
                outputDirectory.mkdir()
            } 
        } catch (Exception e) {
            System.err.println "Tabulator:  could not make output directory ${outputDirectory}"
            throw e
        }
        c.tabulateRepository(outputDirectory)
    }

    /** Creates a Tabulator object and creates tabular representation of all texts.
    */
    public static void main(String[] args) 
    throws Exception {

        if (args.size() != 3) {
            System.err.println "usage: Tabulator ARCHIVEDIR TEXTINVENTORY OUTPUTDIR"
            System.exit(-1)
        }

        File src
        //TextInventory ti
        File tiFile
        File outputDir 


        try {
            src = new File (args[0])
            tiFile = new File(args[1])
            //ti = new TextInventory(tiFile)
            outputDir = new File(args[2])
            if (! outputDir.exists()) {
                outputDir.mkdir()
            }
        } catch (Exception e) {
            System.err.println "Tabulator main method: Bad param or params: ${args}"
            throw e
        }
        Tabulator tab = new Tabulator(src, tiFile, outputDir)
        tab.tabulate()

    }
    /* end main method */

}
