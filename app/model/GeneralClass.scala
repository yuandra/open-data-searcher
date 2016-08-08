package model

import eu.trentorise.opendata.jackan.model.CkanDataset

case class SearchResult(dataPortal:OpenDataPortal,searchStatus:Int,searchNo:Int,searchList:List[CkanDataset])
case class SearchResultJSON(dataPortal:String,dataTitle:String,dataNotes:String,dataType:String,dataLicense:String,dataURL:String)
case class OpenDataPortal(name:String,url:String,typePortal:String,var status:Int)
case class FormSearchGeneral(searchTerm:String)
