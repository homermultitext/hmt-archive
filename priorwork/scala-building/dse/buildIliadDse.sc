import edu.holycross.shot.cite._
import scala.io.Source

val imgSrc =  "iliad-source/va-iliad2image-1-12.cex"
val surfaceSrc = "iliad-source/venA-Iliad-surface.cex"

val imgLines = Source.fromFile(imgSrc).getLines.toVector

val imgPairings = for (l <- imgLines)  yield {
  val parts = l.split("#")
  val txt = CtsUrn(parts(0))
  val img = Cite2Urn(parts(1))
  (txt, img)
}
val imgIndex = imgPairings.toMap

val urnBase = "urn:cite2:hmt:va_dse.v1:il"


val surfaceLines = Source.fromFile(surfaceSrc).getLines.toVector
val cex = for ((l, count) <- surfaceLines.zipWithIndex) yield {
  val urn = s"${urnBase}${count}"


  val parts = l.split("#")
  val txt = CtsUrn(parts(0))
  val surface = Cite2Urn(parts(1))


  val imgUrn = imgIndex.getOrElse(txt,None)
  imgUrn match {
    case img: Cite2Urn =>   s"${urn}#DSE record for Iliad ${txt.passageComponent}#${txt}#${imgUrn}#${surface}"
    case None => ""
  }
  //urn#label#passage#imageroi#surface
}
println(cex.filter(_.nonEmpty).mkString("\n"))


// autogenerate IURN and label.
