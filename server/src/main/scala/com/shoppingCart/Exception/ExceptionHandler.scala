package com.shoppingCart.Exception

import java.util.concurrent.TimeoutException

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{ BadRequest, GatewayTimeout, InternalServerError, MethodNotAllowed, NotFound, Unauthorized }
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server._
import akka.pattern.AskTimeoutException

trait ExceptionHandler {
  val routeExceptionHandler = ExceptionHandler {

    case exception: AskTimeoutException => complete(HttpResponse(GatewayTimeout, entity = s"Server Timeout In Processing Request ${exception.getMessage}"))
    case exception: TimeoutException => complete(HttpResponse(GatewayTimeout, entity = s"Server Timeout In Processing Request ${exception.getMessage}"))
    case exception: Exception => complete(HttpResponse(InternalServerError, entity = s"Server Error ${exception.getMessage}"))

  }

  implicit val routeRejectionHandler: RejectionHandler = RejectionHandler
    .newBuilder()
    .handle {
      case MissingCookieRejection(cookieName) =>
        complete(HttpResponse(BadRequest, entity = "Cookies Not Found In Request"))
    }

    .handle {
      case MalformedRequestContentRejection(msg, cause) =>
        complete(HttpResponse(BadRequest, entity = s"Request Content Malformed ${cause}" + msg))
    }

    .handle {
      case MissingQueryParamRejection(msg) =>
        complete(HttpResponse(BadRequest, entity = s"Missing Query Parameter${msg}"))
    }

    .handle {
      case UnacceptedResponseContentTypeRejection(msg) =>
        complete(HttpResponse(BadRequest, entity = s"Response ContentType Invalid ${msg}"))
    }

    .handle {
      case MissingHeaderRejection(error) =>
        complete(HttpResponse(BadRequest, entity = s"Request Header Not Valid${error}"))
    }

    .handle {
      case InvalidRequiredValueForQueryParamRejection(parameter, required, actual) =>
        complete(HttpResponse(BadRequest, entity = s"Query Parameters are not valid Parameter :${parameter} Required :${required} Actual :${actual}"))
    }

    .handle {
      case UnsupportedRequestContentTypeRejection(msg) =>
        complete(HttpResponse(BadRequest, entity = s"Request ContentType Invalid $msg"))
    }

    .handle {
      case MissingFormFieldRejection(field) =>
        complete(HttpResponse(BadRequest, entity = s"Request Content Malformed $field"))
    }

    .handle {
      case AuthorizationFailedRejection =>
        complete((Unauthorized, "You are not Authorized to access this resource"))
    }
    .handle {
      case ValidationRejection(msg, _) =>
        complete((InternalServerError, "Invalid Request" + msg))
    }
    .handleAll[MethodRejection] { methodRejections =>
      val names = methodRejections.map(_.supported.name)
      complete((MethodNotAllowed, s"Method Not Supported: ${names mkString " or "}!"))
    }
    .handleNotFound { complete((NotFound, "Invalid Request")) }
    .handle {
      case _ => complete((InternalServerError, "Some Error In Request"))
    }
    .result()

}
