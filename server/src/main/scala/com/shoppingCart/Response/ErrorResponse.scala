package com.shoppingCart.Response

import com.shoppingCart.Models.Domains.State

object ErrorResponse {

  sealed trait flowError {

    val errorCode: Int
    val message: Option[String] = None

  }

  case class StateNotCreated(state: State, code: Int) extends flowError {
    val errorCode = code
  }

}
