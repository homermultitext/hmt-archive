import scala.xml._
import java.io.File

// Find unique set of scholia groups in a bunch of XML files.
def scholiaSet(dir: String = "./"): Set[String] = {
  val libraryDir = new File(dir)
  val fileVector = libraryDir.listFiles.filter(_.isFile).toVector
  val xmlFiles = fileVector.filter(_.getName.endsWith("xml"))
  val allGroups = for (f <- xmlFiles) yield {
    //println("Parse " + f + "...")
    val root = XML.loadFile(f)
    val groups = root \ "text" \ "group" \ "text"
    println("Found " + groups.size + " scholia groups")
    groups
  }
  allGroups.flatten.map(_.attribute("n").get.text).toSet
}


def composite(dir: String = "./") = {
  val scholiaGroups = scholiaSet(dir)
  val libraryDir = new File(dir)
  val fileVector = libraryDir.listFiles.filter(_.isFile).toVector
  val xmlFiles = fileVector.filter(_.getName.endsWith("xml"))
  for (f <- xmlFiles) {
    
    val root = XML.loadFile(f)
    val teiGroup = root \ "text" \ "group"
    val bkNode = teiGroup(0)
    val book = bkNode.attribute("n").get
    val bookOpen = s"""<div n="${book}">"""

    val scholiaDocs = bkNode \ "text"
    for (doc <- scholiaDocs) {
      println(s"Book ${book}, shcolia " +  doc.attribute("n").get.text)
      val scholia = doc \ "body" \ "div"
      println("\t" + scholia.size + " scholia.")
    }
  }

}
