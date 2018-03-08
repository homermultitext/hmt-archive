import scala.io.Source
import edu.holycross.shot.cite._

val invFile = "va-scholia-inv.cex"
val invLines = Source.fromFile(invFile).getLines.toVector.tail

val content = for (l <- invLines) yield {
  val cols = l.split("#")
  val txt = CtsUrn(cols(0))
  val label = cols(1)
  val img = Cite2Urn(cols(2))
  val surface = Cite2Urn(cols(3))

  s"DSE record for scholion ${txt.work} ${txt.passageComponent}#${txt}#${img}#${surface}"
}


val recordBase = "urn:cite2:hmt:va_dse.v1:schol"

val records = for ((txt, n) <- content.zipWithIndex) yield {
  s"${recordBase}${n}#${txt}"
}

import java.io.PrintWriter


val hdr = """
#!citedata
urn#label#passage#imageroi#surface
"""


new PrintWriter("scholia-1-dse.cex") { write(hdr + records.mkString("\n")); close }
