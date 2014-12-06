package preso

import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.routing._

trait AssetRoutes extends HttpService {
  // format: OFF
  val assetRoutes = {
    pathPrefix("assets") {
      complete(OK)
    }
  }
  // format: ON
}
