/*
MOVE THIS INTO BUILD SCRIPT!

Groovy script to make composite scholia inventory from all csv files
in byBook directory.
Algorithmically generates scholion URN from CTS URN of edition as

urn:cite:hmt:scholia.
${work}_${bk}_${scholion}

*/


for (fName in this.args) {
  println "Process file: ${fName}"
  Integer lineCount = 0
  File f = new File(fName)
  f.eachLine { ln ->
    if (lineCount > 0) {
      def cols = ln.split(/,/)
      println cols[0]
      
    }
    lineCount++;
  }
}


