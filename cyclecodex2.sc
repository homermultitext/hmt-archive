import edu.holycross.shot.cite._
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.dse._
import edu.holycross.shot.citeobj._
import org.homermultitext.hmtcexbuilder._


val libDir = "archive"
val tbsDir = "archive/codices"
val dseDir = "archive/dse"

// Create a CiteLibrary from CEX files:
val libHeader = DataCollector.compositeFiles(libDir, "cex")
val codicesCex = DataCollector.compositeFiles(tbsDir, "cex")
val codexLibCex = libHeader + "\n" + codicesCex
val codexLib = CiteLibrary(codexLibCex, "#", ",")

val dseCex = DataCollector.compositeFiles(dseDir, "cex")
val dse = Dse(libHeader + "\n" + dseCex )


//val surveyor = ReleaseSurveyor(dseCex, "releases-cex" ,  "non-release")
val hmtIipSrvBase = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/"

def iipSrvUrl(img: Cite2Urn,  width: Int = 60, baseUrl: String = hmtIipSrvBase): String = {
  val trail = s"&WID=${width}&CVT=JPEG"
  val imageOnly = List(baseUrl, img.namespace, img.collection, img.version, img.dropExtensions.objectOption.get).mkString("/") + s".tif"
  imageOnly + trail
}
// Identify collections iomplementing the TBS model:
val tbsModel = Cite2Urn("urn:cite2:cite:datamodels.v1:tbsmodel")
// For each TBS collection, make a markdown view/browser
val mdList = for (c <- codexLib.collectionsForModel(tbsModel)) yield {
  println("PRocess " + c)
  val  collOpt =  codexLib.collectionRepository.get.catalog.collection(c)
  val title = s"## DSE links for ${collOpt.get.collectionLabel}\n\n"

  val collectionData = codexLib.collectionRepository.get ~~ c
  val pages = for (pg <- collectionData)  yield {
    val dseLink = dse.ictForSurface(pg.urn)
    val imgSet = dse.imagesForTbs(pg.urn).toSeq
    //println("IMAGES " + imgSet)
    if (imgSet.nonEmpty) {
      s"-  [![img](${iipSrvUrl(imgSet(0))}) ${pg.urn.objectComponent}](${dseLink})"

    } else {
      s"-  (no indexed DSE relations) [${pg.urn.objectComponent}](${dseLink})"
    }

  }
  title + pages.mkString("\n")
}


import java.io.PrintWriter

new PrintWriter("dse-views-by-codex.md"){write(mdList.mkString("\n\n")); close;}
