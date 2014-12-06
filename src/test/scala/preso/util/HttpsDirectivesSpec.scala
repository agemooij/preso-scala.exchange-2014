package preso.util

import org.scalatest._

import spray.http._
import spray.http.HttpHeaders._
import spray.http.StatusCodes._
import spray.routing._
import spray.routing.Directives._
import spray.testkit._

import HttpsDirectives._

class HttpsDirectivesSpec extends HttpsDirectives with WordSpecLike with Matchers with Inspectors with ScalatestRouteTest {
  val httpUri = Uri("http://example.com/api/awesome")
  val httpsUri = Uri("https://example.com/api/awesome")

  "The enforceHttps directive" should {
    val route = enforceHttps {
      complete(OK)
    }

    "allow https requests and respond with the HSTS header" in {
      Get(httpsUri) ~> route ~> check {
        status shouldBe OK
        header(StrictTransportSecurity.name) should be(Some(StrictTransportSecurity))
      }
    }

    "allow terminated https requests containing a 'X-Forwarded-Proto' header and respond with the HSTS header" in {
      Get(httpUri) ~> addHeader(RawHeader("X-Forwarded-Proto", "https")) ~> route ~> check {
        status shouldBe OK
        header(StrictTransportSecurity.name) should be(Some(StrictTransportSecurity))
      }
    }

    "redirect plain http requests to the matching https URI" in {
      Get(httpUri) ~> route ~> check {
        status shouldBe MovedPermanently
        header[Location].map(l ⇒ Uri(l.value)) should be(Some(httpsUri))
        header(StrictTransportSecurity.name) should be(Some(StrictTransportSecurity))
      }
    }

    "redirect terminated http requests to the matching https URI" in {
      Get(httpUri) ~> addHeader(RawHeader("X-Forwarded-Proto", "http")) ~> route ~> check {
        status shouldBe MovedPermanently
        header[Location].map(l ⇒ Uri(l.value)) should be(Some(httpsUri))
        header(StrictTransportSecurity.name) should be(Some(StrictTransportSecurity))
      }
    }
  }

  "The enforceHttpsIf directive" should {
    "enforce https when the argument resolves to true" in {
      val route = enforceHttpsIf(true) {
        complete(OK)
      }

      Get(httpsUri) ~> route ~> check {
        status shouldBe OK
        header(StrictTransportSecurity.name) should be(Some(StrictTransportSecurity))
      }

      Get(httpUri) ~> route ~> check {
        status shouldBe MovedPermanently
        header[Location].map(l ⇒ Uri(l.value)) should be(Some(httpsUri))
        header(StrictTransportSecurity.name) should be(Some(StrictTransportSecurity))
      }
    }

    "not enforce https when the argument resolves to false" in {
      val route = enforceHttpsIf(false) {
        complete(OK)
      }

      Get(httpsUri) ~> route ~> check {
        status shouldBe OK
        header(StrictTransportSecurity.name) should be(None)
      }

      Get(httpUri) ~> route ~> check {
        status shouldBe OK
        header(StrictTransportSecurity.name) should be(None)
      }
    }
  }

}
