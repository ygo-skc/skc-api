package com.rtomyj.skc.find.card

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.net.HttpHeaders.X_FORWARDED_FOR
import com.rtomyj.skc.browse.card.model.Card
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.testingutil.ControllerTestUtil
import com.rtomyj.skc.util.constant.ErrConstants
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
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
			val mapper = jacksonObjectMapper()
			val mockCardData: Card = mapper
				.readValue(ClassPathResource(TestConstants.CARD_INSTANCE_STRATOS).file, Card::class.java)

			`when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP))
				.thenReturn(mockCardData)


			// call controller and verify correct status, code and message are returned
			mockMvc
				.perform(
					get("/card/${TestConstants.STRATOS_ID}")
						.param("allInfo", "true")
						.header(X_FORWARDED_FOR, TestConstants.MOCK_IP)
				)
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.cardID", `is`(TestConstants.STRATOS_ID)))
				.andExpect(jsonPath("$.cardName", `is`(TestConstants.STRATOS_NAME)))
				.andExpect(jsonPath("$.cardColor", `is`(TestConstants.STRATOS_COLOR)))
				.andExpect(jsonPath("$.cardAttribute", `is`(TestConstants.STRATOS_ATTRIBUTE)))


			// verify mocks are called
			verify(cardService)
				.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)
		}
	}


	@Nested
	inner class UnhappyPath {
		@Test
		fun `Fetching Card Information Using Card ID - Card ID Is Not Formatted Correctly - HTTP 400 Error`() {
			// call controller with incorrectly formatted card ID
			ControllerTestUtil.validateBadRequestHelper(
				mockMvc
					.perform(
						get("/card/123")
							.param("allInfo", "true")
					)
			)
		}


		@Test
		fun `Fetching Card Information Using Card ID - Card Requested Is Not In DB - HTTP 404 Error`() {
			// setup mocks - throw NOT FOUND (card not in DB) exception when particular card is requested
			`when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP))
				.thenThrow(
					SKCException(
						String.format(ErrConstants.CARD_ID_REQUESTED_NOT_FOUND_IN_DB, TestConstants.STRATOS_ID), ErrorType.DB001
					)
				)


			// call controller and verify correct status, code and message are returned
			ControllerTestUtil.validateNotFoundHelper(
				mockMvc
					.perform(
						get("/card/${TestConstants.STRATOS_ID}")
							.param("allInfo", "true")
							.header(X_FORWARDED_FOR, TestConstants.MOCK_IP) // request filter will take the ip from this header and store it in a new header
					)
			)


			// verify mocks are called
			verify(cardService)
				.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)
		}


		@Test
		fun `Fetching Card Information Using Card ID - Required Database Tables Are Missing - HTTP 500 Error`() {
			// setup mocks - throw DB table missing exception when particular card is requested
			`when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP))
				.thenThrow(SKCException(ErrConstants.DB_MISSING_TABLE, ErrorType.DB002))


			// call controller and verify correct status, code and message are returned
			ControllerTestUtil.validateSTableNotCreatedHelper(
				mockMvc
					.perform(
						get("/card/${TestConstants.STRATOS_ID}")
							.param("allInfo", "true")
							.header(X_FORWARDED_FOR, TestConstants.MOCK_IP)
					)
			)


			// verify mocks are called
			verify(cardService)
				.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)
		}
	}
}