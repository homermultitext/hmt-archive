import scala.io.Source
import edu.holycross.shot.cite._

val imgReff = "venA-Iliad-1-image.cex"
val surfaceReff = "venA-Iliad-surface.cex"

val imgLines = Source.fromFile(imgReff).getLines.toVector

val imgPairs = for (l <- imgLines) yield {
  val cols = l.split("#")
  val txt = CtsUrn(cols(0))
  val img = Cite2Urn(cols(1))

  (cols(0) -> cols(1))
}
val imgMap = imgPairs.toMap

val surfaceLines = Source.fromFile(surfaceReff).getLines.toVector
val surfacePairs = for (c <- surfaceLines) yield {
  val cols = c.split("#")
  val txt = CtsUrn(cols(0))
  val img = Cite2Urn(cols(1))
  (cols(0) -> cols(1))
}
val surfaceMap = surfacePairs.toMap




val recordBase = "urn:cite2:hmt:va_dse.v1:il"
val txtLines = for (psg <- imgMap.keySet) yield {
  s"${psg}#${imgMap(psg)}#${surfaceMap(psg)}"
}

val records = for ((txt, n) <- txtLines.zipWithIndex) yield {
  val cols = txt.split("#")
  val u = CtsUrn(cols(0))
  val label = s"DSE record for Iliad ${u.passageComponent}"
  s"${recordBase}${n}#${label}#${txt}"

}

import java.io.PrintWriter


val hdr = """
#!citecollections
URN#Description#Labelling property#Ordering property#License
urn:cite2:hmt:va_dse.v1:#DSE model of HMT editions#urn:cite2:hmt:va_dse.v1.label:##CC-attribution-share-alike


#!citeproperties
Property#Label#Type#Authority list
urn:cite2:hmt:va_dse.v1.urn:#DSE record#Cite2Urn#
urn:cite2:hmt:va_dse.v1.label:#Label#String#
urn:cite2:hmt:va_dse.v1.passage:#Text passage#CtsUrn#
urn:cite2:hmt:va_dse.v1.imageroi:#Image region of interest#Cite2Urn#
urn:cite2:hmt:va_dse.v1.surface:#Artifact surface#Cite2Urn#

#!datamodels
Collection#Model#Label#Description
urn:cite2:hmt:va_dse.v1:#urn:cite2:cite:datamodels.v1:dse#DSE model#Diplomatic Scholarly Edition (DSE) model.  See documentation at <https://github.com/cite-architecture/dse>.


#!citedata
urn#label#passage#imageroi#surface
"""


new PrintWriter("iliad-1-dse.cex") { write(hdr + records.mkString("\n")); close }
