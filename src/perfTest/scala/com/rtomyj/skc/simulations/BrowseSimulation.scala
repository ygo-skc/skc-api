package com.rtomyj.skc.simulations

import com.rtomyj.skc.config.Configuration
import com.rtomyj.skc.config.Configuration._
import com.rtomyj.skc.protocols.Protocol._
import io.gatling.core.Predef._
import com.rtomyj.skc.scenarios.BrowseScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class BrowseSimulation extends Simulation {

  private val getBrowseResultsScenario = BrowseScenario.getBrowseResultsScenario
    .inject(rampUsers(users).during(rampup))

  private val getBrowseCriteria = BrowseScenario.getBrowseCriteria
    .inject(rampUsers(users).during(rampup))


  setUp(getBrowseCriteria)
    .maxDuration(Configuration.simulationMaxTime)
    .assertions(
      global.responseTime.mean.lt(250)
      , global.failedRequests.percent.is(0)
    )
    .protocols(httpProtocol)

}