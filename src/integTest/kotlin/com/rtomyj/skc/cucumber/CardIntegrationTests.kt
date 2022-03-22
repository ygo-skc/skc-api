package com.rtomyj.skc.cucumber

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.restassured.RestAssured
import org.hamcrest.Matchers

class CardIntegrationTests: CucumberBase() {
    val requestEndpoint = "$BASE_ENDPOINT/card"
    
    
    @Given("I request all info for card with ID: {string}")
    fun allInfoIsRequestedForCard(cardId: String) {
        val res = RestAssured.get("$requestEndpoint/$cardId?allInfo=true")

        jsonPath = res.jsonPath()
        validatableResponse = res.then()
    }

    @Given("I request info without all info flag for card with ID: {string}")
    fun someInfoIsRequestedForCard(cardId: String) {
        validatableResponse = RestAssured.get("$requestEndpoint/$cardId").then()
    }

    @Then("http status of response should be {int}")
    fun checkHttpStatus(httpStatus: Int) {
        validatableResponse.statusCode(httpStatus)
    }

    @And("cardID should be {string}")
    fun checkCardId(cardID: String) {
        validatableResponse.body("cardID", Matchers.equalTo(cardID))
    }

    @And("cardName should be {string}")
    fun checkCardName(cardName: String) {
        validatableResponse.body("cardName", Matchers.equalTo(cardName))
    }

    @And("cardColor should be {string}")
    fun checkCardColor(cardColor: String) {
        validatableResponse.body("cardColor", Matchers.equalTo(cardColor))
    }

    @And("cardAttribute should be {string}")
    fun checkCardAttribute(cardAttribute: String) {
        validatableResponse.body("cardAttribute", Matchers.equalTo(cardAttribute))
    }

    @And("monsterType should be {string}")
    fun checkMonsterType(monsterType: String?) {
        validatableResponse
            .body("monsterType",
                Matchers.equalTo(if (monsterType.equals("")) null else monsterType)
            )
    }

    @And("monsterAtk should be {string}")
    fun checkMonsterAtk(monsterAtk: String) {
        if (monsterAtk == "") validatableResponse.body("monsterAttack", Matchers.equalTo<Any?>(null)) else {
            validatableResponse.body("monsterAttack", Matchers.equalTo(monsterAtk.toInt()))
        }
    }

    @And("monsterDef should be {string}")
    fun checkMonsterDef(monsterDef: String) {
        if (monsterDef == "") validatableResponse.body("monsterDefense", Matchers.equalTo<Any?>(null)) else {
            validatableResponse.body("monsterDefense", Matchers.equalTo(monsterDef.toInt()))
        }
    }

    @And("level should be {string}")
    fun checkMonsterLevel(level: String) {
        val levelInt = if (level == "") null else level.toInt()
        if (levelInt != null) {
            validatableResponse.body("monsterAssociation.level", Matchers.equalTo(levelInt))
        } else {
            validatableResponse.body("monsterAssociation.level", Matchers.equalTo<Any?>(null))
        }
    }

    @And("rank should be {string}")
    fun checkMonsterRank(rank: String) {
        val rankInt = if (rank == "") null else rank.toInt()
        if (rankInt != null) {
            validatableResponse.body("monsterAssociation.rank", Matchers.equalTo(rankInt))
        } else {
            validatableResponse.body("monsterAssociation.rank", Matchers.equalTo<Any?>(null))
        }
    }

    @And("card effect should not be empty or null")
    fun checkCardEffect() {
        validatableResponse.body("cardEffect", Matchers.`is`(Matchers.notNullValue()))
        validatableResponse.body("cardEffect", Matchers.`is`(Matchers.not(Matchers.emptyString())))
    }

    @And("products card is found in should be greater than or equal to {int}")
    fun checkNumberOfProductsCardWasIn(productsFoundIn: Int) {
        validatableResponse.body("foundIn.size()", Matchers.`is`(Matchers.greaterThanOrEqualTo(productsFoundIn)))
    }

    @And("products card is found in should not be included")
    fun productInfoShouldBeAvailable() {
        validatableResponse.body("foundIn", Matchers.nullValue())
    }

    @And("ban lists card is found in should be greater than or equal to {int}")
    fun checkBanListInfoArraySize(restrictedIn: Int) {
        if (jsonPath.get<Any?>("restrictedIn") == null) checkBanListArrayExists() else validatableResponse.body(
            "restrictedIn.size()",
            Matchers.`is`(Matchers.greaterThanOrEqualTo(restrictedIn))
        )
    }

    @And("ban lists card is found in should not be included")
    fun checkBanListArrayExists() {
        validatableResponse.body("restrictedIn", Matchers.nullValue())
    }
}