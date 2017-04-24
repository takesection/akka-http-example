package jp.pigumer.boot

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.Specs2RouteTest
import org.specs2.mutable.Specification

class MainSpec extends Specification with Specs2RouteTest {

  "Main" should {

    Get() ~> Route.seal(Main.route) ~> check {
      status must_== Unauthorized
    }

    Get("/?key=1234").addHeader(Authorization(OAuth2BearerToken("123"))) ~> Main.route ~> check {
      status must_== OK
    }

    Post().addHeader(Authorization(OAuth2BearerToken("123"))).withEntity(ContentTypes.`application/json`,
      """
        |{
        |"id": "XXX",
        |"name": "NAME"
        |}
      """.stripMargin) ~> Main.route ~> check {
      status must_== OK
      responseAs[String] must_== "NAME"
    }
  }
}
