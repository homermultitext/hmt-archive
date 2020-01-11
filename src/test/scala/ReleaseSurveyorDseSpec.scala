package org.homermultitext.hmtcexbuilder
import org.scalatest.FlatSpec
import scala.xml._
import java.io.File
import edu.holycross.shot.cite._
import edu.holycross.shot.cex._
import edu.holycross.shot.scm._

class ReleaseSurveyorDseSpec extends FlatSpec {

  val tinyCex = "src/test/resources/hmt-tiny.cex"
  //val tinyCex = "src/test/resources/hmt-rc-A.cex"
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

  "A ReleaseSurveyor" should "report on DSE collections" in {
    val surveyor = ReleaseSurveyor(lib,rootDir,releaseId)
    surveyor.dseOverview( new File(surveyor.releaseDir, "dse"), 6, 250)
  }



}
