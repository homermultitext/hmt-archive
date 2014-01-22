package org.homermultitext

import edu.holycross.shot.nysi.*
import edu.holycross.shot.hocuspocus.Corpus

/**
*
*/
class HmtImgTurtleizer {


    File collectionDirectory 

    File imageDirectory 


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput

    boolean includePrefix

    HmtImgTurtleizer(File collDir, File imgDir, File outFile) {
        this.collectionDirectory =  collDir
        this.imageDirectory = imgDir
        this.turtleOutput = outFile
        this.includePrefix = false
    }

    HmtImgTurtleizer(File collDir, File imgDir, File outFile, boolean prefix) {
        this.collectionDirectory =  collDir
        this.imageDirectory = imgDir
        this.turtleOutput = outFile
        this.includePrefix = prefix
    }


  /** 
   * main() method expects four arguments:
   */
  public static void main(String[] args) 
  throws Exception {

    switch (args.size()) {
    case 0:
    throw new Exception("main method requires four parameters.")
    System.exit(-1)
    break

    case 4:
    try {
      File collDir = new File(args[0])
      File imgDir = new File(args[1])
      File outDir = new File(args[2])
      String outFileName = new File(args[3])

      if (! outDir.exists()) {
	outDir.mkdir()
      }
      File  outFile = new File(outDir, outFileName)

      boolean prefix = false
      HmtImgTurtleizer ttl = new HmtImgTurtleizer(collDir, imgDir, outFile, prefix)
      ttl.generateTurtle()
      
    } catch (Exception e) {
      throw e
    }
    break

    default:
    break
    }
  }


  void generateTurtle() {
    edu.holycross.shot.nysi.ImgTurtleizer ittl  = new edu.holycross.shot.nysi.ImgTurtleizer(this.collectionDirectory.toString(), this.imageDirectory.toString())

    System.err.println "Generating img TTL from collections in " + this.collectionDirectory + " and data in " + this.imageDirectory
    ittl.ttl(turtleOutput, includePrefix) 
  }

}
