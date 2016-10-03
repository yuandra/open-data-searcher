package helpers

import javax.inject.Inject
import javax.inject.Singleton

import eu.trentorise.opendata.jackan.CkanClient
import eu.trentorise.opendata.jackan.model.{CkanDataset, CkanResource}

import scala.collection.JavaConversions._
import model.{Dataset, OpenDataPortal, SearchResult, SearchResultJSON}
import play.api.libs.json.{JsValue, Json, Writes}

import scala.collection.mutable.ListBuffer

//class for handling search API
@Singleton
class SearchHelper @Inject() (configHelper: ConfigHelper) {

  implicit val SearchResultJSONWrites = new Writes[SearchResultJSON] {
    def writes(pSearchResult: SearchResultJSON) = Json.obj(
      "dataPortal" -> pSearchResult.dataPortal,
      "dataTitle" -> pSearchResult.dataTitle,
      "dataType" -> pSearchResult.dataType,
      "dataLicense" -> pSearchResult.dataLicense,
      "dataURL" -> pSearchResult.dataURL

    )
  }

  def convertSearchResultToJSON(pListSearchResult:List[SearchResult]):JsValue = {

    val tListSearchResultJSON:scala.collection.mutable.ArrayBuffer[SearchResultJSON] = new scala.collection.mutable.ArrayBuffer[SearchResultJSON]()

    for (tSearchResult <- pListSearchResult) {

      for(tDataset <- tSearchResult.searchList) {

        val tTitle = tDataset.title
        val tNotes = tDataset.description
        val tLicense = tDataset.license
        var tURL = tDataset.link
        var tFormat = tDataset.format

        val tSearchResultJSON = SearchResultJSON(tSearchResult.dataPortal.name,tTitle,tNotes,tFormat,tLicense,tURL)
        tListSearchResultJSON.append(tSearchResultJSON)
      }

    }

    val tJSON = Json.toJson(tListSearchResultJSON)

    tJSON

  }

  def searchOpenDataPortals(pQuery:String, pLimit:Int = 10, pOffset:Int = 0) :List[SearchResult] = {

    val tListCkanDataset:scala.collection.mutable.ArrayBuffer[SearchResult] = new scala.collection.mutable.ArrayBuffer[SearchResult]()

    //iterate through the open data portal
    configHelper.sOpenDataPortal.foreach(tOpenDataPortal => {

      if(tOpenDataPortal.status == configHelper.STATUS_OPEN_DATA_PORTAL_OK && tOpenDataPortal.typePortal == "CKAN") {
        //status ok & ckan (parse using CKAN parser)

        val tListDataset = SearchOpenDataPortal(pQuery,tOpenDataPortal,pLimit,pOffset)

        tListCkanDataset.append(tListDataset)

      }

    })

    tListCkanDataset.toList

  }

  def SearchOpenDataPortal(pQuery:String,pOpenDataPortal:OpenDataPortal,pLimit:Int=10,pOffset:Int=0):SearchResult = {

    val tCkanClient = new CkanClient(pOpenDataPortal.url)
    val tResults =  tCkanClient.searchDatasets(pQuery,pLimit,pOffset)

    val tListDataset:ListBuffer[Dataset] = ListBuffer[Dataset]()

    for(tResult:CkanDataset <- tResults.getResults) {

      val tTitle = tResult.getTitle
      val tNotes = tResult.getNotes
      val tLicense = tResult.getLicenseId
      var tURL = ""
      var tFormat = ""

      if(!tResult.getResources.isEmpty) {
        tURL = tResult.getResources.head.getUrl
        tFormat = tResult.getResources.head.getFormat
      }
      else {
        tURL = ""
        tFormat = ""
      }

      val tDataset:Dataset = Dataset(tTitle,tNotes,tFormat,tLicense,tURL)

      tListDataset.append(tDataset)

    }

    val tSearchResult:SearchResult = SearchResult(pOpenDataPortal,configHelper.STATUS_SEARCH_OK,tResults.getCount,tListDataset.toList)

    tSearchResult



  }

}
