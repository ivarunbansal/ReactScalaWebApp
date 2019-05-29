package com.shoppingCart

import akka.http.scaladsl.Http
import com.shoppingCart.Exception.ExceptionHandler
import com.shoppingCart.Routes.StateRoute
import com.shoppingCart.Utils.CorsSupport

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.util.{ Failure, Success }
import akka.http.scaladsl.server.Directives._

/**
 * Created by Varun Bansal on 24/4/19
 */
object Boot extends App with ApiActor with StateRoute with ExceptionHandler with CorsSupport {

  val route = corsHandler(
    handleExceptions(routeExceptionHandler) {
      stateRoute
    })

  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", port = 5000)

  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)

}
