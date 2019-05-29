package com.shoppingCart.Persistance

import akka.actor.{ Actor, ActorLogging, ActorRef, Status }
import com.shoppingCart.Flows.StateFlow.StateAlreadyExistException
import com.shoppingCart.Models.Domains.State
import com.shoppingCart.Persistance.databaseMessages._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.FindOneAndReplaceOptions
import org.mongodb.scala.result.DeleteResult
import org.mongodb.scala.{ Completed, MongoCollection, Observer, SingleObservable }
import scala.concurrent.Future

/**
 * Created by Varun Bansal on 25/4/19
 */

object databaseMessages {

  case class AllStatesResponse(states: Future[List[State]])

  case class FindStateById(id: Int)

  case class StateFound(state: Future[Option[State]])

  case class InsertNewState(state: State)

  case class StateInserted(state: State)

  case class PersistState(state: State)

  case class StatePersisted(response: SingleObservable[Completed])

  case class ModifyState(state: State)

  case class StateModified(state: State)

  case class RemoveState(id: Int)

  case class StateRemoved(id: Int)

  case object FindAllStates

  /* case class StateDeleted(response:SingleObservable[T])*/

}

class Repository extends Actor with ActorLogging with DbConfig with CodecRegistory {

  lazy val statesCollection: MongoCollection[State] = MongoDatabase.withCodecRegistry(stateCodecRegistry).getCollection("States")
  var Requester: ActorRef = _

  override def preStart(): Unit = {

    log.info("Starting the MongodbDataStore...")
    log.info("MongoDB servers  : " + Servers.map(_.toString).mkString("[", ", ", "]"))
    log.info("MongoDB Database : " + Database)
    log.info("MongoDB Username : " + (if (Username != "") Username else "-"))
    log.info("MongoDB Password : " + Option(Password).map(_ => "****************").getOrElse("-"))

  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {

    super.preRestart(reason, message)
    sender() ! Status.Failure(reason)

  }

  override def postStop(): Unit = {

    log.info("Closing Connection to DataBase")
    client.close()

  }

  override def receive: Receive = {

    case FindAllStates => sender() ! fetchAllStates()
    case FindStateById(id: Int) => sender() ! fetchStateById(id)
    case InsertNewState(state: State) => insertState(state, sender)
    case ModifyState(state: State) => modifyState(state, sender)
    case RemoveState(id: Int) => deleteState(id, sender)
  }

  def fetchAllStates(): Future[List[State]] = {
    log.info("Fetching All States from database ")
    statesCollection.find().limit(100).collect()
      .map(_.toList)
      .head()
  }

  def fetchStateById(id: Int): Future[Option[State]] = {
    log.info(s"Fetching State for  id = ${id} from database ")
    statesCollection.find(equal("id", id)).first().headOption()

  }

  def modifyState(state: State, sender: ActorRef) = {
    log.info(s"Modifying  State In Database ${state.id}:${state.name}")
    statesCollection.findOneAndReplace(equal("id", state.id), state, new FindOneAndReplaceOptions().upsert(true)).subscribe(new Observer[State] {

      override def onNext(result: State): Unit = {
        log.info(s"state ${state.id}-${state.name}-${state.code} has been updated successfully ")
        sender ! StateModified(state)
      }

      override def onError(e: Throwable): Unit = {
        log.info(s"Error Occured in Updating State  ${state.id}-${state.name}-${state.code} ")
        sender ! Status.Failure(e)
      }

      override def onComplete(): Unit = {
      }

    })

  }

  def insertState(state: State, sender: ActorRef) = {
    log.info(s"Adding New State In Database ${state.id}:${state.name}")

    statesCollection.insertOne(state).subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = {
        log.info("State Inserted Successfully")
      }

      override def onError(e: Throwable): Unit = {
        log.info(s"Exception Occured In Inserting State Into Db ${e.getMessage}")

        if (e.getMessage.indexOf("11000") != -1) {
          sender ! Status.Failure(StateAlreadyExistException(state))
        } else
          sender ! Status.Failure(e)
      }

      override def onComplete(): Unit = {
        log.info(s"State Inserted Successfully ${state.name}")
        sender ! StateInserted(state)
      }
    })

  }

  def deleteState(id: Int, sender: ActorRef) = {
    log.info(s"Deleting State In Database ${id}")
    statesCollection.deleteOne(equal("id", id)).subscribe(new Observer[DeleteResult] {
      override def onNext(result: DeleteResult): Unit = {
        log.info(s"State ${id} Deleted SuccessFully")
        sender ! StateRemoved(id)
      }

      override def onError(e: Throwable): Unit = {
        log.info(s"Error In Deleting State ${id}")
        sender ! Status.Failure(e)
      }

      override def onComplete(): Unit = {
        log.info(s"State ${id} Deleted SuccessFully")
      }
    })

    id
  }

}