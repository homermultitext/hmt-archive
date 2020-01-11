package org.homermultitext.hmtcexbuilder
import org.scalatest.FlatSpec
import scala.xml._
import java.io.File
import edu.holycross.shot.cex._

class XmlParsingSpec extends FlatSpec {

  "The DataCollector object" should "collect text content of a parsed xml node.  NO, IT SHOULDN'T" in  pending /* {
    val tinyNode = XML.loadString("<root>Text message</root>")
    assert (DataCollector.collectXmlText(tinyNode) == "Text message")
  }

  it should "collect text content from a node with multiple levels of markup" in {
    val twoTier = "<root>Text <subElement>message</subElement></root>"
      val tinyNode = XML.loadString(twoTier)
      assert (DataCollector.collectXmlText(tinyNode) == "Text message")

  }

  it should "collect XML text content from a well-formed text fragment" in {
      val twoTier = "<root>Text <subElement>message</subElement></root>"
      assert (DataCollector.collectXmlText(twoTier)  == "Text message")
  }

*/


}
