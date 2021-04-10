package com.rtomyj.skc.scenarios

import com.rtomyj.skc.requests.BrowseResultsRequest
import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.core.structure.ScenarioBuilder

import scala.language.postfixOps

object BrowseResultsScenario {

  val cardBrowseFeed: BatchableFeederBuilder[String]#F#F = ssv("browse_cards_three_or_more_criteria.ssv").random.circular

  val getBrowseResultsScenario: ScenarioBuilder = scenario("Get Card Browse Results")
    .feed(cardBrowseFeed)
    .forever  // each virtual user will keep calling/executing request inside lambda - forever
    {
      exec(BrowseResultsRequest.get_browse_results)
    }

}
