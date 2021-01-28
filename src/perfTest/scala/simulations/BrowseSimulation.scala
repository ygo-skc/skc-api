package simulations

import config.Configuration._
import protocols.Protocol._
import io.gatling.core.Predef._
import scenarios.BrowseResultsScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class BrowseSimulation extends Simulation {

  private val scn = BrowseResultsScenario.getBrowseResultsScenario
    .inject(rampUsers(users).during(rampup))


  setUp(scn)
    .maxDuration(25 seconds)
    .assertions(
      global.responseTime.max.lt(1000)
      , global.successfulRequests.percent.gt(85)
    )
    .protocols(httpProtocol)
}