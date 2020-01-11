package org.homermultitext.hmtcexbuilder
import org.scalatest.FlatSpec
import scala.xml._
import java.io.File
import edu.holycross.shot.cite._

import edu.holycross.shot.scm._

class ReleaseSurveyorTextsSpec extends FlatSpec {

  val tinyCex = "src/test/resources/hmt-wtexts.cex"
  //val tinyCex = "src/test/resources/hmt-rc-A.cex"
  val lib = CiteLibrarySource.fromFile(tinyCex)

  val rootDir = "src/test/resources"
  val releaseId = "test-release"



  "A ReleaseSurveyor" should "cope with texts" in {
    val surveyor = ReleaseSurveyor(lib, rootDir, releaseId)
    surveyor.textOverview(new File(surveyor.releaseDir, "texts"))
  }




}
