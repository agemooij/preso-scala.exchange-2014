package preso
package things

import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.routing._

trait ThingsApiRoutes extends HttpService {
  // format: OFF
  val thingsApiRoutes = {
    pathPrefix("things") {
      pathEnd {
        get {
          complete(OK, List(Thing("Apple"), Thing("iPhone")))
        } ~
        post {
          entity(as[Thing]) { thing ⇒
            complete(Created)
          }
        }
      } ~
      path(LongNumber) { id ⇒
        get {
          complete(OK, Thing(s"Thing # $id"))
        }
      }
    }
  }
  // format: ON
}
