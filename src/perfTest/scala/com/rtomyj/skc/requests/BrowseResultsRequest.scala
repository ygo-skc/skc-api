package com.rtomyj.skc.requests

import com.rtomyj.skc.config.Configuration
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object BrowseResultsRequest {
  var get_browse_results: HttpRequestBuilder = http("Browse Details")
    .get(Configuration.browseCardsUri)
    .header("CLIENT_UUID", "PERF-TEST")
    .queryParam("cardColors", "${cardColors}")
    .queryParam("attributes", "${attributes}")
    .queryParam("monsterTypes", "${monsterTypes}")
    .queryParam("levels", "${levels}")
    .queryParam("ranks", "${ranks}")
    .queryParam("linkRatings", "${linkRatings}")
    .check(status.is(200))
}
