package preso
package things

import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.routing._

import org.scalatest._
import spray.testkit._

class ThingsApiRoutesSpec extends ThingsApiRoutes with WordSpecLike with Matchers with Inspectors with ScalatestRouteTest {
  def actorRefFactory = system

  "The ThingsApiRoutes route handler" should {
    "handle a GET on /things by returning some things in JSON format" in {
      Get("/things") ~> thingsApiRoutes ~> check {
        status shouldBe StatusCodes.OK
        contentType shouldBe ContentTypes.`application/json`
        responseAs[List[Thing]] should have size (2)
      }
    }
    "handle a POST on /things by responding with Created in JSON format" in {
      Post("/things", Thing("thong")) ~> thingsApiRoutes ~> check {
        status shouldBe StatusCodes.Created
        contentType shouldBe ContentTypes.`text/plain(UTF-8)`
      }
    }
    "handle a GET on /things/12345 by returning one thing in JSON format" in {
      Get("/things/12345") ~> thingsApiRoutes ~> check {
        status shouldBe StatusCodes.OK
        contentType shouldBe ContentTypes.`application/json`
        responseAs[Thing].name should include("12345")
      }
    }

    "reject a PUT on /things/12345 with a MethodRejection" in {
      Put("/things/12345", Thing("thong")) ~> thingsApiRoutes ~> check {
        handled should be(false)
        rejections should not be ('empty)
        rejection should be(a[MethodRejection])
      }
    }

    "reject a GET on /unsupported with an empty rejections list (i.e. a 404)" in {
      Get("/unsupported") ~> thingsApiRoutes ~> check {
        handled should be(false)
        rejections should be('empty)
      }
    }
  }
}

