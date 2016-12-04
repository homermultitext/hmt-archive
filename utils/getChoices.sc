// read lines text as one unit of well-formed xml
// per line.  Find all child elements of `choice`


import scala.io.Source
import scala.xml._



def getChoiceName(oneChild: NodeSeq): String = {
  if (oneChild.size == 0) {
    ""
  } else {
    var buf = new StringBuilder
    val oneChoice = oneChild(0)
    oneChoice.label
  }
}


def isElem(n: Node) = {
 n match {
 case el: Elem => true
 case _ => false
 }
}

def extractChildNames(xml: String) = {
  val choiceEll = XML.loadString(xml) \\ "choice"
  val kids = choiceEll.flatMap(_.child)
  val elKids = kids.map { l => l.filter(isElem(_)) }.filter(_.size > 0)
  val kidNames = elKids.map(getChoiceName(_))
  //kidNames.map(_.mkString("_"))
  kidNames.sliding(2).toVector.map(_.mkString("_"))
}

@main
def choicesFromFile(f: String) = {
  val xmlLines =  scala.io.Source.fromFile(f).getLines.toVector

  val choicesInLine = xmlLines.flatMap(extractChildNames(_))

  for (c <- choicesInLine.distinct) {
    println(c)
  }
}
