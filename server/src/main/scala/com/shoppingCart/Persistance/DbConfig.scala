package com.shoppingCart.Persistance

import org.mongodb.scala.{ Completed, FindObservable, MongoClient, MongoClientSettings, MongoCollection, MongoCompressor, MongoDatabase, Observable, Observer, ReadPreference, ServerAddress, WriteConcern }
import org.mongodb.scala.connection.ClusterSettings
import com.mongodb.MongoCredential
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.connection.ClusterSettings
import com.mongodb.MongoCredential._
import java.util.logging.{ Level, Logger }

import com.typesafe.config.ConfigFactory
import org.mongodb.scala.connection.{ NettyStreamFactoryFactory, SslSettings }
import sun.security.util.Password

import scala.collection.JavaConverters._
/**
 * Created by Varun Bansal on 25/4/19
 */
trait DbConfig {

  val ServerPattern = """([a-zA-Z0-9\.\-]*):(\d*)""".r
  val config = ConfigFactory.load()
  val Servers: List[ServerAddress] = {
    config.getString("mongodb.servers").split(", *").toList.flatMap { value: String =>
      value.trim match {
        case ServerPattern(host, port) => Some(new ServerAddress(host, port.toInt))
        case invalid => None
      }
    }
  }
  val Database = config getString "mongodb.database"
  val Username = config getString "mongodb.username"
  val Password = config getString "mongodb.password"

  val mongoLogger: Logger = Logger.getLogger("com.mongodb")

  mongoLogger.setLevel(Level.INFO)

  val credentials = MongoCredential.createCredential(Username, Database, Password.toCharArray)
  val Settings = MongoClientSettings.builder()
    .compressorList(List(MongoCompressor.createSnappyCompressor).asJava)
    .applyToClusterSettings((b: ClusterSettings.Builder) => b.hosts(Servers.asJava).build())
    .writeConcern(WriteConcern.ACKNOWLEDGED)
    .build()

  val client: MongoClient = MongoClient(Settings)
  val MongoDatabase = client.getDatabase(Database)

}
