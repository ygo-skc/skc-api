package requests

import config.Configuration.baseUrl
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object BrowseResultsRequest {
  var get_browse_results: HttpRequestBuilder = http("Browse Details")
    .get(baseUrl + "/api/v1/card/browse?monsterTypes=warrior")
    .header("CLIENT_UUID", "PERF-TEST")
    .check(status is 200)
}
