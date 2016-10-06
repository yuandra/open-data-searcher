package helpers

import javax.inject.Inject
import javax.inject.Singleton

import eu.trentorise.opendata.jackan.{CkanClient, SearchResults}
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

  //convert search result to JSON
  def convertSearchResultToJSON(pListSearchResult:List[SearchResult]):JsValue = {

    val tListSearchResultJSON:scala.collection.mutable.ArrayBuffer[SearchResultJSON] = new scala.collection.mutable.ArrayBuffer[SearchResultJSON]()

    for (tSearchResult <- pListSearchResult) {

      if(tSearchResult!=null) {
        for (tDataset <- tSearchResult.searchList) {

          val tTitle = tDataset.title
          val tNotes = tDataset.description
          val tLicense = tDataset.license
          var tURL = tDataset.link
          var tFormat = tDataset.format

          val tSearchResultJSON = SearchResultJSON(tSearchResult.dataPortal.name, tTitle, tNotes, tFormat, tLicense, tURL)
          tListSearchResultJSON.append(tSearchResultJSON)
        }
      }

    }

    val tJSON = Json.toJson(tListSearchResultJSON)

    tJSON

  }

  //main entry point for search open data portals
  def searchOpenDataPortals(pQuery:String, pLimit:Int = 10, pOffset:Int = 0) :List[SearchResult] = {

    val tListDataset:scala.collection.mutable.ArrayBuffer[SearchResult] = new scala.collection.mutable.ArrayBuffer[SearchResult]()

    //iterate through the open data portal
    configHelper.sOpenDataPortal.foreach(tOpenDataPortal => {

      if(tOpenDataPortal.status == configHelper.STATUS_OPEN_DATA_PORTAL_OK) {
        //status ok
        if(tOpenDataPortal.typePortal == "CKAN") {
          // is ckan (parse using CKAN parser)

          var tListResult = SearchOpenDataPortalCKAN(pQuery, tOpenDataPortal, pLimit, pOffset)
          if(tListResult==null)
          {
            tListResult = SearchResult(tOpenDataPortal,configHelper.STATUS_SEARCH_OK,0,List[Dataset]())
          }
          tListDataset.append(tListResult)
        }

      }

    })

    tListDataset.toList

  }

  //open data portal CKAN client
  def SearchOpenDataPortalCKAN(pQuery:String,pOpenDataPortal:OpenDataPortal,pLimit:Int=10,pOffset:Int=0):SearchResult = {

    val tCkanClient = new CkanClient(pOpenDataPortal.url)
    var tResults:SearchResults[CkanDataset] = null
    var tSearchResult:SearchResult = null

    try {
      val tListDataset:ListBuffer[Dataset] = ListBuffer[Dataset]()

      tResults = tCkanClient.searchDatasets(pQuery,pLimit,pOffset)

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

     tSearchResult = SearchResult(pOpenDataPortal,configHelper.STATUS_SEARCH_OK,tResults.getCount,tListDataset.toList)

      tSearchResult
    }
    catch {
      case _: Throwable => tSearchResult
    }
    finally {
      tSearchResult
    }

  }

}
