package preso
package dudes

import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.routing._

import preso.util._

trait DudesApiRoutes extends HttpService with DudeStoreProvider with ExecutionContextProvider {
  // format: OFF
  val dudesApiRoutes = {
    pathPrefix("dudes") {
      pathEndOrSingleSlash {
        get {
          complete(OK, dudeStore.allDudes)
        } ~
        post {
          entity(as[Dude]) { dude ⇒
            onSuccess(dudeStore.createDude(dude)) { newId ⇒
              complete(Created, JsObject("id" -> JsNumber(newId)))
            }
          }
        }
      } ~
      path(LongNumber) { id ⇒
        get {
          complete(OK)
        } ~
        put {
          entity(as[Dude]) { dude ⇒
            onSuccess(dudeStore.updateDude(id, dude)) {
              case Some(dude) ⇒ complete(Created, dude)
              case None       ⇒ complete(NotFound)
            }
          }
        }
      }
    }
  }
  // format: ON
}
