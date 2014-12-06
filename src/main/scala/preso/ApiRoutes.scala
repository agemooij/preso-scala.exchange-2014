package preso

import spray.http.CacheDirectives._
import spray.http.HttpHeaders._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.routing._

import things._
import dudes._

// format: OFF
trait ApiRoutes extends HttpService
                   with ThingsApiRoutes
                   with DudesApiRoutes {
  val apiRoutes = {
    pathPrefix("api") {
      handleRejections(ApiRejectionHandler) {
        noCachingAllowed {
          thingsApiRoutes ~ dudesApiRoutes
        }
      }
    }
  }

  val ApiRejectionHandler = RejectionHandler.Default
  val noCachingAllowed = respondWithHeaders(List(`Cache-Control`(`no-store`),
                                                  RawHeader("Pragma", "no-cache")))
}
// format: ON
