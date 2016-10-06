package controllers

import javax.inject.Inject

import helpers.ConfigHelper
import model.FormConfiguration
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

//controller for configuration class
class ConfigController @Inject() (val messagesApi: MessagesApi,configHelper: ConfigHelper, webJarAssets: WebJarAssets) extends Controller  with I18nSupport {

  //show configuration form
  def showConfigForm = Action {

    val tConfigForm = Form(
      mapping(
        "baseURL" -> text,
        "configSheetID" -> text,
        "dataPortalSheetID" -> text,
        "translationSheetID" -> text
      )(FormConfiguration.apply)(FormConfiguration.unapply)

    )

    Ok(views.html.config(tConfigForm,configHelper))

  }

  //update configuration
  def updateConfigForm = Action {

    implicit request => {
      val tConfigForm = Form(
        mapping(
          "baseURL" -> text,
          "configSheetID" -> text,
          "dataPortalSheetID" -> text,
          "translationSheetID" -> text
        )(FormConfiguration.apply)(FormConfiguration.unapply)

      )

      val tFormConfigurationData = tConfigForm.bindFromRequest().get

      configHelper.CONFIG_BASE_URL = tFormConfigurationData.baseURL
      configHelper.CONFIG_SHEET_ID = tFormConfigurationData.configSheetID
      configHelper.CONFIG_DATA_PORTAL_SHEET_ID = tFormConfigurationData.dataPortalSheetID
      configHelper.CONFIG_TRANSLATION_SHEET_ID = tFormConfigurationData.translationSheetID

      configHelper.loadConfig()

      Redirect(routes.Application.index())

    }

  }

  def loadConfigForm = Action {
    configHelper.loadConfig()

    Redirect(routes.Application.index())
  }

}
