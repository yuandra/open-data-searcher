package helpers

import com.github.tototoshi.csv.CSVReader
import model.OpenDataPortal
import javax.inject.Singleton


@Singleton
class ConfigHelper {

  //declare constant variables

  val STATUS_OPEN_DATA_PORTAL_OK = 1
  val STATUS_OPEN_DATA_PORTAL_NOT_OK = 0

  val STATUS_SEARCH_OK = 1
  val STATUS_SEARCH_NOT_OK = 0

  val STATUS_CONFIG_OK = 1
  val STATUS_CONFIG_NOT_OK = 0

  val STATUS_URL_NOT_OK = ""

  val STATUS_TRANSLATION_LANGUAGE_DEFAULT = "en"

  var CONFIG_BASE_URL = STATUS_URL_NOT_OK
  var CONFIG_DATA_PORTAL_SHEET_ID = STATUS_URL_NOT_OK
  var CONFIG_TRANSLATION_SHEET_ID = STATUS_URL_NOT_OK
  var CONFIG_SHEET_ID = STATUS_URL_NOT_OK

  var CONFIG_DATA_PORTAL_SHEET_URL = STATUS_URL_NOT_OK
  var CONFIG_SHEET_URL = STATUS_URL_NOT_OK
  var CONFIG_TRANSLATION_SHEET_URL = STATUS_URL_NOT_OK

  var CONFIG_STATUS = STATUS_CONFIG_NOT_OK
  var CONFIG_MAX_DATA_SEARCH = 10000
  var CONFIG_TRANSLATION_LANGUAGE = STATUS_TRANSLATION_LANGUAGE_DEFAULT


  var sOpenDataPortal:List[OpenDataPortal] = null
  var sConfig:scala.collection.mutable.Map[String,String] = null
  val sTranslationDefault:Map[String,String] = loadTranslationFromFile("translation_default.csv",STATUS_TRANSLATION_LANGUAGE_DEFAULT)
  var sTranslation:Map[String,String] = Map[String,String]()
  var sTranslationList:Array[String] = null


  //load the open data portal information from CSV
  def loadOpenDataPortalFromGoogleSheet(pURL:String):List[OpenDataPortal] = {
    val tListOpenDataPortal:scala.collection.mutable.ArrayBuffer[OpenDataPortal] = new scala.collection.mutable.ArrayBuffer[OpenDataPortal]()

    val tReader = CSVReader.open(scala.io.Source.fromURL(pURL))

    val tList = tReader.allWithHeaders()
    tList.foreach(tMap => {
      val tOpenDataPortal = OpenDataPortal(tMap.get("Name").mkString,tMap.get("Url").mkString,tMap.get("Type").mkString,tMap.get("Status").mkString.toInt)
      tListOpenDataPortal.append(tOpenDataPortal)
    })

    tListOpenDataPortal.toList
  }

  //load the configuration information from CSV
  def loadConfigFromGoogleSheet(pURL:String):scala.collection.mutable.Map[String,String] = {

    val tConfigMap:scala.collection.mutable.Map[String,String] = scala.collection.mutable.Map[String,String]()

    val tReader = CSVReader.open(scala.io.Source.fromURL(pURL))

    val tList = tReader.allWithHeaders()
    tList.foreach(tMap => {
      tConfigMap.put(tMap.get("Key").mkString,tMap.get("Value").mkString)
    })

    tConfigMap
  }

  //load the translation from google sheets
  def loadTranslationFromGoogleSheet(pURL:String, pLanguage:String):Map[String,String] = {

    val tConfigMap:scala.collection.mutable.Map[String,String] = scala.collection.mutable.Map[String,String]()

    val tReader = CSVReader.open(scala.io.Source.fromURL(pURL))

    val tList = tReader.allWithHeaders()
    tList.foreach(tMap => {
      tConfigMap.put(tMap.get("Key").mkString,tMap.get(pLanguage).mkString)
    })

    tConfigMap.toMap

  }

  //generate google sheets url
  def generateGoogleSheetsURL(pBaseURL:String,pID:String):String = {
    val tURL = pBaseURL.trim+"&gid="+pID.trim
    tURL
  }

  //load google sheets URL
  def loadGoogleSheetsURL() = {

    CONFIG_DATA_PORTAL_SHEET_URL = generateGoogleSheetsURL(CONFIG_BASE_URL,CONFIG_DATA_PORTAL_SHEET_ID)
    CONFIG_SHEET_URL = generateGoogleSheetsURL(CONFIG_BASE_URL,CONFIG_SHEET_ID)
    CONFIG_TRANSLATION_SHEET_URL = generateGoogleSheetsURL(CONFIG_BASE_URL,CONFIG_TRANSLATION_SHEET_ID)

  }

  //load the config based on the google sheet URL
  def loadConfig() = {

    //check if config,data portal, and translation URL not empty
    if(CONFIG_SHEET_ID != STATUS_URL_NOT_OK
      && CONFIG_DATA_PORTAL_SHEET_ID != STATUS_URL_NOT_OK
      && CONFIG_TRANSLATION_SHEET_ID != STATUS_URL_NOT_OK
      && CONFIG_BASE_URL != STATUS_URL_NOT_OK)
    {
      loadGoogleSheetsURL
      sOpenDataPortal = loadOpenDataPortalFromGoogleSheet(CONFIG_DATA_PORTAL_SHEET_URL)
      sConfig = loadConfigFromGoogleSheet(CONFIG_SHEET_URL)
      loadConfigGlobal
      loadTranslation
      CONFIG_STATUS = STATUS_CONFIG_OK
    }
    else {
      CONFIG_STATUS = STATUS_CONFIG_NOT_OK
    }


  }

  def setConfig(pKey:String,pValue:String) = {
    sConfig(pKey) = pValue
  }

  //load global config
  def loadConfigGlobal() = {
    CONFIG_TRANSLATION_LANGUAGE = sConfig.get("language").mkString
    CONFIG_MAX_DATA_SEARCH = sConfig.get("max_data_search").mkString.toInt
  }

  //load translation
  def loadTranslation() = {
    sTranslationList = sConfig.get("language_list").mkString.split(";")
    sTranslation = loadTranslationFromGoogleSheet(CONFIG_TRANSLATION_SHEET_URL,CONFIG_TRANSLATION_LANGUAGE)
  }

  //get a translation key
  def getTranslation(pKey:String): String = {

    if(sTranslation.isDefinedAt(pKey)){
      sTranslation.get(pKey).mkString
    }
    else if(sTranslationDefault.isDefinedAt(pKey)){
      sTranslationDefault.get(pKey).mkString
    }
    else {
      ""
    }

  }

  //load the default translation from file
  def loadTranslationFromFile(pFileName:String, pLanguage:String):Map[String,String] = {

    val tConfigMap:scala.collection.mutable.Map[String,String] = scala.collection.mutable.Map[String,String]()

    val tStream: java.io.InputStream = this.getClass().getResourceAsStream("/public/data/"+pFileName)
    val tReader = CSVReader.open(scala.io.Source.fromInputStream(tStream))

    val tList = tReader.allWithHeaders()
    tList.foreach(tMap => {
      tConfigMap.put(tMap.get("Key").mkString,tMap.get(pLanguage).mkString)
    })

    tConfigMap.toMap

  }


}
