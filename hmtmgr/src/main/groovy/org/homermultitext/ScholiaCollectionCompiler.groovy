package org.homermultitext


import edu.harvard.chs.f1k.GreekNode
import org.apache.commons.io.FilenameUtils
import edu.harvard.chs.cite.CtsUrn

/** Class for creating composite CITE collection out of various 
* source .tsv files.
*/
class ScholiaCollectionCompiler {

  File outputDir
  File srcDir


  
  ScholiaCollectionCompiler(File inDir, File outDir) {
    this.srcDir = inDir
    this.outputDir = outDir
  }


  public static void main(String[] args) 
  throws Exception {
    if (args.size() != 2) {
      System.err.println "usage: ScholiaCompiler SRCDIR  OUTPUTDIR "
      System.exit(-1)
    }
      File src
      File outputDir 
	
      try {
	src = new File (args[0])
	outputDir = new File(args[1])

	if (! outputDir.exists()) {
	  outputDir.mkdir()
	}
      } catch (Exception e) {
	System.err.println "ScholiaCollectionCompiler main method: Bad param or params: ${args}"
	throw e
      }


      ScholiaCollectionCompiler scc = new ScholiaCollectionCompiler(src,outputDir)
      scc.compileCollection()
  }


  void compileCollection() {
    File outFile = new File(outputDir, "scholiaInventory.csv")
    outFile.setText("CtsUrn,Comments,VisualEvidence,FolioUrn\n")
  
  
    srcDir.eachFileMatch(~/.*.csv/) { csv ->  
      Integer lineCount = 0
      csv.eachLine { ln ->
	if (lineCount > 0) {
	  String cts = ln.replaceFirst(/,.+/, '')
	  String noCts = ln.replaceFirst(/^[^,]+,/,'') 
	  CtsUrn urn
	  try {
	    urn = new CtsUrn(cts.replaceAll(/["]/,''))
	  } catch (Exception e) {
	    System.err.println "Exception in scholia inventory for line " + ln
	    throw e
	  }
	  String psg = urn.getRef().replaceAll(/[.]/,"_")
	  String citeStr = "urn:cite:hmt:scholia.${urn.getWork()}_${psg}"
	  outFile.append "${citeStr},${urn},${noCts}\n"
	}
	lineCount++;
      }
    }
  }


}
