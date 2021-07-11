package com.livesafe.tips

import scala.io.StdIn
import scala.util.{ Failure, Success }

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{ Directives, Route }
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

class TipsHttpService(tipService: TipService) extends Directives with PlayJsonSupport {

  val routes: Route = {
    pathPrefix("tips") {
      get {
        pathEnd { complete(tipService.getTips()) } ~
        path(JavaUUID) { tipId =>
          onComplete(tipService.getTip(tipId)) {
            case Success(tip) => complete(tip)
            case Failure(ex: Repository.RecordNotFoundException) => complete(StatusCodes.NotFound)
            case Failure(ex) => complete(StatusCodes.InternalServerError)
          }
        }
      }
    }
  }

  /**
   * Binds the routes [[routes]] on port localhost:8080.
   *
   * Feel free to ignore this for the sake of the assignment
   */
  def start() {

    implicit val system = ActorSystem("tips")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher


    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }

}
