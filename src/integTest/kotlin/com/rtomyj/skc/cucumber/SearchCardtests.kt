package com.rtomyj.skc.cucumber

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers


class SearchCardtests : CucumberBase() {
  val requestEndpoint = "$BASE_ENDPOINT/card/search"
  lateinit var requestSpec: RequestSpecification


  @Given("user searches using card name {string}")
  fun user_searches_for_hyphenated_card_name(cardName: String) {
    requestSpec = RestAssured
        .given()
        .queryParam("cName", cardName)
  }

  @Given("user searches using card name {string}, card ID {string}, attribute {string}, card color {string}, and monster type {string}")
  fun user_searches_using_card_name_and_all_other_filters(
    cardName: String,
    cardId: String,
    attribute: String,
    cardColor: String,
    monsterType: String
  ) {
    requestSpec = RestAssured
        .given()
        .queryParam("cId", cardId)
        .queryParam("cAttribute", attribute)
        .queryParam("cColor", cardColor)
        .queryParam("monsterType", monsterType)
        .queryParam("cName", cardName)
  }

  @When("user sends search request")
  fun user_sends_search_request() {
    val res = requestSpec
        .get(requestEndpoint)

    jsonPath = res.jsonPath()
    validatableResponse = res.then()
  }

  @Then("search results request should return with OK http status")
  fun search_results_request_should_return_with_ok_http_status() {
    validatableResponse
        .statusCode(200)
  }

  @And("number of search results should be {int}")
  fun number_of_search_results_should_be(expectedNumResults: Int) {
    validatableResponse
        .body("size()", Matchers.equalTo(expectedNumResults))
  }
}