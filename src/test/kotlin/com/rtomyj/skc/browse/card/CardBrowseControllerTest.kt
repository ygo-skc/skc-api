package com.rtomyj.skc.browse.card

import com.google.common.net.HttpHeaders
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.model.CardBrowseCriteria
import com.rtomyj.skc.model.CardBrowseResults
import org.hamcrest.Matchers
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CardBrowseController::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)    // since we cache Browse Criteria using Guava cache, we need to clean up after each test execution
@Tag("Controller")
class CardBrowseControllerTest {
	@MockBean
	private lateinit var cardBrowseService: CardBrowseService

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Nested
	@Tag("CardBrowse")
	inner class HappyPath {
		@Test
		fun `'Retrieving Browse Results With No Errors'`() {
			// mock setup
			val cardBrowseCriteria = CardBrowseCriteria(
				emptySet(),
				emptySet(),
				emptySet(),
				emptySet(),
				setOf(4),
				emptySet(),
				emptySet()
			)

			Mockito.`when`(cardBrowseService.browseResults(cardBrowseCriteria))
				.thenReturn(CardBrowseResults(listOf(CardBrowseTestUtil.stratos), 0))

			// perform call
			mockMvc.perform(
				get("/card/browse")
					.param("levels", "4")
					.header(HttpHeaders.X_FORWARDED_FOR, TestConstants.MOCK_IP)
			)
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.results.length()", Matchers.`is`(1)))
				.andExpect(jsonPath("$.results[0].cardID", Matchers.`is`("40044918")))
				.andExpect(jsonPath("$.results[0].cardName", Matchers.`is`("Elemental HERO Stratos")))
				.andExpect(jsonPath("$.results[0].cardColor", Matchers.`is`("Effect")))
				.andExpect(jsonPath("$.results[0].cardAttribute", Matchers.`is`("Wind")))
				.andExpect(jsonPath("$.results[0].monsterType", Matchers.`is`("Warrior/Effect")))
				.andExpect(jsonPath("$.results[0].monsterAssociation.level", Matchers.`is`(4)))

			// verify mocks are called with correct values
			Mockito.verify(cardBrowseService)
				.browseResults(cardBrowseCriteria)
		}


		@Test
		fun `'Retrieving Browse Criteria With No Errors'`() {
			// mock setup
			val cardBrowseCriteria = CardBrowseTestUtil.cardBrowseCriteria

			Mockito.`when`(cardBrowseService.browseCriteria())
				.thenReturn(cardBrowseCriteria)

			// perform call
			mockMvc.perform(
				get("/card/browse/criteria")
					.header(HttpHeaders.X_FORWARDED_FOR, TestConstants.MOCK_IP)
			)
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.cardColors.length()", Matchers.`is`(3)))
				.andExpect(jsonPath("$.attributes.length()", Matchers.`is`(7)))
				.andExpect(jsonPath("$.monsterTypes.length()", Matchers.`is`(5)))
				.andExpect(jsonPath("$.monsterSubTypes.length()", Matchers.`is`(6)))
				.andExpect(jsonPath("$.levels.length()", Matchers.`is`(3)))
				.andExpect(jsonPath("$.ranks.length()", Matchers.`is`(3)))
				.andExpect(jsonPath("$.linkRatings.length()", Matchers.`is`(3)))

//			 verify mocks are called with correct values
			Mockito.verify(cardBrowseService)
				.browseCriteria()
		}
	}
}