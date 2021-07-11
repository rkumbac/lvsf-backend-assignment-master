package com.livesafe.tips

import java.util.UUID

import scala.concurrent.Future

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FreeSpec, Matchers }

class TipsHttpServiceSpec extends FreeSpec with Matchers with MockFactory with ScalatestRouteTest {

  val mockTipService: TipService = mock[TipService]

  val service = new TipsHttpService(mockTipService)

  "TipsHttpService endpoints" - {
    "/tips" in {
      (mockTipService.getTips _).expects().returns(Future.successful(Nil))
      Get("/tips") ~> service.routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }
    "/tips/:id" in {
      val testTipId = UUID.randomUUID()
      (mockTipService.getTip _).expects(testTipId).returns(Future.successful("test tip response"))
      // FIXME: Finish this test please
    }
  }
}
