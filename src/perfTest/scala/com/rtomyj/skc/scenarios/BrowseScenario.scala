package com.rtomyj.skc.scenarios

import com.rtomyj.skc.config.Configuration
import com.rtomyj.skc.requests.BrowseRequest
import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.core.structure.ScenarioBuilder

import scala.language.postfixOps

object BrowseScenario
{
  val cardBrowseFeed: BatchableFeederBuilder[String]#F#F = ssv("gatling/browse_cards_three_or_more_criteria.ssv").random.circular

  val getBrowseResultsScenario: ScenarioBuilder = scenario("Card Browse Details Scenario")
    .feed(cardBrowseFeed)
    .during(Configuration.simulationMaxTime)
    {
      exec(BrowseRequest.get_browse_results)
    }

  val getBrowseCriteria: ScenarioBuilder = scenario("Card Browse Criteria Scenario")
    .during(Configuration.simulationMaxTime)
    {
      exec(BrowseRequest.get_browse_criteria)
    }

  val browseUserLoad: ScenarioBuilder = scenario("User Loads Browse Page & Uses Browse Functionality")
    .feed(cardBrowseFeed)
    .during(Configuration.simulationMaxTime)
    {
      exec(BrowseRequest.get_browse_results)
      exec(BrowseRequest.get_browse_criteria)
    }
}
