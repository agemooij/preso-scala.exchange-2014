package preso.util

import spray.routing._
import spray.routing.Directives._
import spray.http.HttpHeaders._
import spray.http.StatusCodes._

trait HttpsDirectives {
  import HttpsDirectives._

  def enforceHttpsIf(yes: ⇒ Boolean): Directive0 = {
    if (yes) enforceHttps
    else pass
  }

  def enforceHttps: Directive0 = {
    respondWithHeader(StrictTransportSecurity) &
      extract(isHttpsRequest).flatMap(
        if (_) pass
        else redirectToHttps
      )
  }

  def redirectToHttps: Directive0 = {
    requestUri.flatMap { uri ⇒
      redirect(uri.copy(scheme = "https"), MovedPermanently)
    }
  }
}

object HttpsDirectives {
  /** Hardcoded max-age of one year (31536000 seconds) for now. */
  val StrictTransportSecurity = RawHeader("Strict-Transport-Security", "max-age=31536000")

  val isHttpsRequest: RequestContext ⇒ Boolean = { ctx ⇒
    ctx.request.uri.scheme == "https" || ctx.request.headers.exists(h ⇒ h.is("x-forwarded-proto") && h.value == "https")
  }
}
