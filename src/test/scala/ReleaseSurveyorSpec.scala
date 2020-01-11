package org.homermultitext.hmtcexbuilder
import org.scalatest.FlatSpec
import scala.xml._
import java.io.File
import edu.holycross.shot.cite._
import edu.holycross.shot.cex._
import edu.holycross.shot.scm._

class ReleaseSurveyorSpec extends FlatSpec {

  val tinyCex = "src/test/resources/hmt-tiny.cex"
  //val tinyCex = "src/test/resources/hmt-test.cex"
  val lib = CiteLibrarySource.fromFile(tinyCex)

  val rootDir = "src/test/resources"
  val releaseId = "test-release"

  def tidy(surveyor: ReleaseSurveyor) = {
    val directories =  surveyor.dirMap
    for (d <- directories.keySet) {
      val subdir = directories(d)
      assert(subdir.exists)
      println(s"${subdir} exists.")
      // and tidy up
      for (f <- DataCollector.filesInDir(subdir, "md")) {
        f.delete
      }
      subdir.delete
    }
    surveyor.releaseDir.delete
  }

  "A ReleaseSurveyor" should "set up a set of directories for reports" in {
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    val directories =  surveyor.dirMap

    val expectedKeys = Set("texts","tbs", "images", "dse")
    assert(directories.keySet == expectedKeys)
    //tidy(surveyor)
  }
  it should "compose a home page for the report" in {
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    surveyor.overview(2, 400)
  }
  it should "report on binary image collections" in {
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    surveyor.imageOverview( new File(surveyor.releaseDir, "images"), 2, 400)
  }


  it should "report on TBS collections" in {
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    surveyor.tbsOverview( new File(surveyor.releaseDir, "codices-papyri"), 6, 250)
  }

/*
  it should "report on DSE collections" in {
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    surveyor.dseOverview( new File(surveyor.releaseDir, "dse"), 6, 250)
  }
*/
  it should "produce an IIPSrv url for an image" in {
    val img = Cite2Urn("urn:cite2:hmt:vaimg.2017a:VA083RN_0084")
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    val expected = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom//hmt/vaimg/2017a/VA083RN_0084.tif&WID=1000&CVT=JPEG"

    assert(surveyor.iipSrvUrl(img) == expected)
  }

  it should "include RoI in an IIPSrvUrl if included in URN" in {
    val img = Cite2Urn("urn:cite2:hmt:vaimg.2017a:VA083RN_0084@0.1107,0.3552,0.05651,0.04688")
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    val expected = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom//hmt/vaimg/2017a/VA083RN_0084.tif&RGN=0.1107,0.3552,0.05651,0.04688&WID=1000&CVT=JPEG"
    assert(surveyor.iipSrvUrl(img) == expected)
  }

  it should "make it easy to tidy up after all these tests :-)" in {
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    //tidy(surveyor)
  }

  it should "write a rational home page" in {
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    println(surveyor.homePage)
  }




}
