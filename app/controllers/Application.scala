package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.libs.ws._
import eu.trentorise.opendata.jackan.CkanClient
import eu.trentorise.opendata.jackan.model.{CkanDataset, CkanResource}

import scala.collection.JavaConversions._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import com.github.tototoshi.csv._
import model.{FormSearchGeneral, OpenDataPortal, SearchResult, SearchResultJSON}
import play.api.libs.json
import play.api.libs.json.{JsValue, Json, Writes}


class Application @Inject() (val messagesApi: MessagesApi,ws: WSClient, webJarAssets: WebJarAssets) extends Controller with I18nSupport {

  //declare constant variables
  val OpenDataPortalStatusOk = 1
  val OpenDataPortalStatusNotOk = 0

  val SearchStatusOk = 1
  val SearchStatusNotOk = 0

  val sOpenDataPortal = loadOpenDataPortalFromCSV("open_data_portal.csv")

  implicit val SearchResultJSONWrites = new Writes[SearchResultJSON] {
    def writes(pSearchResult: SearchResultJSON) = Json.obj(
      "dataPortal" -> pSearchResult.dataPortal,
      "dataTitle" -> pSearchResult.dataTitle,
      "dataType" -> pSearchResult.dataType,
      "dataLicense" -> pSearchResult.dataLicense,
      "dataURL" -> pSearchResult.dataURL

    )
  }

  def loadOpenDataPortalFromCSV(pFileName:String):List[OpenDataPortal] = {
    val tListOpenDataPortal:scala.collection.mutable.ArrayBuffer[OpenDataPortal] = new scala.collection.mutable.ArrayBuffer[OpenDataPortal]()

    val tStream: java.io.InputStream = this.getClass().getResourceAsStream("/public/data/"+pFileName)
    val tReader = CSVReader.open(scala.io.Source.fromInputStream(tStream))

    val tList = tReader.allWithHeaders()
    tList.foreach(tMap => {
      val tOpenDataPortal = OpenDataPortal(tMap.get("Name").mkString,tMap.get("Url").mkString,tMap.get("Type").mkString,tMap.get("Status").mkString.toInt)
      tListOpenDataPortal.append(tOpenDataPortal)
    })

    tListOpenDataPortal.toList
  }

  def convertSearchResultToJSON(pListSearchResult:List[SearchResult]):JsValue = {

    val tListSearchResultJSON:scala.collection.mutable.ArrayBuffer[SearchResultJSON] = new scala.collection.mutable.ArrayBuffer[SearchResultJSON]()

    for (tSearchResult <- pListSearchResult) {

      for(tDataset <- tSearchResult.searchList) {

        val tSearchResultJSON = SearchResultJSON(tSearchResult.dataPortal.name,tDataset.getTitle,tDataset.getNotes,tDataset.getResources.head.getFormat,tDataset.getLicenseId,tDataset.getResources.head.getUrl)
        tListSearchResultJSON.append(tSearchResultJSON)
      }

    }

    val tJSON = Json.toJson(tListSearchResultJSON)

    tJSON

  }

  def searchOpenDataPortals(pQuery:String, pLimit:Int = 10, pOffset:Int = 0) :List[SearchResult] = {

    val tListCkanDataset:scala.collection.mutable.ArrayBuffer[SearchResult] = new scala.collection.mutable.ArrayBuffer[SearchResult]()

    //iterate through the open data portal
    sOpenDataPortal.foreach(tOpenDataPortal => {

      if(tOpenDataPortal.status == OpenDataPortalStatusOk && tOpenDataPortal.typePortal == "CKAN") {
        //status ok

        val tListDataset = SearchOpenDataPortal(pQuery,tOpenDataPortal,pLimit,pOffset)

        tListCkanDataset.append(tListDataset)

      }

    })

    tListCkanDataset.toList

  }

  def SearchOpenDataPortal(pQuery:String,pOpenDataPortal:OpenDataPortal,pLimit:Int=10,pOffset:Int=0):SearchResult = {

    val tCkanClient = new CkanClient(pOpenDataPortal.url)
    val tResult =  tCkanClient.searchDatasets(pQuery,pLimit,pOffset)
    val tSearchResult = SearchResult(pOpenDataPortal,SearchStatusOk,tResult.getCount,tResult.getResults.toList)

    tSearchResult

  }

  def search = Action
  {
    implicit request => {
      val tSearchGeneralForm = Form(
        mapping(
          "searchTerm" -> text
        )(FormSearchGeneral.apply)(FormSearchGeneral.unapply)

      )

      val tSearchGeneralData = tSearchGeneralForm.bindFromRequest().get
      val tSearchResult = searchOpenDataPortals(tSearchGeneralData.searchTerm,1000,0)
      val tSearchResultJSON = convertSearchResultToJSON(tSearchResult)
      Ok(views.html.search(tSearchGeneralForm, tSearchResult,tSearchResultJSON))
    }
  }

  def about = Action {
    Ok(views.html.about(null))
  }


  def index = Action {


    val tSearchGeneralForm = Form(
      mapping(
        "searchTerm" -> text
      )(FormSearchGeneral.apply)(FormSearchGeneral.unapply)

    )

   Ok(views.html.index(tSearchGeneralForm,sOpenDataPortal))
  }

}
