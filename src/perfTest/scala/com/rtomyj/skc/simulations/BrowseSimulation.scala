package com.rtomyj.skc.simulations

import com.rtomyj.skc.protocols.Protocol._
import com.rtomyj.skc.scenarios.BrowseScenario
import io.gatling.core.Predef._

import scala.language.postfixOps

class BrowseSimulation extends Simulation
{
  private val getBrowseCriteriaScenario = BrowseScenario.getBrowseCriteria
    .inject(atOnceUsers(30))

  private val getBrowseResultsScenario = BrowseScenario.getBrowseResultsScenario
    .inject(atOnceUsers(30))

  private val browseUserLoadScenario = BrowseScenario.browseUserLoad
    .inject(atOnceUsers(15))


  setUp(getBrowseCriteriaScenario
      .andThen(getBrowseResultsScenario)
      .andThen(browseUserLoadScenario)
    )
    .assertions(
      global.failedRequests.count.is(0)
      , details("Card Browse Criteria Request").responseTime.mean.lt(150)
      , details("Card Browse Details Request").responseTime.mean.lt(250)
    )
    .protocols(httpProtocol)
}