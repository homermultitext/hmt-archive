package org.homermultitext.hmtcexbuilder
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.dse._
import java.io.File
import java.io.PrintWriter

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** Class for creating overviews of a published release of HMT
* instantiated as a CiteLibrary.
*
* @param lib The published release to survey.
* @param baseDir Directory where subdirectory for reports will be created.
* @param releaseId Identifier for this release, used to create name
* of subdirectory where reports are written.
*/
case class ReleaseSurveyor(lib: CiteLibrary, baseDir: String, releaseId: String) extends LogSupport {

  /** Base URL for references to HMT Image Citation Tool.*/
  val hmtIctBase = "http://www.homermultitext.org/ict2/"

  /** Base URL to an installation of IIPSrv with HMT data. */
  val hmtIipSrvBase = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/"

  /** Compose IIPSrv URL for an image. */
  def iipSrvUrl(img: Cite2Urn,  width: Int = 1000, baseUrl: String = hmtIipSrvBase): String = {
    val trail = s"&WID=${width}&CVT=JPEG"
    val imageOnly = List(baseUrl, img.namespace, img.collection, img.version, img.dropExtensions.objectOption.get).mkString("/") + s".tif"

    img.objectExtensionOption match {
      case None => imageOnly +  trail
      case roi:  Some[String] =>imageOnly + "&RGN=" + roi.get + trail
    }
  }

  /** Map of topic label to subdirectory name. */
  val subdirForTopic = Map (
    "images" -> "images",
    "tbs" -> "codices-papyri",
    "texts" -> "texts",
    "dse" -> "dse"
  )


  /** Assemble a complete suite of reports for a this release.
  *
  *  @param columns Width in columns of tables with thumbnail images.
  *  @param thumbSize Width in pixes of thumbnail images.
  */
  def overview(columns: Int = 6, thumbSize: Int = 400) = {
    val indexText = homePage
    val indexFile = new File(releaseDir, "index.md")
    new PrintWriter(indexFile) {write(indexText); close; }
    info("Surveying texts...")
    textOverview(dirMap("texts"))

    info("Surveying images...")
    imageOverview(dirMap("images"), columns, thumbSize)
    info("Surveying text-bearing surfaces...")
    tbsOverview(dirMap("tbs"), columns,thumbSize)
    info("Surveying DSE relations...")
    dseOverview(dirMap("dse"), columns,thumbSize)
  }


  /** Compose a home page, in markdown format, for this report.
  */
  def homePage: String = {
    val hdr = "# Overview of HMT project release **" + releaseId +"**\n\n" +
    fileLayoutBoilerPlate  +
    "## Collection data models\n\n"


    val citeCatalog = lib.collectionRepository.get.catalog
    val dm = for (dm <- lib.dataModels.get)  yield {
      val modelLabel = "\n**" + dm.label + s"** (`${dm.model}`) applies to \n\n-   "
      val u = dm.collection
      val display = s"${citeCatalog.collection(u).get.collectionLabel} (`${u}`)"
      /*
      val appliedTo = lib.collectionsForModel(dm.model)
      val display = appliedTo.map(u => s"${citeCatalog.collection(u).get.collectionLabel} (`${u}`)")  */
      modelLabel + display

    }

    val txtsHdr = "\n\n## Texts\n\nThe OHCO2 model of citable texts applies to \n\n"
    val textCatalog = lib.textRepository.get.catalog
    val exemplarList = for (txt <- textCatalog.labelledExemplars) yield {
      s"-   ${textCatalog.groupName(txt.urn)}, *${textCatalog.workTitle(txt.urn)}* (${txt.label}: `${txt.urn}`)"
    }
    val versionList =  for (txt <- textCatalog.labelledVersions) yield {
        s"-   ${textCatalog.groupName(txt.urn)}, *${textCatalog.workTitle(txt.urn)}* (${txt.label}: `${txt.urn})`"
      //s"-   ${txt.label} (${txt.urn})"
    }
    hdr + dm.mkString("\n") + txtsHdr+ exemplarList.mkString("\n") + versionList.mkString("\n")
  }

  /** Find root directory as a File object,
  * ensuring that it has been created.
  */
  def rootDir : File = {
    val root = new File(baseDir)
    if (!root.exists){
      root.mkdir
    }
    root
  }

  /** Find directory for reports on this release, as a File object,
  * ensuring that it has been created.
  */
  def releaseDir : File = {
    val reportDir = new File(rootDir, s"hmt-${releaseId}-summary")
    if (!reportDir.exists){
      reportDir.mkdir
    }
    reportDir
  }

  /** Construct map of required subdirectories.
  */
  def dirMap: Map[String, File] = {
    val subdirMap = for (topic <- subdirForTopic.keySet) yield {
        val subdir = new File(releaseDir, subdirForTopic(topic))
        subdir.mkdir
        require(subdir.exists, s"Did not create directory for topic ${topic}")
        (topic, subdir)
    }
    subdirMap.toMap
  }



  /** Compose report on collections of images modelled as imagemodel objects.
  *
  * @param imageDir Directory where image reports should be written.
  * @param columns Width of output table in columns.
  * @param thumbSize Widthof thumbnail images in pixels.
  */
  def imageOverview(imageDir: File, columns: Int, thumbSize: Int) = {
    val binaryImageModel = Cite2Urn("urn:cite2:cite:datamodels.v1:imagemodel")
    val citeCatalog = lib.collectionRepository.get.catalog

    for (urn <- lib.collectionsForModel(binaryImageModel)) {
      val objects = lib.collectionRepository.get.objectsForCollection(urn)

      val hdr = "# Summary for image collection\n\n" +
      s"**${citeCatalog.collection(urn).get.collectionLabel}** (`${urn}`):  total of ${objects.size} images.\n\n"

      // format a markdown string for each image
      val imgSet = for(obj <- objects) yield {
         s"[![${obj.urn}](${iipSrvUrl(obj.urn, thumbSize)})](${hmtIctBase}?urn=${obj.urn}) <br/>${obj.label} (`${obj.urn}`)"
      }
      val imgRecords = imgSet.toSeq.toVector
      // place the images in a tablewith with specified width (in cells)
      val rows = for (i <- 0 until imgRecords.size) yield {
          val oneBasedIndex = i + 1
          if (oneBasedIndex % columns == 0){
            val sliver = imgRecords.slice( oneBasedIndex - columns, oneBasedIndex)
            "| " + sliver.mkString(" | ") + " |"
          } else ""
      }
      val sizedRows = rows.filter(_.nonEmpty)

      // catch any left over if rows/columns didn't work out evenly
      val remndr =  imgRecords.size % columns
      val trailer = if (remndr != 0)  {
        val sliver = imgRecords.slice(imgRecords.size - remndr, imgRecords.size)
        val pad = List.fill( columns - remndr - 1)( " | ").mkString
        "| " + sliver.mkString(" | ") + pad + " |\n"
      } else ""

      val tableLabels =  List.fill(columns)("| ").mkString + "|\n"
      val tableSeparator =  List.fill(columns)("|:-------------").mkString + "|\n"

      val reportFile = new File(imageDir, urn.collection + "-summary.md")
      new PrintWriter(reportFile){write(hdr + tableLabels +  tableSeparator + sizedRows.mkString("\n") + "\n "+ trailer  +  "\n\n") ; close;}

    }   // for each collection
  }

  /** Compose report on collections of text-bearing surfaces.
  *
  * @param tbsDir Directory where TBS reports should be written.
  * @param columns Width of output table in columns.
  * @param thumbSize Widthof thumbnail images in pixels.
  */
  def tbsOverview(tbsDir: File, columns: Int, thumbSize: Int) = {
    def tbsModel = Cite2Urn("urn:cite2:cite:datamodels.v1:tbsmodel")
    val citeCatalog = lib.collectionRepository.get.catalog

    for (urn <- lib.collectionsForModel(tbsModel)) {
      val objects = lib.collectionRepository.get.objectsForCollection(urn)
      val hdr = "# Summary for artifact with texts\n\n" +
        s"**${citeCatalog.collection(urn).get.collectionLabel}** (`${urn}`):  total of ${objects.size} surfaces.  The following table illustrates each surface in sequence with its default image.\n\n"

      // write a markdown entry for each entry
      val md = for(obj <- objects) yield {
        val imgProp = obj.urn.addProperty("image")
        val img:Cite2Urn = obj.propertyValue(imgProp) match {
          case u: Cite2Urn => u
          case _ => throw new Exception(s"Value for image property on ${obj} was note a Cite2Urn.")
        }
        s"[![${obj.urn}](${iipSrvUrl(img, thumbSize)})](${hmtIctBase}?urn=${img}) <br/>${obj.label} (`${obj.urn}`)"
      }
      // organize objects in a table
      val rows = for (i <- 0 until md.size) yield {
        val oneBasedIndex = i + 1
        if (oneBasedIndex % columns == 0){
          val sliver = md.slice( oneBasedIndex - columns, oneBasedIndex)
          "| " + sliver.mkString(" | ") + " |"
        } else ""
      }
      val sizedRows = rows.filter(_.nonEmpty)

      // catch any left over if rows/columns didn't work out evenly
      val remndr =  md.size % columns
      val trailer = if (remndr != 0)  {
        val sliver = md.slice(md.size - remndr, md.size)
        val pad = List.fill( columns - remndr - 1)( " | ").mkString
        "| " + sliver.mkString(" | ") + pad + " |\n"
      } else ""

      val tableLabels =  List.fill(columns)("| ").mkString + "|\n"
      val tableSeparator =  List.fill(columns)("|:-------------").mkString + "|\n"

      val reportFile = new File(tbsDir, urn.collection + "-summary.md")
      new PrintWriter(reportFile){write(hdr + tableLabels +  tableSeparator + sizedRows.mkString("\n") + "\n" + trailer  +  "\n\n") ; close;}
    }
  }

    /** Compose report on collections of DSE relations.
    *
    * @param dseDir Directory where TBS reports should be written.
    * @param columns Width of output table in columns.
    * @param thumbSize Widthof thumbnail images in pixels.
    */
  def dseOverview(dseDir: File, columns: Int, thumbSize: Int)= {
    val dseModel = Cite2Urn("urn:cite2:cite:datamodels.v1:dse")
    val citeCatalog = lib.collectionRepository.get.catalog

    for (urn <- lib.collectionsForModel(dseModel)) {
      val objects = lib.collectionRepository.get.objectsForCollection(urn)

      val dseRecords = for (obj <-  objects) yield {
        val imgProp = obj.urn.addProperty("imageroi")
        val img:Cite2Urn = obj.propertyValue(imgProp) match {
          case u: Cite2Urn => u
          case _ => throw new Exception(s"Value for image property on ${obj} was note a Cite2Urn.")
        }

        val textProp = obj.urn.addProperty("passage")
        val passage:CtsUrn = obj.propertyValue(textProp) match {
          case u: CtsUrn => u
          case _ => throw new Exception(s"Value for texxt property on ${obj} was note a Cts2Urn.")
        }

        val surfaceProp = obj.urn.addProperty("surface")
        val surface:Cite2Urn = obj.propertyValue(surfaceProp) match {
          case u: Cite2Urn => u
          case _ => throw new Exception(s"Value for surface property on ${obj} was note a Cite2Urn.")
        }
        DsePassage(obj.urn, obj.label, passage, img, surface)
      }
      val dse = DseVector(dseRecords)

      // THIS SHOULD BE A DSE FUNCTION!
      val tbsCollections = dse.tbs.map(_.dropSelector.dropProperty)

      val hdr = "# Summary for digital scholarly edition releations\n\n" +
        s"**${citeCatalog.collection(urn).get.collectionLabel}** (`${urn}`):  total of ${objects.size} relations.\n\nEntries in the following list are organized by the order of surfaces in each codex or papyrus, and are linked to visualizations of texts appearing on the surface.\n\n"


      // now get sequence of those pages...
      // Need to verify that value of codex is OK: otherwise,
      // you'll get hideous crashing with exceptions.
      val md = for (codex <- tbsCollections) yield {
        info("Preparing DSE record for " + codex)
        val docHeader = s"## Records for ${citeCatalog.collection(codex).get.collectionLabel}\n\n"
        info(s"\n\nAssembling DSE records for ${codex}.")
        info("Please be patient...")
        val pages = lib.collectionRepository.get.objectsForCollection(codex)
        val md = for (pg <- pages) yield {
          info("\tpage "+ pg + "...")
          s"-  [${pg.urn.objectComponent}:  ${dse.textsForTbs(pg.urn).size} text passages recorded.](${dse.ictForSurface(pg.urn, hmtIctBase)})"
        }
        info("Made " + md.size + " records.")
        val reportFile = new File(dseDir, codex.collection + "-dse-summary.md")
        info("Writing report to " + reportFile)
        new PrintWriter(reportFile){write(hdr + md.mkString("\n") + "\n\n") ; close;}
      }
    }
  }


  /** Compose report on OHCO2 editions.
  *
  * @param textDir Directory where TBS reports should be written.
  */
  def textOverview(textDir: File) = {
    val ctsCatalog = lib.textRepository.get.catalog
    val corpus = lib.textRepository.get.corpus
    val alphaGroups = ctsCatalog.labelledGroups.toSeq.sortBy(_.label)


    // collect lines of text for report by successively
    // drilling down table of contents.
    // 1. text group level
    val lines = for (group <- alphaGroups) yield {
      val worksFromGroupToVersions = ctsCatalog.toc(group)

      val groupHdr = worksFromGroupToVersions.size match {
        case 1 =>  s"## ${group.label}: ${worksFromGroupToVersions.size} work\n\n"
        case _ =>  s"## ${group.label}: ${worksFromGroupToVersions.size} works\n\n"
      }
      // 2. work level
      val wkData = for (wk <- worksFromGroupToVersions.keySet.toSeq.sortBy(_.label))  yield {
        val versionsFromWorkToExemplar = worksFromGroupToVersions(wk)

        // 3. version level
        val versionData = for (vers <- versionsFromWorkToExemplar.keySet.toSeq.sortBy(_.label) ) yield {
          val nodes = corpus ~~ vers.urn
          s"-   ${vers.label} (`${vers.urn}`):  ${nodes.size} citable units"
        }

        s"### ${wk.label}\n\n" + versionData.mkString("\n") +"\n\n"
      }
      groupHdr +  wkData.mkString("\n") +"\n\n"
      }

      val outFile = new File(textDir, "texts-summary.md")
      new PrintWriter(outFile){ write(lines.mkString("\n")); close;}
  }

  /** Compose message about file layout. */
  def fileLayoutBoilerPlate: String =  {
    val folderList = for (topic <- subdirForTopic.keySet) yield {
      "-   `" + subdirForTopic(topic) + "`"
    }
    "Note: more details are provided for specific contents of this release in the associated folders:\n\n" + folderList.mkString("\n") + "\n\n"
  }

}
