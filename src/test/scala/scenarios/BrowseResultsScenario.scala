package scenarios

import io.gatling.core.Predef.{exec, scenario}
import io.gatling.core.structure.ScenarioBuilder
import requests.BrowseResultsRequest

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object BrowseResultsScenario {
  val getBrowseResultsScenario: ScenarioBuilder = scenario("Get Card Browse Results")
    .during(20 seconds) {
      exec(BrowseResultsRequest.get_browse_results)
    }
}
