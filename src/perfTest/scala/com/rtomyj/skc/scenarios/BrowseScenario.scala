package com.rtomyj.skc.scenarios

import com.rtomyj.skc.requests.BrowseRequest
import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.core.structure.ScenarioBuilder

import scala.language.postfixOps

object BrowseScenario {

  val cardBrowseFeed: BatchableFeederBuilder[String]#F#F = ssv("gatling/browse_cards_three_or_more_criteria.ssv").random.circular

  val getBrowseResultsScenario: ScenarioBuilder = scenario("Get Card Browse Results")
    .feed(cardBrowseFeed)
    .forever  // each virtual user will keep calling/executing request inside lambda - forever
    {
      exec(BrowseRequest.get_browse_results)
    }

  val getBrowseCriteria: ScenarioBuilder = scenario("Get Browse Criteria")
    .forever
    {
      exec(BrowseRequest.get_browse_criteria)
    }

}
