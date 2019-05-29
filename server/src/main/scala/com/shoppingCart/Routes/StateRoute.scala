package com.shoppingCart.Routes

import akka.actor.ActorRef
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{ as, entity, get, post, _ }
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.pattern._
import akka.stream.Materializer
import akka.util.Timeout
import com.shoppingCart.Exception.ExceptionHandler
import com.shoppingCart.Flows.StateFlow._
import com.shoppingCart.Models.Domains.{ State, StateDto, StateUpdate }
import com.shoppingCart.Utils.CorsSupport
import spray.json.JsString

import scala.util.{ Failure, Success => AkkaSuccess }

/**
 * Created by Varun Bansal on 24/4/19
 */

trait StateRoute extends ExceptionHandler with CorsSupport {

  import com.shoppingCart.Models.Domains.ServiceJsonProtocol._

  implicit val materializer: Materializer
  implicit val timeout: Timeout

  val stateRoute: Route = DebuggingDirectives.logRequestResult(("State-Route", Logging.DebugLevel)) {
    pathPrefix("states") {
      pathEndOrSingleSlash {
        get {
          rejectEmptyResponse {
            onComplete(stateFlow ? GetAllStates) {

              case AkkaSuccess(response) => {
                response match {
                  case states: List[State] => complete(OK, states)
                  case _ => complete(InternalServerError, JsString("Internal Server Error In processing your request"))
                }
              }
              case Failure(exception) => {
                exception match {
                  case _ => complete(InternalServerError, JsString(s"Internal Server Error In processing your request ${exception.getMessage}"))

                }
              }
            }

          }
        }
      } ~
        pathPrefix("state") {
          post {
            entity(as[StateDto]) { stateDto =>
              rejectEmptyResponse {
                onComplete(stateFlow ? AddNewState(stateDto)) {

                  case AkkaSuccess(StateAddedSuccessFully(state)) => complete(Created, state)
                  case Failure(exception) => exception match {
                    case StateAlreadyExistException(state) => complete(Conflict, JsString(s"State already exist"))
                    case _ => complete(InternalServerError, JsString(s"Internal Server Error In Processing your Request ${exception.getMessage}"))
                  }
                }

              }

            }

          } ~ parameters("id".as[Int]) { id =>
            get {
              rejectEmptyResponse {

                onComplete(stateFlow ? GetStateById(id)) {

                  case AkkaSuccess(value) => value match {
                    case state: State => complete(OK, state)
                    case _ => complete(InternalServerError, JsString("Internal Server Error In Processing your Request"))
                  }

                  case Failure(exception) => exception match {
                    case StateNotFoundException(id: Int) => complete(NotFound, JsString(s"State not exist for id =${id}"))
                    case _ => complete(InternalServerError, JsString(s"Internal Server Error In Processing your Request ${exception.getMessage}"))
                  }
                }
              }
            } ~ put {
              entity(as[StateUpdate]) { update =>
                rejectEmptyResponse {

                  onComplete(stateFlow ? UpdateState(id, update)) {

                    case AkkaSuccess(value) => value match {
                      case StateUpdatedSuccessFully(state: State) => complete(OK, state)
                      case _ => complete(InternalServerError, JsString("Internal Server Error In Processing your Request"))
                    }

                    case Failure(exception) => exception match {
                      case StateNotFoundException(id: Int) => complete(NotFound, JsString(s"State not exist for id =${id}"))
                      case _ => complete(InternalServerError, JsString(s"Internal Server Error In Processing your Request ${exception.getMessage}"))
                    }
                  }
                }

              }

            } ~ delete {
              rejectEmptyResponse {
                onComplete(stateFlow ? DeleteState(id)) {

                  case AkkaSuccess(value) => value match {
                    case StateDeletedSuccessFully(id: Int) => complete(OK, JsString(s"State removed successfully for id=${id}"))
                    case _ => complete(InternalServerError, JsString("Internal Server Error In Processing your Request"))
                  }

                  case Failure(exception) => exception match {
                    case StateNotFoundException(id: Int) => complete(NotFound, JsString(s"State not exist for id =${id}"))
                    case _ => complete(InternalServerError, JsString(s"Internal Server Error In Processing your Request ${exception.getMessage}"))
                  }

                }

              }

            }
          }

        }
    }
  }

  def stateFlow: ActorRef

}
