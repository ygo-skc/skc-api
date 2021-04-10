package com.rtomyj.skc.config

import scala.concurrent.duration._
import scala.language.postfixOps

object Configuration
{
  val localhost = "http://localhost:9999"
  val localhostDocker = "http://localhost:9998"
  val dev = "https://dev.skc-ygo-api.com"
  val prod = "https://skc-ygo-api.com"

  val baseUrl = prod
  val browseCardsUri = "/api/v1/card/browse"

  val users = 30
  val rampup = 60 seconds
  val simulationMaxTime = 2 minutes
}