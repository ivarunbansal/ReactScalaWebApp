package com.shoppingCart

import java.util.UUID.randomUUID

import akka.actor._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.shoppingCart.Flows.StateFlow
import com.shoppingCart.Persistance.Repository

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created by Varun Bansal on 29/4/19
 */
trait ApiActor {

  implicit val system: ActorSystem = ActorSystem("ShoppingCart")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val timeout = Timeout(5 seconds)
  implicit val executionContext: ExecutionContext = system.dispatcher

  val dbActor = system.actorOf(Props(new Repository), "MongoDbActor" + randomUUID())

  /**
   * Service Flows
   */
  val stateFlow = system.actorOf(Props(new StateFlow(dbActor)), "StateFlow" + randomUUID())

}
