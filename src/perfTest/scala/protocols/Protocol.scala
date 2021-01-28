package protocols;

import config.Configuration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Protocol {
    val httpProtocol = http
      .baseUrl(baseUrl)
      .disableCaching
}
