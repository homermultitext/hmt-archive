package org.homermultitext

import edu.holycross.shot.abracadabra.*


/**
*
*/
class HmtIndexTurtleizer {

    boolean debug = false

    File inventory

    File data


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput

    boolean includePrefix

    HmtIndexTurtleizer(File inv, File srcDir, File outFile) {
        this.inventory =  inv
        this.data = srcDir
        this.turtleOutput = outFile
        this.includePrefix = false
    }

    HmtIndexTurtleizer(File inv, File srcDir, File outFile, boolean prefix) {
        this.inventory =  inv
        this.data = srcDir
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
                throw new Exception("main method requires five parameters.")
            System.exit(-1)
            break

            case 4:
                try {
                File inv = new File(args[0])
                File dataDir = new File(args[1])
                File outDir = new File(args[2])
                if (! outDir.exists()) {
                    outDir.mkdir()
                }
                File outFile = new File(outDir, args[3])

                boolean prefix = false
                if ( (!inv.exists())  || (!dataDir.exists())) {
                    System.err.println "HmtIndexTurtelizer:  ${inv} and ${dataDir} must exist."
                } else {
                    HmtIndexTurtleizer ttl = new HmtIndexTurtleizer(inv, dataDir, outFile, prefix)
                    ttl.generateTurtle()
                }

            } catch (Exception e) {
                throw e
            }
            break


            default:
            break
        }
    }


    void generateTurtle() {
      if (debug) {
	println "generateTurtle with inventory: ${inventory}; data ${data}"
	println "Output to ${turtleOutput}"
      }
      CiteIndex idx = new CiteIndex(inventory, data)
      idx.ttl(turtleOutput, includePrefix)
    }

}
