package scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import requests.BrowseResultsRequest

import scala.concurrent.duration._
import scala.language.postfixOps

object BrowseResultsScenario {

  val cardBrowseFeed = csv("browse_cards.csv").random.circular

  val getBrowseResultsScenario: ScenarioBuilder = scenario("Get Card Browse Results")
    .feed(cardBrowseFeed)
    .during(40 seconds)
    {
      exec(BrowseResultsRequest.get_browse_results)
    }
}
