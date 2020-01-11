package org.homermultitext.hmtcexbuilder

import org.scalatest.FlatSpec
import java.io.File
import scala.xml._
import edu.holycross.shot.cex._
class ScholiaCompositeSpec extends FlatSpec {

  "The ScholiaComposite object" should "find unique scholia groups in a set of files" in {
    val scholiaSrc = "src/test/resources/scholia-xml"
    val expected = Set("msA",
    "msAextra", "msAext", "msAil", "msAint", "msAim", "msAlater")
    assert(ScholiaComposite.scholiaSet(scholiaSrc) == expected)
  }

  it should "extract text content for a given single document" in {
    val doc = "msAextra"
    val srcDir = "src/test/resources/scholia-xml"
    val xmlFiles = DataCollector.filesInDir(srcDir, "xml")
    val msAextraText = ScholiaComposite.compositeDocument(doc, xmlFiles)
    // this results in XML organized by book.  Put a wrapper element around it
    // to form valid XML.
    val root = XML.loadString("<root>" + msAextraText + "</root>")
    // should produce two scholia in book 1 and one in book 2.
    val books = root \ "div"
    assert(books.size == 2)
    val book1 = books(0)
    val scholia1 = book1 \ "div"
    assert (scholia1.size == 2)
    val scholia2 = books(1)
    assert (scholia2.size == 1)
  }


  it should "write full-blown CEX serialization of scholia content"  in {
    val srcDir = "src/test/resources/scholia-xml"
    val outDir = "src/test/resources/scholia-composites"
    ScholiaComposite.composite(srcDir, outDir)
    val expectedOutput = Vector(
      new File ("src/test/resources/scholia-composites/va_composite_scholia_msA.xml"),
      new File ("src/test/resources/scholia-composites/va_composite_scholia_msAext.xml"),
      new File ("src/test/resources/scholia-composites/va_composite_scholia_msAextra.xml"),
      new File ("src/test/resources/scholia-composites/va_composite_scholia_msAil.xml"),
      new File ("src/test/resources/scholia-composites/va_composite_scholia_msAim.xml"),
      new File ("src/test/resources/scholia-composites/va_composite_scholia_msAint.xml"),
      new File ("src/test/resources/scholia-composites/va_composite_scholia_msAlater.xml")
    )
    val actualOutput = DataCollector.filesInDir(outDir, "xml")
    assert(actualOutput == expectedOutput)

    //tidy up:
    for (f <- actualOutput.toSeq) {
      f.delete()
    }
  }
}
