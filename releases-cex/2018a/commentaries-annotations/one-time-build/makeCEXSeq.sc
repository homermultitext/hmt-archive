val f = "va-source.cex"

import scala.io.Source
import edu.holycross.shot.cite._

val lines = Source.fromFile(f).getLines.toVector.tail

val baseUrn = "urn:cite2:hmt:va_signs.v1.urn:cs"
val records = for ((txt, n) <- lines.zipWithIndex) yield {
    val cols = txt.split("#")
    val psg = CtsUrn(cols(0))
    val csign = Cite2Urn(cols(1))
    s"${baseUrn}${n}#${csign.objectComponent} on Iliad ${psg.passageComponent}#${psg}#${csign}#${n}"
}



import java.io.PrintWriter


val hdr = """
#!citedata
urn#label#passage#critsign#sequence
"""


new PrintWriter("va_criticalsigns.cex") { write(hdr + records.mkString("\n")); close }
