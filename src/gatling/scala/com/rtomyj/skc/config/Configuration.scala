package com.rtomyj.skc.config

import com.rtomyj.skc.constants.Url

import scala.concurrent.duration._
import scala.language.postfixOps

object Configuration {
  val baseUrl: String = Url.prod

  val rampup: FiniteDuration = 60 seconds
  val simulationMaxTime: FiniteDuration = 1 minutes
}