package simulations

import config.Configuration
import io.gatling.core.Predef._
import scenarios.BrowseResultsScenario


class BrowseSimulation extends Simulation {

  private val gg = BrowseResultsScenario.getBrowseResultsScenario
    .inject(rampUsers(Configuration.users) during(Configuration.rampup))


  setUp(gg)
}