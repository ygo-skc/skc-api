package com.rtomyj.skc.cucumber

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import org.hamcrest.Matchers

class BanListDatesTests: CucumberBase() {
	val requestEndpoint = "$BASE_ENDPOINT/ban_list/dates"


	@When("User requests ban list dates")
	fun user_requests_ban_list_dates() {
		val res = RestAssured.get(requestEndpoint)

		jsonPath = res.jsonPath()
		validatableResponse = res.then()
	}


	@Then("HTTP status of response should be {int}")
	fun http_status_of_response_should_be(okStatus: Int) {
		validatableResponse.statusCode(okStatus)
	}


	@Then("First - oldest - ban list found in DB has effectiveDate of {string} and newest ban list found in DB has effectiveDate of {string}")
	fun first_ban_list_found_in_db_has_efective_date_of(oldestBanListEffectiveDate: String, newestBanListEffectiveDate: String) {
		validatableResponse.body("banListDates[0].effectiveDate", Matchers.equalTo(newestBanListEffectiveDate))
		validatableResponse.body("banListDates[24].effectiveDate", Matchers.equalTo(oldestBanListEffectiveDate))
	}
}