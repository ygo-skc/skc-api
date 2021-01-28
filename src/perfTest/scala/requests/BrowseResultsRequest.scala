package requests

import config.Configuration
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object BrowseResultsRequest {
  var get_browse_results: HttpRequestBuilder = http("Browse Details")
    .get(Configuration.browseCardsUri)
    .header("CLIENT_UUID", "PERF-TEST")
    .queryParam("monsterTypes", "${monsterType}")
    .check(status.is(200))
}
