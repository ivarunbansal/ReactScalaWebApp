
package com.shoppingCart.Utils

import akka.http.scaladsl.model.HttpMethods.{ DELETE, GET, OPTIONS, POST, PUT }
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers.{ HttpOrigin, `Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`, `Access-Control-Expose-Headers`, `Access-Control-Max-Age` }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Directive0, Route }
import com.typesafe.config.ConfigFactory

trait CorsSupport {

  val configurations = ConfigFactory.load()

  val allowedOriginHeader = {

    lazy val sAllowedOrigin = configurations.getString("corsSupport.allowed-origin")
    if (sAllowedOrigin == "*")
      `Access-Control-Allow-Origin`.*
    else
      `Access-Control-Allow-Origin`(HttpOrigin(sAllowedOrigin))
  }

  def corsHandler(r: Route) = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }

  private def addAccessControlHeaders: Directive0 = {
    mapResponseHeaders { headers =>
      allowedOriginHeader +:
        `Access-Control-Allow-Credentials`(true) +:
        `Access-Control-Allow-Headers`("*") +:
        `Access-Control-Max-Age`(configurations.getString("corsSupport.accessControlMaxAge").toLong) +:
        `Access-Control-Expose-Headers`("x-sessionId") +:
        headers

    }
  }

  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(200).withHeaders(
      `Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }
}
