package scenarios

import config.Configuration
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import requests.BrowseResultsRequest

import scala.concurrent.duration._
import scala.language.postfixOps

object BrowseResultsScenario {

  val cardBrowseFeed = ssv("browse_cards_three_or_more_criterias.ssv").random.circular

  val getBrowseResultsScenario: ScenarioBuilder = scenario("Get Card Browse Results")
    .feed(cardBrowseFeed)
    .during(Configuration.simulationMaxTime)
    {
      exec(BrowseResultsRequest.get_browse_results)
    }
}
