package org.homermultitext


import edu.harvard.chs.f1k.GreekNode
import org.apache.commons.io.FilenameUtils


/** Class for creating composite editions out of various 
* source files manually edited by HMT contributors.
*/
class ScholiaCompiler {

    /** Directory containing TEI XML files, named
    * following HMT project conventions.
    */
    File xmlSourceDirectory

    /** File containing text of TEI header element to use. */
    File headerFile

    /** Writable directory where composite edition will be created.
    */
    File outputDirectory

    /** Siglum for one document of scholia. */
    String siglum

    /** Resulting edition of scholia. */
    File scholiaEdition

    /** Verbosity level 0-3 of debugging output */
    Integer debug = 1

    /** Namespace object for TEI */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    ScholiaCompiler(File inputDir, File header, File outDir, String scholiaDoc) {
        this.xmlSourceDirectory = inputDir        
        this.headerFile = header
        this.outputDirectory = outDir
        this.siglum = scholiaDoc
        System.err.println "Find scholia for " + siglum
    }



    void compileTexts() {
        groovy.util.Node hdrRoot = new XmlParser().parse(headerFile)
        def hdrElem = hdrRoot[tei.teiHeader][0]
        GreekNode gn = new GreekNode(hdrElem)

        String scholiaFileName = FilenameUtils.getName(headerFile.getAbsoluteFile().toString())

        System.err.println "Writing scholia edition " + scholiaFileName
        scholiaEdition = new File(outputDirectory,scholiaFileName)
        scholiaEdition.setText("")


        scholiaEdition.append('<?xml version="1.0" encoding="UTF-8"?>\n', "UTF-8")
        scholiaEdition.append('<TEI  xmlns="http://www.tei-c.org/ns/1.0">\n', "UTF-8")
        scholiaEdition.append(gn.toXml() + "\n", "UTF-8")
        scholiaEdition.append("<text>\n<body>\n", "UTF-8")


        def fileNames = []

        xmlSourceDirectory.eachFileMatch(~/.*\.xml/) { fileName ->
            fileNames.add(fileName)
        }

        fileNames.sort().each { srcFile -> 
            System.err.println "Process file " + srcFile
            System.err.println "And write output to " + scholiaEdition
            try {

                groovy.util.Node fRoot = new XmlParser().parse(srcFile)
                groovy.util.Node groupNode = fRoot[tei.text][tei.group][0]
                String bk = groupNode.'@n'

                groupNode[tei.text].each { txt ->
                    if (txt.'@n' == siglum) {
                        scholiaEdition.append("<div n='" + bk +"'>\n" , "UTF-8")
                        if (debug) {System.err.println "Found section ${siglum} in book ${bk}"}

                        txt[tei.body][tei.div].each { schol ->
                            scholiaEdition.append("<div type='scholion' n='" + schol.'@n' + "'>\n", "UTF-8")

                            schol[tei.div].each { d ->

                                switch (d.'@type') {
                                    case "ref":
                                        //do nothing 
                                        break
                                    default :
                                        GreekNode divNode = new GreekNode(d)
                                    scholiaEdition.append(divNode.toXml(), "UTF-8")
                                    break
                                    
                                }
                            }
                            scholiaEdition.append("\n</div>\n\n" , "UTF-8")
                        }
                        // end of book:
                        scholiaEdition.append("\n</div>\n\n" , "UTF-8")
                    }
                }

            } catch (Exception e) {
                System.err.println "Could not parse or process ${srcFile}!"
                System.err.println "Exception: ${e}"
            }
        }
        scholiaEdition.append("</body>\n</text>\n</TEI>", "UTF-8")
    }

    /** Determines whether resulting scholia text validates.
    * @returns true if XML parses syntactically.
    */
    boolean compilationParses() {
        try {
            groovy.util.Node root = new XmlParser().parse(scholiaEdition)
            System.err.println "${scholiaEdition} validates syntactically"
            return true
        } catch (Exception e) {
            System.err.println "Error parsing ${scholiaEdition}:  ${e}"
            return false
        }
    }





    /** Creates a ScholiaCompiler object and compiles editions of scholia. 
    */
    public static void main(String[] args) 
    throws Exception {
        if (args.size() != 4) {
            System.err.println "usage: ScholiaCompiler SRCDIR TEIHEADERFILE OUTPUTDIRSIGLUM "
            System.exit(-1)
        }
        
        File src
        File teiHeader
        File outputDir 

        try {
            src = new File (args[0])
            teiHeader =  new File(args[1])
            outputDir = new File(args[2])

            if (! outputDir.exists()) {
                outputDir.mkdir()
            }
        } catch (Exception e) {
            System.err.println "ScholiaCompiler main method: Bad param or params: ${args}"
            throw e
        }

        ScholiaCompiler sc = new ScholiaCompiler(src,teiHeader,outputDir, args[3])
        sc.compileTexts()
        if (sc.compilationParses()) {
            System.err.println "ScholiaCompiler: composite text ${sc.scholiaEdition} parses"
        } else {
            System.err.println "ScholiaCompiler:  could not parse composite text ${sc.scholiaEdition} parses"
        }

    }

}
