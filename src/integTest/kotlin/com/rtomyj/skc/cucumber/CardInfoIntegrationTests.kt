package com.rtomyj.skc.cucumber

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import org.hamcrest.Matchers

class CardInfoIntegrationTests : CucumberBase() {
  private val baseEndpoint = "$BASE_ENDPOINT/card"
  private lateinit var requestEndpoint: String


  @Given("I want all info for card with ID {string} - IE, info on ban lists, products, etc")
  fun allInfoIsRequestedForCard(cardId: String) {
    requestEndpoint = "$baseEndpoint/$cardId?allInfo=true"
  }

  @Given("I want basic info for card with ID {string} - IE, no info on ban lists, products, etc")
  fun basicInfoIsRequestedForCard(cardId: String) {
    requestEndpoint = "$baseEndpoint/$cardId"
  }

  @When("I request info for the card")
  fun iRequestInfoForCard() {
    val res = RestAssured.get(requestEndpoint)

    jsonPath = res.jsonPath()
    validatableResponse = res.then()
  }

  @Then("http status of response should be {int}")
  fun checkHttpStatus(httpStatus: Int) {
    validatableResponse.statusCode(httpStatus)
  }

  @And("card info return value should contain {string}, {string}, {string}, {string}")
  fun checkBasicCardInfo(cardName: String, cardID: String, cardColor: String, cardAttribute: String) {
    validatableResponse.body("cardName", Matchers.equalTo(cardName))
    validatableResponse.body("cardID", Matchers.equalTo(cardID))
    validatableResponse.body("cardColor", Matchers.equalTo(cardColor))
    validatableResponse.body("cardAttribute", Matchers.equalTo(cardAttribute))
  }

  @And("monster card specific info return value be {string} {string} {string} {string} {string}")
  fun checkMonsterCardSpecificFields(
    monsterType: String,
    monsterAtk: String,
    monsterDef: String,
    level: String,
    rank: String
  ) {
    validatableResponse.body("monsterType", Matchers.equalTo(monsterType))
    validatableResponse.body("monsterAttack", Matchers.equalTo(monsterAtk.toInt()))
    validatableResponse.body("monsterDefense", Matchers.equalTo(if (monsterDef == "") null else monsterDef.toInt()))
    validatableResponse.body("monsterAssociation.level", Matchers.equalTo(if (level == "") null else level.toInt()))
    validatableResponse.body("monsterAssociation.rank", Matchers.equalTo(if (rank == "") null else rank.toInt()))
  }

  @And("card effect should not be empty or null")
  fun checkCardEffect() {
    validatableResponse.body("cardEffect", Matchers.`is`(Matchers.notNullValue()))
    validatableResponse.body("cardEffect", Matchers.`is`(Matchers.not(Matchers.emptyString())))
  }

  @And("products card is found in should be greater than or equal to {int}")
  fun checkNumberOfProductsCardWasIn(productsFoundIn: Int) {
    validatableResponse.body("foundIn", Matchers.`is`(Matchers.notNullValue()))
    validatableResponse.body("foundIn.size()", Matchers.`is`(Matchers.greaterThanOrEqualTo(productsFoundIn)))
  }

  @And("products card is found in should not be included")
  fun productInfoShouldBeAvailable() {
    validatableResponse.body("foundIn", Matchers.emptyIterable<String>())
  }

  @And("ban lists card is found in should not be included")
  fun restrictedInMissing() {
    validatableResponse.body("restrictedIn", Matchers.anEmptyMap<Object, Object>())
  }

  @And("card will not be found in any ban list")
  fun cardNotInAnyBanList() {
    validatableResponse.body("restrictedIn['TCG'].size()", Matchers.`is`(0))
    validatableResponse.body("restrictedIn['MD'].size()", Matchers.`is`(0))
    validatableResponse.body("restrictedIn['DL'].size()", Matchers.`is`(0))
  }

  @And("ban lists card is found in should be greater than or equal to {int}")
  fun checkBanListInfoArraySize(restrictedIn: Int) {
    validatableResponse.body("restrictedIn['TCG']", Matchers.`is`(Matchers.notNullValue()))
    validatableResponse.body("restrictedIn['TCG'].size()", Matchers.`is`(Matchers.greaterThanOrEqualTo(restrictedIn)))
  }

  @And("monsterType, monsterAtk, monsterDef, level, rank should all be null")
  fun validateMonsterOnlyAttributesAreNull() {
    validatableResponse.body("monsterType", Matchers.equalTo<String>(null))
    validatableResponse.body("monsterAttack", Matchers.equalTo<String>(null))
    validatableResponse.body("monsterDefense", Matchers.equalTo<String>(null))
    validatableResponse.body("monsterAssociation", Matchers.equalTo<String>(null))
  }
}