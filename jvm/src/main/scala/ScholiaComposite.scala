package org.homermultitext.hmtcexbuilder

import edu.holycross.shot.cex._

import scala.xml._
import java.io.File
import java.io.PrintWriter


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** Factory for creating CEX composite texts of scholia from
* multiple XML source files in a given directory.
*
* @param srcDir Source directory with archival XML files..
*/
object ScholiaComposite extends LogSupport {


  /** Find set of text groups represented in XML
  * source files in a given directory.
  *
  * @param srcDir Directory containing XML editions of scholia.
  */
  def scholiaSet(srcDir: String): Set[String]  = {
    val xmlSource = DataCollector.filesInDir(srcDir, "xml")
    val allGroups = for (f <- xmlSource) yield {
      val root = XML.loadFile(f)
      val groups = root \ "text" \ "group" \ "text"
      groups
    }
    allGroups.flatten.map(_.attribute("n").get.text).toSet
  }


  /** Extract a given scholia document from a set of XML
  * source files and create a single composite text (as a String).
  *
  * @param document Document identifier for scholia document (work ID in the document's CTS URN).
  * @param files XML files to extract content from.
  */
  def compositeDocument(document : String, files: Vector[File]): String = {
    val lines = for (f <- files) yield {
      val root = XML.loadFile(f)
      val teiGroup = root \ "text" \ "group"
      val bkNode = teiGroup(0)
      val book = bkNode.attribute("n").get
      val bookOpen = s"""<div n="${book}" type="book">"""

      val allDocs = bkNode \ "text"
      val relevant = allDocs.filter(_.attribute("n").get.text == document)
      if (relevant.size > 0) {
        val doc = relevant(0)
        val scholia = doc \ "body" \ "div"
        info("\tIn " + document + ", book " + book  +": " + scholia.size + " scholia.")
        val scholStrings = scholia.map(_.toString)
        bookOpen + scholStrings.mkString("\n") + "</div>"
      } else { "" }
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
    fileNameBase: String = "va_composite_scholia") = {
    //  ) = {

    val cexDir = new File(outputDir)

    val scholiaGroups = scholiaSet(inputDir)
    val xmlFiles = DataCollector.filesInDir(inputDir, "xml")
    for (s <- scholiaGroups) {
      val content = compositeDocument(s, xmlFiles)
      val outputFile = new File(cexDir, s"${fileNameBase}_${s}.xml")
      new PrintWriter(outputFile) {write(docOpen + content + docClose); close}
    }
  }



  /** Closing statement for TEI wrapping elements.*/
  val docClose = "   </body>\n  </text>\n</TEI>"


  /** Opening statement for TEI wrapping elements.*/
  val docOpen = """<?xml version="1.0" encoding="utf-8"?>
<TEI xmlns="http://www.tei-c.org/ns/1.0">
   <teiHeader>
      <fileDesc>
         <titleStmt>
            <title>Composite text of HMT XML edition of scholia</title>
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
