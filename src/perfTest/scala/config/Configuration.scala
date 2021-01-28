package config

import scala.concurrent.duration._
import scala.language.postfixOps

object Configuration
{
  val baseUrl = "http://localhost:9999"
  val browseCardsUri = "/api/v1/card/browse"

  val users = 10
  val rampup = 60 seconds
}