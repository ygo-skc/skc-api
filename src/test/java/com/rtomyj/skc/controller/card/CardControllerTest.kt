package com.rtomyj.skc.controller.card

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.service.card.CardService
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CardController::class)
@Tag("Controller")
class CardControllerTest {
	@MockBean
	private lateinit var cardService: CardService

	@Autowired
	private lateinit var mockMvc: MockMvc


	@Nested
	inner class HappyPath {
		@Test
		fun `Fetching Card Information Using Card ID - Success`() {
			// setup mocks - throw NOT FOUND (card not in DB) exception when particular card is requested
			val mapper = ObjectMapper()
			val mockCardData: Card = mapper
				.readValue(ClassPathResource(TestConstants.CARD_INSTANCE_JSON_FILE).file, Card::class.java)

			`when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true))
				.thenReturn(mockCardData)


			// call controller and verify correct status, code and message are returned
			mockMvc
				.perform(
					get("/card/${TestConstants.STRATOS_ID}")
						.param("allInfo", "true")
				)
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.cardID", `is`(TestConstants.STRATOS_ID)))
				.andExpect(jsonPath("$.cardName", `is`(TestConstants.STRATOS_NAME)))
				.andExpect(jsonPath("$.cardColor", `is`(TestConstants.STRATOS_COLOR)))
				.andExpect(jsonPath("$.cardAttribute", `is`(TestConstants.STRATOS_ATTRIBUTE)))


			// verify mocks are called
			verify(cardService)
				.getCardInfo(TestConstants.STRATOS_ID, true)
		}
	}


	@Nested
	inner class UnhappyPath {
		@Test
		fun `Fetching Card Information Using Card ID - Card ID Is Not Formatted Correctly - HTTP 400 Error`() {
			// call controller with incorrectly formatted card ID
			mockMvc
				.perform(
					get("/card/123")
						.param("allInfo", "true")
				)
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("$.code", `is`(ErrorType.G001.name)))
				.andExpect(jsonPath("$.message", `is`(ErrorType.G001.toString())))
		}


		@Test
		fun `Fetching Card Information Using Card ID - Card Requested Is Not In DB - HTTP 404 Error`() {
			// setup mocks - throw NOT FOUND (card not in DB) exception when particular card is requested
			`when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true))
				.thenThrow(YgoException(String.format(ErrConstants.CARD_ID_REQUESTED_NOT_FOUND_IN_DB, TestConstants.STRATOS_ID)
					, ErrorType.D001))


			// call controller and verify correct status, code and message are returned
			mockMvc
				.perform(
					get("/card/${TestConstants.STRATOS_ID}")
						.param("allInfo", "true")
				)
				.andExpect(status().isNotFound)
				.andExpect(
					jsonPath("$.message", `is`(ErrorType.D001.toString()))
				)
				.andExpect(
					jsonPath("$.code", `is`(ErrorType.D001.name))
				)


			// verify mocks are called
			verify(cardService)
				.getCardInfo(TestConstants.STRATOS_ID, true)
		}


		@Test
		fun `Fetching Card Information Using Card ID - Required Database Tables Are Missing - HTTP 500 Error`() {
			// setup mocks - throw DB table missing exception when particular card is requested
			`when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true))
				.thenThrow(YgoException(ErrConstants.DB_MISSING_TABLE, ErrorType.D002))

			// call controller and verify correct status, code and message are returned
			mockMvc
				.perform(
					get("/card/${TestConstants.STRATOS_ID}")
						.param("allInfo", "true")
				)
				.andExpect(status().isInternalServerError)
				.andExpect(
					jsonPath("$.message", `is`(ErrorType.D002.toString()))
				)
				.andExpect(
					jsonPath("$.code", `is`(ErrorType.D002.name))
				)


			// verify mocks are called
			verify(cardService)
				.getCardInfo(TestConstants.STRATOS_ID, true)
		}
	}
}