package com.shoppingCart.Services

import com.shoppingCart.Models.Domains._
import com.shoppingCart.Persistance.Repository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

/**
 * Created by Varun Bansal on 25/4/19
 */
class StateService(repository: Repository) {

  def getAllStates(): Future[List[State]] = {

    repository.fetchAllStates()

  }

  /* def getStateById(id:Int):Option[State]{}
  def deleteState(id:Int){}
  def addNewState(state:State)=???
  def modifyState(state:State,id:Int)=???*/
}
