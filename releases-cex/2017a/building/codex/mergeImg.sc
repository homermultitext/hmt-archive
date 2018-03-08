import scala.io.Source

val cex1 = "vapages.cex"
val index = "index.tsv"

val cex = Source.fromFile(cex1).getLines.toVector

/*
val urnPaired = for (c <- cex.tail) yield {
  val cols = c.split("#")
  (cols(1), c)
}
val cexIndex = urnPaired.toMap
*/


val imgLines = Source.fromFile(index).getLines.toVector
val imgPairs = for (l <- imgLines) yield {
  val cols = l.split("\t")
  (cols(0) -> cols(1))
}
val imgMap = imgPairs.toMap

val revisedCex = for (c <- cex.tail) yield {
  val cols = c.split("#")
  val urn = cols(1)
  c + "#" + imgMap(urn)
}



import java.io.PrintWriter

new PrintWriter("revised-cex.cex") { write(cex(0) + "\n" + revisedCex.mkString("\n") + "\n"); close }
