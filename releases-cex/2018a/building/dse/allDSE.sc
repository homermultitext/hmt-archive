import scala.io.Source
import edu.holycross.shot.cite._

val ilImgReff = "iliad-source/venA-Iliad-1-image.cex"
val ilSurfaceReff = "iliad-source/venA-Iliad-surface.cex"

val ilImgLines = Source.fromFile(ilImgReff).getLines.toVector

val ilImgPairs = for (l <- ilImgLines) yield {
  val cols = l.split("#")
  val txt = CtsUrn(cols(0))
  val img = Cite2Urn(cols(1))

  (cols(0) -> cols(1))
}
val ilImgMap = ilImgPairs.toMap

val ilSurfaceLines = Source.fromFile(ilSurfaceReff).getLines.toVector
val ilSurfacePairs = for (c <- ilSurfaceLines) yield {
  val cols = c.split("#")
  val txt = CtsUrn(cols(0))
  val img = Cite2Urn(cols(1))
  (cols(0) -> cols(1))
}
val ilSurfaceMap = ilSurfacePairs.toMap




val ilRecordBase = "urn:cite2:hmt:va_dse.v1:il"
val ilTxtLines = for (psg <- ilImgMap.keySet) yield {
  s"${psg}#${ilImgMap(psg)}#${ilSurfaceMap(psg)}"
}

val ilRecords = for ((txt, n) <- ilTxtLines.zipWithIndex) yield {
  val cols = txt.split("#")
  val u = CtsUrn(cols(0))
  val label = s"DSE record for Iliad ${u.passageComponent}"
  s"${ilRecordBase}${n}#${label}#${txt}"

}


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


val invFile = "scholia-inv/vascholia-1-12.cex"
val invLines = Source.fromFile(invFile).getLines.toVector

val schContent = for (l <- invLines) yield {
  println(l)
  val cols = l.split("#")
  val txt = CtsUrn(cols(0))
  val label = cols(1)
  val img = Cite2Urn(cols(2))
  val surface = Cite2Urn(cols(3))

  s"DSE record for scholion ${txt.work} ${txt.passageComponent}#${txt}#${img}#${surface}"
}


val schRecordBase = "urn:cite2:hmt:va_dse.v1:schol"

val schRecords = for ((txt, n) <- schContent.zipWithIndex) yield {
  s"${schRecordBase}${n}#${txt}"
}




///new PrintWriter("scholia-1-12-dse.cex") { write(hdr + records.mkString("\n")); close }


import java.io.PrintWriter
new PrintWriter("va-dse.cex") { write(hdr + ilRecords.mkString("\n") + schRecords.mkString("\n") ); close }
