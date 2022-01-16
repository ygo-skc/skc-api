package com.rtomyj.skc.cucumber

import io.cucumber.java.en.And
import io.restassured.response.ValidatableResponse
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.restassured.RestAssured
import io.restassured.path.json.JsonPath
import org.hamcrest.Matchers

class CardIntegrationTests {
    //    private final String BASE_ENDPOINT = "http://localhost:9999/api/v1/card";
    private val BASE_ENDPOINT = "https://skc-ygo-api.com/api/v1/card"

    //    private final String BASE_ENDPOINT = "https://skc-ygo-api.com/api/v1/card";
    private var jsonPath: JsonPath? = null
    private var validatableResponse: ValidatableResponse? = null
    @Given("I request all info for card with ID: {string}")
    fun i_request_all_info_for_card_with_id(cardId: String) {
        val requestEndpoint = "$BASE_ENDPOINT/$cardId?allInfo=true"
        jsonPath = RestAssured.get(requestEndpoint).jsonPath()
        validatableResponse = RestAssured.get(requestEndpoint).then()
    }

    @Given("I request info without all info flag for card with ID: {string}")
    fun i_request_some_info_for_card_with_id(cardId: String) {
        val requestEndpoint = "$BASE_ENDPOINT/$cardId"
        validatableResponse = RestAssured.get(requestEndpoint).then()
    }

    @Then("http status of response should be {int}")
    fun http_status_should_be(httpStatus: Int) {
        validatableResponse!!.statusCode(httpStatus)
    }

    @And("cardID should be {string}")
    fun check_card_id_is_correct(cardID: String) {
        validatableResponse!!.body("cardID", Matchers.equalTo(cardID))
    }

    @And("cardName should be {string}")
    fun check_card_name_is_correct(cardName: String) {
        validatableResponse!!.body("cardName", Matchers.equalTo(cardName))
    }

    @And("cardColor should be {string}")
    fun check_card_color_is_correct(cardColor: String) {
        validatableResponse!!.body("cardColor", Matchers.equalTo(cardColor))
    }

    @And("cardAttribute should be {string}")
    fun check_card_attribute_is_correct(cardAttribute: String) {
        validatableResponse!!.body("cardAttribute", Matchers.equalTo(cardAttribute))
    }

    @And("monsterType should be {string}")
    fun check_monster_type_is_correct(monsterType: String?) {
        var monsterType = monsterType
        if (monsterType == "") monsterType = null
        validatableResponse!!.body("monsterType", Matchers.equalTo(monsterType))
    }

    @And("monsterAtk should be {string}")
    fun check_monster_atk(monsterAtk: String) {
        if (monsterAtk == "") validatableResponse!!.body("monsterAttack", Matchers.equalTo<Any?>(null)) else {
            validatableResponse!!.body("monsterAttack", Matchers.equalTo(monsterAtk.toInt()))
        }
    }

    @And("monsterDef should be {string}")
    fun check_monster_def(monsterDef: String) {
        if (monsterDef == "") validatableResponse!!.body("monsterDefense", Matchers.equalTo<Any?>(null)) else {
            validatableResponse!!.body("monsterDefense", Matchers.equalTo(monsterDef.toInt()))
        }
    }

    @And("level should be {string}")
    fun check_level(level: String) {
        val levelInt = if (level == "") null else level.toInt()
        if (levelInt != null) {
            validatableResponse!!.body("monsterAssociation.level", Matchers.equalTo(levelInt))
        } else {
            validatableResponse!!.body("monsterAssociation.level", Matchers.equalTo<Any?>(null))
        }
    }

    @And("rank should be {string}")
    fun check_rank(rank: String) {
        val rankInt = if (rank == "") null else rank.toInt()
        if (rankInt != null) {
            validatableResponse!!.body("monsterAssociation.rank", Matchers.equalTo(rankInt))
        } else {
            validatableResponse!!.body("monsterAssociation.rank", Matchers.equalTo<Any?>(null))
        }
    }

    @And("card effect should not be empty or null")
    fun check_card_effect_is_not_empty() {
        validatableResponse!!.body("cardEffect", Matchers.`is`(Matchers.notNullValue()))
        validatableResponse!!.body("cardEffect", Matchers.`is`(Matchers.not(Matchers.emptyString())))
    }

    @And("products card is found in should be greater than or equal to {int}")
    fun check_products_found_in_array_has_a_minimum_of_products(productsFoundIn: Int) {
        validatableResponse!!.body("foundIn.size()", Matchers.`is`(Matchers.greaterThanOrEqualTo(productsFoundIn)))
    }

    @And("products card is found in should not be included")
    fun check_products_found_in_array_is_not_included() {
        validatableResponse!!.body("foundIn", Matchers.nullValue())
    }

    @And("ban lists card is found in should be greater than or equal to {int}")
    fun check_restricted_in_array_has_a_minimum_of(restrictedIn: Int) {
        if (jsonPath!!.get<Any?>("restrictedIn") == null) check_restricted_in_array_is_not_included() else validatableResponse!!.body(
            "restrictedIn.size()",
            Matchers.`is`(Matchers.greaterThanOrEqualTo(restrictedIn))
        )
    }

    @And("ban lists card is found in should not be included")
    fun check_restricted_in_array_is_not_included() {
        validatableResponse!!.body("restrictedIn", Matchers.nullValue())
    }
}