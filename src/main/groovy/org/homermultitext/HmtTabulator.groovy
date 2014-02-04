package org.homermultitext

import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.Corpus

import org.apache.commons.io.FilenameUtils


/**
*/
class HmtTabulator {

    /** Namespace object for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")


    File archiveDirectory
    File textInventory
    File outputDirectory


    /** Verbosity level 0-3 of debugging output */
    Integer debug = 3


    HmtTabulator(File srcDir, File textInventory, File outDir) {
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
	if (debug > 1) {
	  System.err.println "Files in archive: " + c.filesInArchive()
	  System.err.println "Files in inventory: " + c.filesInInventory()
	  
	}
        try  {
            if (! outputDirectory.exists()) {
                outputDirectory.mkdir()
            } 
        } catch (Exception e) {
            System.err.println "HmtTabulator:  could not make output directory ${outputDirectory}"
            throw e
        }
        c.tabulateRepository(outputDirectory)
    }

    /** Creates a HmtTabulator object and creates tabular representation of all texts.
    */
    public static void main(String[] args) 
    throws Exception {

        if (args.size() != 3) {
            System.err.println "usage: HmtTabulator ARCHIVEDIR TEXTINVENTORY OUTPUTDIR"
            System.exit(-1)
        }

        File src
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
            System.err.println "HmtTabulator main method: Bad param or params: ${args}"
            throw e
        }

	System.err.println "Making tabularo with: ${src}, inv ${tiFile}, output ${outputDir}"
        HmtTabulator tab = new HmtTabulator(src, tiFile, outputDir)
        tab.tabulate()

    }
    /* end main method */

}
