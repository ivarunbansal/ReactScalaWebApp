package com.shoppingCart.Models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{ DefaultJsonProtocol, RootJsonFormat }

/**
 * Created by Varun Bansal on 24/4/19
 */

object Domains extends DefaultJsonProtocol {

  case class State(id: Int, code: String, name: String)
  case class StateList(states: List[State])
  case class StateDto(code: String, name: String)
  case class StateUpdate(id: Option[Int], code: Option[String], name: Option[String])

  object State extends DefaultJsonProtocol {

    val r = new scala.util.Random

    def apply(code: String, name: String): State = new State(
      r.nextInt(800000),
      code.toUpperCase,
      name.toLowerCase.capitalize)

  }

  object ServiceJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {

    implicit val stateJsonFormat: RootJsonFormat[State] = jsonFormat3(State.apply)
    implicit val stateListFormat: RootJsonFormat[StateList] = jsonFormat(StateList.apply, "stateList")
    implicit val stateUpdateFormat: RootJsonFormat[StateUpdate] = jsonFormat3(StateUpdate.apply)
    implicit val stateDtoFormat: RootJsonFormat[StateDto] = jsonFormat2(StateDto.apply)
  }

}