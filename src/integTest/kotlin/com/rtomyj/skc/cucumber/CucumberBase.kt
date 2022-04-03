package com.rtomyj.skc.cucumber

import io.restassured.path.json.JsonPath
import io.restassured.response.ValidatableResponse

open class CucumberBase {
	companion object {
		const val BASE_ENDPOINT = "http://localhost:9999/api/v1";
//		const val BASE_ENDPOINT = "https://skc-ygo-api.com/api/v1"
//        private final String BASE_ENDPOINT = "https://skc-ygo-api.com/api/v1";
	}


	lateinit var jsonPath: JsonPath
	lateinit var validatableResponse: ValidatableResponse
}