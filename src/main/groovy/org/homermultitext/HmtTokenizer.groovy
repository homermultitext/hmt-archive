package org.homermultitext

import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.Corpus
//import edu.holycross.shot.hutils.HmtGreekTokenization
import org.homermultitext.utils.HmtGreekTokenization

import org.apache.commons.io.FilenameUtils


/**
*/
class HmtTokenizer {

    /** Namespace object for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    File archiveDirectory
    File textInventory

    /** Directory containing .txt files with tabulated representation of texts */
    File outputDirectory

    /** Verbosity level 0-3 of debugging output */
    Integer debug = 1


    HmtTokenizer(File srcDir, File textInventory, File outDir) {
        this.archiveDirectory = srcDir
        this.textInventory = textInventory
        this.outputDirectory = outDir
    }

    void tokenize() 
    throws Exception {
        Corpus c = new Corpus(textInventory, archiveDirectory)
	def tokenSystemMap = ["grc" : "edu.holycross.shot.hocuspocus.HmtGreekTokenization"]
	c.languageToTokenSystemMap = tokenSystemMap.clone()

        try  {
            if (! outputDirectory.exists()) {
                outputDirectory.mkdir()
            } 
        } catch (Exception e) {
            System.err.println "HmtTokenizer:  could not make output directory ${this.outputDirectory}"
            throw e
        }
	System.out.println "Beginning to tokenize entire repository:  please be patient."
        c.tokenizeRepository(this.outputDirectory)

	// kludge and a half! cleanup all txt files except token.txt...
	this.outputDirectory.eachFileMatch(~/.*.txt/) { tab ->  
	  if ("tokens.txt" != tab.name) {
	    tab.delete()
	  }
	}
    }



    /** Creates an HmtTokenizer object and tokenizes tabular files
    */
    public static void main(String[] args) 
    throws Exception {
        if (args.size() != 3) {
            System.err.println "usage: HmtTokenizer ARCHIVEDIR TEXTINVENTORY OUTPUTDIR "
            System.exit(-1)
        }

        File src
        File tiFile
        File outputDir 

        try {
            src = new File (args[0])
            tiFile = new File(args[1])
            outputDir = new File(args[2])
            if (! outputDir.exists()) {
                outputDir.mkdir()
            }
        } catch (Exception e) {
            System.err.println "HmtTabulator main method: Bad param or params: ${args}"
            throw e
        }


        HmtTokenizer ht = new HmtTokenizer(src,tiFile,outputDir)
        ht.tokenize()
    }
    /* end main method */

}
