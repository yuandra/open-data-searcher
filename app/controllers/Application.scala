package controllers

import javax.inject.Inject

import helpers.{ConfigHelper, SearchHelper}
import model.FormSearchGeneral
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws._
import play.api.mvc._

class Application @Inject() (val messagesApi: MessagesApi,ws: WSClient, webJarAssets: WebJarAssets, configHelper:ConfigHelper,searchHelper: SearchHelper) extends Controller with I18nSupport {

  //show about page
  def about = Action {
    Ok(views.html.about(configHelper))
  }

  //show main page
  def index = Action {

    //check if config has been loaded
    if(configHelper.CONFIG_STATUS == configHelper.STATUS_CONFIG_NOT_OK){
      //redirect to config
      Redirect(routes.ConfigController.showConfigForm())
    }
    else {
      //load as normal, show main page
      val tSearchGeneralForm = Form(
        mapping(
          "searchTerm" -> text
        )(FormSearchGeneral.apply)(FormSearchGeneral.unapply)

      )

      Ok(views.html.index(tSearchGeneralForm,configHelper.sOpenDataPortal,configHelper))

    }

  }

  //show search page
  def search = Action
  {
    implicit request => {
      val tSearchGeneralForm = Form(
        mapping(
          "searchTerm" -> text
        )(FormSearchGeneral.apply)(FormSearchGeneral.unapply)

      )

      val tSearchGeneralData = tSearchGeneralForm.bindFromRequest().get
      val tSearchResult = searchHelper.searchOpenDataPortals(tSearchGeneralData.searchTerm,configHelper.CONFIG_MAX_DATA_SEARCH,0)
      val tSearchResultJSON = searchHelper.convertSearchResultToJSON(tSearchResult)
      Ok(views.html.search(tSearchGeneralForm, tSearchResult,tSearchResultJSON,configHelper))
    }
  }

}
