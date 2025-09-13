package com.rtomyj.skc.cucumber

import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import org.hamcrest.Matchers

class BanListTests : CucumberBase() {
  val banListDatesEndpoint = "$BASE_ENDPOINT/ban_list/dates"
  val banListRemovedContentEndpoint = "$BASE_ENDPOINT/ban_list/{banListDate}/removed"


  @When("User requests ban list dates")
  fun user_requests_ban_list_dates() {
    val res = RestAssured.get(banListDatesEndpoint)

    jsonPath = res.jsonPath()
    validatableResponse = res.then()
  }


  @When("user requests removed content for ban list whose effective date is {string}")
  fun user_requests_removed_content_for_ban_list_whose_effective_date_is(banListDate: String) {
    val res = RestAssured
        .given()
        .pathParam("banListDate", banListDate)
        .get(banListRemovedContentEndpoint)

    jsonPath = res.jsonPath()
    validatableResponse = res.then()
  }


  @Then("HTTP status of response should be {int}")
  fun http_status_of_response_should_be(okStatus: Int) {
    validatableResponse.statusCode(okStatus)
  }


  @And("First - oldest - ban list found in DB has effectiveDate of {string} and newest ban list found in DB has effectiveDate of {string}")
  fun first_ban_list_found_in_db_has_effective_date_of(
    oldestBanListEffectiveDate: String,
    newestBanListEffectiveDate: String
  ) {
    validatableResponse.body("banListDates[0].effectiveDate", Matchers.equalTo(newestBanListEffectiveDate))
    validatableResponse.body("banListDates[-1].effectiveDate", Matchers.equalTo(oldestBanListEffectiveDate))
  }


  @And("requested list in response matches the date used in request: {string}")
  fun requested_list_matches_the_date_used_in_request(expectedBanListDate: String) {
    validatableResponse
        .body("listRequested", Matchers.equalTo(expectedBanListDate))
  }


  @And("previous ban list used to compare is {string}")
  fun previous_ban_list_used_to_compare_is(expectedPreviousBanList: String) {
    validatableResponse
        .body("comparedTo", Matchers.equalTo(expectedPreviousBanList))
  }


  @And("total removed cards is {int}")
  fun total_removed_cards_is(expectedNumRemoved: Int) {
    validatableResponse
        .body("numRemoved", Matchers.equalTo(expectedNumRemoved))
  }
}