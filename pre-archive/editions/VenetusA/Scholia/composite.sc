import scala.xml._
import java.io.File
import java.io.PrintWriter

// Find unique set of scholia groups in a bunch of XML files.
def scholiaSet(dir: String = "./"): Set[String] = {
  val libraryDir = new File(dir)
  val fileVector = libraryDir.listFiles.filter(_.isFile).toVector
  val xmlFiles = fileVector.filter(_.getName.endsWith("xml"))
  val allGroups = for (f <- xmlFiles) yield {
    val root = XML.loadFile(f)
    val groups = root \ "text" \ "group" \ "text"
    groups
  }
  allGroups.flatten.map(_.attribute("n").get.text).toSet
}


def composite(document : String, files: Vector[File]): String = {
  println("Extract " + document)
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
      println("\t" + scholia.size + " scholia.")
      val scholStrings = scholia.map(_.toString)
      bookOpen + scholStrings.mkString("\n") + "</div>"
    } else { "" }
  }
  lines.mkString("\n")
}

def compositeAll(dir: String = "./") = {

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
              <p>Transcribed from Venetus A MS</p>
           </sourceDesc>
        </fileDesc>
     </teiHeader>
     <text xml:lang="grc"><body>

"""

  val docClose = "</body></text></TEI>"
  val scholiaGroups = scholiaSet(dir)
  val libraryDir = new File(dir)
  val fileVector = libraryDir.listFiles.filter(_.isFile).toVector
  val xmlFiles = fileVector.filter(_.getName.endsWith("xml"))

  println(s"Creating composites for ${scholiaGroups.size} texts ")
  for (s <- scholiaGroups) {
    val content = composite(s, xmlFiles)
    new PrintWriter(s"va_composite_scholia_${s}.xml") {write(docOpen + content + docClose); close}
  }
}
