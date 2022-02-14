
import scala.xml.XML
import java.io.File

val baseDir = "archive/editions/VenetusA/Scholia/"


def listFiles(dir: String): Vector[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      val realFiles =  d.listFiles.filter(_.isFile).toVector
      realFiles.filter(_.getName.matches(".+xml"))
    } else {
        Vector[File]()
    }
}


def stringPairing(baseDir : String): String = {

  val builder = StringBuilder.newBuilder

  for (f <- listFiles(baseDir) ) {

      //val xml = XML.loadFile(s"${baseDir}${f}")
      val xml = XML.loadFile(f)
      val bookElem = xml \ "text" \ "group"
      val book = bookElem(0) \ "@n"

      val docElem = bookElem \ "text"
      val doc = docElem(0) \ "@n"

      val scholia = docElem \ "body" \ "div"

      for (s <- scholia) {
        val n = s \ "@n"
        val urnVal = s"urn:cts:greekLit:tlg5026.${doc}:${book}.${n}"
        val divv = s \ "div"
        val ref = divv(1) \ "p"
        builder.append(urnVal + "#" + ref.text + "\n")
      }
    }
  builder.toString
}
