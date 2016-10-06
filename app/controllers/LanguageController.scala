package controllers

import javax.inject.Inject

import helpers.ConfigHelper
import play.api.mvc.Action
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class LanguageController @Inject() (val messagesApi: MessagesApi,configHelper: ConfigHelper, webJarAssets: WebJarAssets) extends Controller  with I18nSupport {

  def setLanguage(pLanguage:String) = Action {

    configHelper.setConfig("language",pLanguage)
    configHelper.loadConfigGlobal
    configHelper.loadTranslation

    Redirect(routes.Application.index())

  }

}
