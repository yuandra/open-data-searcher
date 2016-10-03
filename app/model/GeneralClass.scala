package model

import eu.trentorise.opendata.jackan.model.CkanDataset

case class Dataset(title:String,description:String,format:String,license:String,link:String)
case class SearchResult(dataPortal:OpenDataPortal,searchStatus:Int,searchNo:Int,searchList:List[Dataset])
case class SearchResultJSON(dataPortal:String,dataTitle:String,dataNotes:String,dataType:String,dataLicense:String,dataURL:String)
case class OpenDataPortal(name:String,url:String,typePortal:String,var status:Int)
case class FormSearchGeneral(searchTerm:String)
case class FormConfiguration(baseURL:String,configSheetID:String,dataPortalSheetID:String,translationSheetID:String)
