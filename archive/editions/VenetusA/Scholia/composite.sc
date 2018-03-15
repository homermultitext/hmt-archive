import scala.xml._
import java.io.File

// Find unique set of scholia groups in a bunch of XML files.
def scholiaSet(dir: String = "./"): Set[String] = {
  val libraryDir = new File(dir)
  val fileVector = libraryDir.listFiles.filter(_.isFile).toVector
  val xmlFiles = fileVector.filter(_.getName.endsWith("xml"))
  val allGroups = for (f <- xmlFiles) yield {
    println("Parse " + f + "...")
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
    println("Parse " + f + "...")
    val root = XML.loadFile(f)
    val grp = root \ "text" \ "group"
    val bookOpen = s"""<div n="${grp(0).attribute("n").get}">"""
    println ("OPEN: " + bookOpen)
  }

}
