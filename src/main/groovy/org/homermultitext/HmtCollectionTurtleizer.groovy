package org.homermultitext

import edu.holycross.shot.prestochango.*


/**
*
*/
class HmtCollectionTurtleizer {

    boolean debug = true

    File collectionInventory

    File dataDirectory 

    String inventorySource


    /** Writable file for resulting turtle-formatted triplets. */
    File turtleOutput

    boolean includePrefix


    HmtCollectionTurtleizer(File inv, String invSch, File srcDir, File outDir) {
        this.collectionInventory =  inv
        this.inventorySource = invSch
        this.dataDirectory = srcDir
        this.turtleOutput = new File(outDir, "collections.ttl")
        this.includePrefix = false
    }

    HmtCollectionTurtleizer(File inv, String invSch, File srcDir, File outDir, boolean prefix) {
        this.collectionInventory =  inv
        this.inventorySource = invSch
        this.dataDirectory = srcDir
        this.turtleOutput = new File(outDir, "collections.ttl")
        this.includePrefix = prefix
    }


    /** 
    * main() method expects four arguments: 
    */
    public static void main(String[] args) 
    throws Exception {

        switch (args.size()) {
            case 0:
                throw new Exception("main method requires three parameters.")
            System.exit(-1)
            break

            case 4:
                try {
                
                File inv = new File(args[0])
		String invSchema = args[1]
                File dataDir = new File(args[2])
                File outDir = new File(args[3])
                if (! outDir.exists()) {
                    outDir.mkdir()
                }

		println "USE URL " + invSchema
		try {
		  URL u = new URL(invSchema)
		} catch (Exception e) {
		  println "Couldn't make url from " + invSchema
		}

                boolean prefix = false
                HmtCollectionTurtleizer pcttl = new HmtCollectionTurtleizer(inv, invSchema, dataDir, outDir, prefix)
                pcttl.generateTurtle()

            } catch (Exception e) {
                throw e
            }
            break


            default:
            break
        }
    }


    void generateTurtle() {
        CollectionArchive cc = new CollectionArchive(this.collectionInventory, this.inventorySource, this.dataDirectory)

        cc.ttl(turtleOutput, includePrefix)
    }

}
