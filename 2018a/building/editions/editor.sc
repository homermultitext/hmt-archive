import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import org.homermultitext.edmodel._
import java.io.PrintWriter

val iliad = "src/va-iliad-1-12.xml"
val catalog = "src/ctscatalog.cex"
val citation = "src/citationconfig.cex"

val repo = TextRepositorySource.fromFiles(catalog,citation,"src")

new PrintWriter("va_iliad_xml.cex") { write(repo.cex("#"));close }


val tokens = TeiReader.fromCorpus(repo.corpus)

val diplIliad = DiplomaticEditionFactory.corpusFromTokens(tokens)



// MOVE THESE TWO TO OHCO2
def flattenIt(v: Vector[(String, edu.holycross.shot.ohco2.CitableNode, Int)], newVersion : String) = {
  val psg = v(0)._1
  val seq = v(0)._3


  val urn = CtsUrn(s"${v(0)._2.urn.dropPassage.addVersion(newVersion)}${psg}")
  val cnodes = v.map(_._2)
  (seq, CitableNode(urn,   cnodes.map(_.text).mkString(" ")))
}

def exemplarToVersion(c: Corpus, newVersionId: String) = {
  val zipped = diplIliad.nodes.zipWithIndex
  val triple = zipped.map{
    case (cn,i) => (cn.urn.passageComponent,cn,i)
  }
  val reduced = triple.map {
    case (s,cn,i) => (s.split("[.]").dropRight(1).mkString("."),cn,i)
  }
  val grouped = reduced.groupBy(_._1).values.toVector
  Corpus(grouped.map(
    flattenIt(_, newVersionId)).sortBy(_._1).map( _._2)
    )
}


val diplIliadByLine = exemplarToVersion(diplIliad, "msA")

val diplHeader = "\n\n#!ctscatalog\nurn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang\nurn:cts:greekLit:tlg0012.tlg001.msA:#book,line#Homeric epic#Iliad#HMT project diplomatic edition##true#grc\n\n#!ctsdata\n"

new PrintWriter("va_iliad_diplomatic.cex") { write(diplHeader + diplIliadByLine.cex("#"));close }
