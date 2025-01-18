package com.rtomyj.skc.requests

import com.rtomyj.skc.constants.{Header, Url}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object BrowseRequest {
  val get_browse_results: HttpRequestBuilder = http("Card Browse Details Request")
    .get(Url.browseCardsEndpoint)
    .header(Header.CLIENT_ID_HEADER_NAME, Header.CLIENT_ID_HEADER_VALUE)
    .queryParam("cardColors", "${cardColors}")
    .queryParam("attributes", "${attributes}")
    .queryParam("monsterTypes", "${monsterTypes}")
    .queryParam("levels", "${levels}")
    .queryParam("ranks", "${ranks}")
    .queryParam("linkRatings", "${linkRatings}")
    .check(status.is(200))

  val get_browse_criteria: HttpRequestBuilder = http("Card Browse Criteria Request")
    .get(Url.cardBrowseCriteriaEndpoint)
    .header(Header.CLIENT_ID_HEADER_NAME, Header.CLIENT_ID_HEADER_VALUE)
    .check(status.is(200))
}
