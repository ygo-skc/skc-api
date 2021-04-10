package com.rtomyj.skc.protocols;

import com.rtomyj.skc.config.Configuration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

object Protocol {
    val httpProtocol: HttpProtocolBuilder = http
      .baseUrl(baseUrl)
      .disableCaching
}
