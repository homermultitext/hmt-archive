package org.homermultitext.hmtcexbuilder
import edu.holycross.shot.cex._
import org.scalatest.FlatSpec
import java.io.File
import scala.xml._

class IliadCompositeSpec extends FlatSpec {


  val srcDir = "src/test/resources/iliad-xml"
  val outDir = "src/test/resources/iliad-composites"


  "The IliadComposite object" should "extract text content in boks in a single text" in {

    val xmlFiles = DataCollector.filesInDir(srcDir,"xml")

    val iliadText = IliadComposite.compositeDocument(xmlFiles)

    // this results in XML organized by book.  Put a wrapper element around it
    // to form valid XML.
    val root = XML.loadString("<root>" + iliadText + "</root>")
    // should produce three lines each from book 1 and book 2.
    val books = root \ "div"
    assert(books.size == 2)
    val book1 = books(0)
    val lines1 = book1 \ "l"
    assert (lines1.size == 3)


    val book2 = books(1)
    val lines2 = book2 \ "l"
    assert (lines2.size == 3)
  }


  it should "write full-blown CEX serialization of iliad content"  in {
    val srcDir = "src/test/resources/iliad-xml"
    val outDir = "src/test/resources/iliad-composites"
    IliadComposite.composite(srcDir, outDir)
    val expectedOutput = Vector(
      new File ("src/test/resources/iliad-composites/va_iliad.xml")
    )
    val actualOutput = DataCollector.filesInDir(outDir, "xml")
    assert(actualOutput == expectedOutput)

    //tidy up:
    for (f <- actualOutput.toSeq) {
      f.delete()
    }
  }
}
