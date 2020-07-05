package org.homermultitext.hmtcexbuilder

import edu.holycross.shot.cex._
import scala.xml._
import java.io.File
import java.io.PrintWriter

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** Factory for creating CEX composite texts of Iliads
* from multiple XML source files in a given directory.
*
* @param srcDir Source directory with archival XML files.
*/
object IliadComposite extends LogSupport {

  /** Extract a given Iliad document from a set of XML
  * source files and create a single composite text (as a String).
  *
  * @param document Document identifier for scholia document (work ID in the document's CTS URN).
  * @param files XML files to extract content from.
  */
  def compositeDocument(files: Vector[File]): String = {
    val lines = for (f <- files) yield {
      val root = XML.loadFile(f)
      val teiDiv = root \ "text" \ "body" \ "div"
      val bookNode = teiDiv(0)
      val book = bookNode.attribute("n").get
      val bookOpen = s"""<div n="${book}" type="book">"""
      val lines = bookNode \ "l"
      info("In book " + book + s", ${lines.size} lines.")
      val iliadStrings = lines.map(_.toString)
      bookOpen + iliadStrings.mkString("\n") + "</div>"
    }
    lines.mkString("\n")
  }


  /** Write composite CEX files for set of XML source files.
  *
  * @param inputDir Name of directory with XML source files.
  * @param outputDir Name of directory where CEX files should be written.
  * @param fileNameBase Base for individual file names.  Final file name will
  * be this string + "_" + the scholion identifier + ".xml".
  */
  def composite(inputDir: String,
    outputDir: String,
    fileNameBase: String = "va_iliad") = {

    val cexDir = new File(outputDir)

    val xmlFiles = DataCollector.filesInDir(inputDir, "xml")

    val content = compositeDocument(xmlFiles)
    val outputFile = new File(cexDir, s"${fileNameBase}.xml")
    new PrintWriter(outputFile) {write(docOpen + content + docClose); close}
  }



  /** Closing statement for TEI wrapping elements.*/
  val docClose = "   </body>\n  </text>\n</TEI>"


  /** Opening statement for TEI wrapping elements.*/
  val docOpen = """<?xml version="1.0" encoding="utf-8"?>
<TEI xmlns="http://www.tei-c.org/ns/1.0">
   <teiHeader>
      <fileDesc>
         <titleStmt>
            <title>Composite text of HMT XML edition of Iliad</title>
         </titleStmt>
         <publicationStmt>
            <p>Unpublished</p>
         </publicationStmt>
         <sourceDesc>
            <p>Transcribed directly from photograhy of MS source</p>
         </sourceDesc>
      </fileDesc>
   </teiHeader>
   <text xml:lang="grc">
     <body>
"""
}
