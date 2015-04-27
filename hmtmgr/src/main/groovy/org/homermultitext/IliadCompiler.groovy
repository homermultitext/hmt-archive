package org.homermultitext

import edu.harvard.chs.f1k.GreekNode
import org.apache.commons.io.FilenameUtils

/** Class for creating composite editions out of various 
* source files manually edited by HMT contributors.
*/
class IliadCompiler {

    /** Directory containing TEI XML files, named
    * following HMT project conventions.
    */
    File xmlSrcDir

    /** File containing text of TEI header element to use. */
    File headerFile

    /** Writable directory where composite edition will be created.
    */
    File outputDir

    /** Resulting file with Iliad edition of Venetus A. */
    File compositeIliad

    /** Namespace object for parsing TEI documents. */
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")


    /** Constructor using a readable directory with XML source files, a
    * a TEI header to insert, and a writable directory for output.
    */
    IliadCompiler(File srcDir, File teiHeaderFile, File outDir) {
        this.xmlSrcDir = srcDir
        this.headerFile = teiHeaderFile
        this.outputDir = outDir
        String outFileName = FilenameUtils.getName(headerFile.getAbsoluteFile().toString())
        this.compositeIliad = new File(outputDir,outFileName)
    }


    /** Determines whether resulting Iliad text validates.
    * @returns true if XML parses syntactically.
    */
    boolean compilationParses() {
        try {
            groovy.util.Node root = new XmlParser().parse(compositeIliad)
            System.err.println "${compositeIliad} validates syntactically"
            return true
        } catch (Exception e) {
            System.err.println "Error parsing ${compositeIliad}:  ${e}"
            return false
        }
    }

    /** Compiles an Iliad text.
    */
    void compileTexts() {
        compositeIliad.setText("")
        groovy.util.Node hdrRoot = new XmlParser().parse(headerFile)
        groovy.util.Node hdrElem = hdrRoot[tei.teiHeader][0]
        GreekNode gn = new GreekNode(hdrElem)
        compositeIliad.append('<?xml version="1.0" encoding="UTF-8"?>\n', "UTF-8")
        compositeIliad.append('<TEI  xmlns="http://www.tei-c.org/ns/1.0">\n', "UTF-8")
        compositeIliad.append(gn.toXml() + "\n", "UTF-8")
        compositeIliad.append("<text>\n<body>", "UTF-8")


        def fileNames = []
        xmlSrcDir.eachFileMatch(~/.*\.xml/) { fileName -> 
            fileNames.add(fileName) 
        }

        fileNames.sort().each { srcFile ->
            try {
                def fRoot = new XmlParser().parse(srcFile)
                GreekNode iliadDiv = new GreekNode(fRoot[tei.text][tei.body][tei.div][0])
                compositeIliad.append(iliadDiv.toXml(), "UTF-8")
            } catch (Exception e) {
                System.err.println "Could not parse or process ${srcFile}!"
                System.err.println "Exception: ${e}"
            }
        }
        compositeIliad.append("</body>\n</text>\n</TEI>", "UTF-8")
    }


    /** Creates an IliadCompiler object and compiles Iliad files. 
    */
    public static void main(String[] args) 
    throws Exception {
        if (args.size() != 3) {
            System.err.println "usage: IliadCompiler SRCDIR TEIHEADERFILE OUTPUTDIR"
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
            System.err.println "IliadCompilermai n method: Bad param or params: ${args}"
            throw e
        }

        IliadCompiler iliad = new IliadCompiler(src,teiHeader,outputDir)
        iliad.compileTexts()
        if (iliad.compilationParses()) {
            System.err.println "IliadCompiler: composite text ${iliad.compositeIliad} parses"
        } else {
            System.err.println "IliadCompiler:  could not parse composite text ${iliad.compositeIliad} parses"
        }
    }
  
}
