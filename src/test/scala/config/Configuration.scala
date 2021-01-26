package config

import scala.concurrent.duration._
import scala.language.postfixOps

object Configuration
{
  val baseUrl: String = "http://localhost:9999"
  val users = 10
  val rampup = 5 seconds
}