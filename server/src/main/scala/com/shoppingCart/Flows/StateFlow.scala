package com.shoppingCart.Flows

import akka.actor.{ Actor, ActorLogging, ActorRef, Status }
import akka.pattern._
import akka.util.Timeout
import com.shoppingCart.Flows.StateFlow.{ DeleteState, _ }
import com.shoppingCart.Models.Domains.{ State, StateDto, StateUpdate }
import com.shoppingCart.Persistance.databaseMessages._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Created by Varun Bansal on 29/4/19
 */

object StateFlow {

  sealed trait StateFlowResponse

  case class StateNotFoundException(id: Int) extends Throwable

  case class StateAlreadyExistException(state: State) extends Throwable

  case class GetStateById(id: Int)

  case class AllStates(states: List[State])

  case class ErrorInGettingStates(exception: Throwable)

  case class StateResponse(state: State)

  case class StateFlowFailed(reason: Any)

  case class AddNewState(state: StateDto)

  case class UpdateState(id: Int, state: StateUpdate)

  case class StatePersistedSuccessFully(state: State)

  case class DeleteState(id: Int)

  case class StateDeletedSuccessFully(id: Int)

  case class RequestNotCompletedException(msg: String = "Request Not Completed", exception: Throwable) extends Throwable(msg)

  case class StateUpdatedSuccessFully(state: State)

  case class StateAddedSuccessFully(state: State)

  case class StateNotFoundForDelete(id: Int)

  case object StateListEmptyException extends Throwable

  case object StateFlowException extends Throwable

  case object GetAllStates

  case object StatesNotFound

  case object StateNotFound

  case object StateNotFoundForUpdate

}

class StateFlow(dbActor: ActorRef) extends Actor with ActorLogging {

  var stateUpdate: StateUpdate = _
  var stateId: Int = _
  var Requester: ActorRef = _
  var currentState: State = _

  implicit val timeout: Timeout = Timeout(5 seconds)

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {

    super.preRestart(reason, message)

    log.info(s"Exception In State Flow ${reason.getMessage}")
    sender() ! Status.Failure(reason)

  }

  override def receive: Receive = {

    case GetAllStates => {
      Requester = sender()
      log.info("Request to fetch  all states from database")
      (dbActor ? FindAllStates).mapTo[Future[List[State]]].flatMap(s => s.map({

        case states: List[State] => {
          log.info(s"Retrieved ${states.size} states from db")
          Requester ! Status.Success(states)
        }

        case _ => handleStateFlowError(StateFlowException)

      }))
    }

    case GetStateById(id: Int) => {
      Requester = sender()
      stateId = id
      log.info(s"Finding State for Id $id From Database")

      verifyAndFetchStateById(stateId).map({
        case true => Requester ! Status.Success(currentState)
        case false => Requester ! Status.Failure(StateNotFoundException(stateId))
      })
    }

    case UpdateState(id, update) =>
      Requester = sender()
      stateId = id
      stateUpdate = update
      log.info(s"Request to update state for id =${id}")

      verifyAndFetchStateById(stateId).map({
        case true => {
          log.info(s"Updating State for Id $stateId")
          val updatedState: State =
            State(
              stateUpdate.id.getOrElse(currentState.id),
              stateUpdate.code.getOrElse(currentState.code),
              stateUpdate.name.getOrElse(currentState.name))

          dbActor ! ModifyState(updatedState)

        }

        case false => Requester ! Status.Failure(StateNotFoundException(stateId))
      })

    case StateModified(state) =>
      log.info(s"State ${state.id}-${state.code}-${state.name} updated successfully")
      Requester ! Status.Success(StateUpdatedSuccessFully(state))

    case AddNewState(stateDto) => {
      Requester = sender()
      log.info(s"Adding New State to Database ${stateDto.code}-${stateDto.name}")
      dbActor ! InsertNewState(State(stateDto.code, stateDto.name))
    }

    case StateInserted(state) => {
      log.info(s"State Inserted Into Database Successfully ${state.id}-${state.code}-${state.name}")
      Requester ! Status.Success(StateAddedSuccessFully(state))

    }

    case DeleteState(id: Int) => {
      stateId = id
      Requester = sender()
      log.info(s"Request to remove state from db for id=${id}")

      verifyAndFetchStateById(stateId).map({
        case true => dbActor ! RemoveState(stateId)
        case false => Requester ! Status.Failure(StateNotFoundException(stateId))
      })

    }

    case StateRemoved(id) =>
      log.info(s"State ${id} removed successfully")
      Requester ! Status.Success(StateDeletedSuccessFully(id))

    case Status.Failure(reason) =>
      log.info(s"Exception Occured In Processing Message In StateFlow ${if (reason.isInstanceOf[Exception]) reason.getMessage else reason}")
      handleStateFlowError(reason)
  }

  private def verifyAndFetchStateById(id: Int): Future[Boolean] = {

    log.info(s"Finding state for id $id")
    val x = (dbActor ? FindStateById(stateId)).mapTo[Future[Option[State]]]

    x.flatMap(s => s.map({
      case Some(state: State) => {
        log.info(s"State found for id ${state.id}")
        currentState = state
        true
      }

      case None => {
        log.info(s"State not found for id $stateId")
        false
      }
    }))

  }

  private def handleStateFlowError(exception: Throwable): Unit = {
    log.error(s"Exception occured in processing state flow message ${exception.getMessage}")

    if (Requester != null)
      Requester ! Status.Failure(exception)

  }

}
