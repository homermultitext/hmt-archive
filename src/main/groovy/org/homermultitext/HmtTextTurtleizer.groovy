package org.homermultitext

import edu.harvard.chs.cite.TextInventory
import edu.holycross.shot.hocuspocus.Corpus

/**
*
*/
class HmtTextTurtleizer {

  // writable working directory
  File workDirectory 

  File ttlFile
    
  Corpus corpus
  
  boolean includePrefix

  HmtTextTurtleizer(File inventoryFile,  File archiveDir,  File workingDir, File outFile, boolean prefix) 
  throws Exception {

    try {
      if (! workingDir.exists()) {
	workingDir.mkdir()
	println "Made directory " + workingDir
      }
      this.workDirectory =  workingDir                
      this.ttlFile = outFile
      this.includePrefix =  prefix

      corpus = new Corpus(inventoryFile, archiveDir)

    } catch (Exception e) {
      throw new Exception ("HmtTextTurtelizer: unable to create Corpus from inventory ${inventoryFile} for archive ${archiveDir}: ${e}")
    }
  }


  /** 
   * main() method expects four arguments: 
   * 
   */
  public static void main(String[] args)  {

    boolean runIt = true
        
    String invFileName = args[0]
    String archiveName = args[1]
    String outputDirName = args[2]
    String outputFile = args[3]

    HmtTextTurtleizer t            
    switch (args.size()) {
    case 4:
    File outputDir
    File inventory
    File archiveDir
    File outFile

    try {
      outputDir = new File(outputDirName)
      outFile = new File(outputDir, outputFile)
      inventory = new File(invFileName)
      archiveDir = new File(archiveName)

      println "Files: output ${outputDir}, outFile ${outFile}"

    } catch (Exception e) {
      System.err.println "HmtTextTurteizer: unable to create files: ${e} " 
      runIt = false
    }

    try {
      if (! outputDir.exists()) {
	outputDir.mkdir()
      }
    } catch (Exception e) {
      System.err.println "HmtTextTurtelizer main: ${e}"
      runIt = false
    }

    if (! inventory.canRead()) {
      System.err.println "HmtTextTurtelizer main: cannot read inventory file '" + inventory + "'"
      runIt = false
    }


    if (! archiveDir.canRead()) {
      System.err.println "HmtTextTurtelizer main: cannot read archive directory ${archiveDir}"
      runIt = false
    }


    boolean prefix = false
    t  = new HmtTextTurtleizer(inventory, archiveDir, outputDir, outFile, prefix)
    t.generateTtl()
    
    break

            
    default :
    throw new Exception ("HmtTextTurteizer: main method needs 4 args" )
    break
    }

  }

  void generateTtl() {
    this.corpus.ttl(ttlFile, includePrefix, workDirectory)
  }

}
