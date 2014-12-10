package preso

import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.routing._

import util._

trait WebsiteRoutes extends HttpService with PageDirectives {
  val websiteRoutes = {
    handleExceptions(WebsiteExceptionHandler) {
      handleRejections(WebsiteRejectionHandler) {
        getFromResourceDirectory("assets")
      }
    }
  }

  val WebsiteRejectionHandler = RejectionHandler {
    case Nil ⇒ completeWithNotFoundPage()
    case _   ⇒ completeWithInternalServerErrorPage()
  }

  val WebsiteExceptionHandler = ExceptionHandler {
    case _ ⇒ completeWithInternalServerErrorPage()
  }
}

trait PageDirectives extends Directives with ActorRefFactoryProvider {
  def completeWithNotFoundPage(): Route = {
    respondWithStatus(NotFound) {
      getFromResource("errors/404.html")
    }
  }

  def completeWithInternalServerErrorPage(): Route = {
    respondWithStatus(InternalServerError) {
      getFromResource("errors/500.html")
    }
  }
}
