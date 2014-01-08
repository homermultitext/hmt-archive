package org.homermultitext

import edu.holycross.shot.hocuspocus.Corpus
import edu.harvard.chs.cite.TextInventory
import edu.harvard.chs.cite.CtsUrn

import edu.harvard.chs.f1k.GreekNode



class TextArchive {

    Corpus corpus

    TextArchive(File ti, archiveDir) {
        this.corpus = new Corpus(ti,archiveDir)
    }

    public static void main(String[] args) 
    throws Exception {
        if (args.size() != 4) {
            System.err.println "usage: TextArchive TEXTINVENTORY ARCHIVEDIR TABDIR RDFDIR"
            System.exit(-1)
        }
        File inv
        File archiveDirectory 
        File tabDir 
        File outputDir 
        try {
            inv = new File (args[0])
            archiveDirectory =  new File(args[1])
            tabDir = new File(args[2])
            outputDir = new File(args[3])

            if (! outputDir.exists()) {
                outputDir.mkdir()
            }
        } catch (Exception e) {
            System.err.println "TextArchive main method: Bad param or params: ${args}"
            throw e
        }
        Corpus c = new Corpus (inv, archiveDirectory)
        //c.turtleizeTabs(tabDir, "${args[3]}/cts.ttl", false)
        c.turtleizeRepository(outputDir, "hmt-cts.ttl")
    }

}
