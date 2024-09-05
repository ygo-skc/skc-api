package com.rtomyj.skc.cucumber

import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import org.hamcrest.Matchers

class CardBrowseCriteriaTests : CucumberBase() {
  val requestEndpoint = "$BASE_ENDPOINT/card/browse/criteria"


  @When("user requests browsing criteria")
  fun user_requests_browsing_criteria() {
    val res = RestAssured.get(requestEndpoint)

    jsonPath = res.jsonPath()
    validatableResponse = res.then()
  }

  @Then("browse criteria request status should be {int} and body should contain {int} elements")
  fun browse_criteria_request_status_should_be_and_body_should_contain_elements(
    expectedStatus: Int,
    expectedNumElements: Int
  ) {
    validatableResponse
        .statusCode(expectedStatus)
        .body("size()", Matchers.equalTo(expectedNumElements))
  }


  @And("cardColor criteria array should contain the following:")
  fun card_color_criteria_array_should_contain_the_following(
    expectedCardColorCriteria: List<String>
  ) {
    validatableResponse
        .body("cardColors", Matchers.notNullValue())
        .body("cardColors.size()", Matchers.equalTo(expectedCardColorCriteria.size))
        .body("cardColors", Matchers.equalToObject(expectedCardColorCriteria))
  }


  @And("attributes array should contain the following:")
  fun attributes_array_should_contain_the_following(expectedAttributeCriteria: List<String>) {
    validatableResponse
        .body("attributes", Matchers.notNullValue())
        .body("attributes.size()", Matchers.equalTo(expectedAttributeCriteria.size))
        .body("attributes", Matchers.equalTo(expectedAttributeCriteria))
  }


  @And("monsterTypes array should contain the following:")
  fun monster_types_array_should_contain_the_following(expectedMonsterTypeCriteria: List<String>) {
    validatableResponse
        .body("monsterTypes", Matchers.notNullValue())
        .body("monsterTypes.size()", Matchers.equalTo(expectedMonsterTypeCriteria.size))
        .body("monsterTypes", Matchers.equalTo(expectedMonsterTypeCriteria))
  }


  @And("levels array should contains the following:")
  fun levels_array_should_contain_the_following(expectedLevels: List<Int>) {
    validatableResponse
        .body("levels", Matchers.notNullValue())
        .body("levels.size()", Matchers.equalTo(expectedLevels.size))
        .body("levels", Matchers.equalTo(expectedLevels))
  }


  @And("ranks array should contains the following:")
  fun rank_array_should_contain_the_following(expectedRanks: List<Int>) {
    validatableResponse
        .body("ranks", Matchers.notNullValue())
        .body("ranks.size()", Matchers.equalTo(expectedRanks.size))
        .body("ranks", Matchers.equalTo(expectedRanks))
  }


  @And("link rating array should contains the following:")
  fun link_rating_array_should_contain_the_following(expectedLinkRatings: List<Int>) {
    validatableResponse
        .body("linkRatings", Matchers.notNullValue())
        .body("linkRatings.size()", Matchers.equalTo(expectedLinkRatings.size))
        .body("linkRatings", Matchers.equalTo(expectedLinkRatings))
  }
}