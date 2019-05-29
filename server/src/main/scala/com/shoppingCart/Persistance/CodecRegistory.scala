package com.shoppingCart.Persistance

import com.shoppingCart.Models.Domains.State
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistries.{ fromProviders, fromRegistries }
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistries.{ fromProviders, fromRegistries }
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._

/**
 * Created by Varun Bansal on 26/4/19
 */
trait CodecRegistory {

  val stateCodecRegistry = fromRegistries(fromProviders(classOf[State]), DEFAULT_CODEC_REGISTRY)

}