package com.rtomyj.skc.browse.card

import com.rtomyj.skc.browse.card.dao.CardBrowseDao
import com.rtomyj.skc.browse.card.model.CardBrowseResults
import com.rtomyj.skc.constant.TestConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CardBrowseService::class])
@Tag("Service")
class CardBrowseServiceTest {
	@MockBean(name = "jdbc")
	private lateinit var dao: CardBrowseDao

	@Autowired
	private lateinit var cardBrowseService: CardBrowseService


	@Nested
	inner class HappyPath {
		@Test
		fun `Retrieving Browse Results With No Errors`() {
			// mock setup
			val cardBrowseCriteria = CardBrowseTestUtil.cardBrowseCriteria
			val levelSet = setOf("\"level\": \"4\"", "\"level\": \"6\"", "\"level\": \"12\"")
			val rankSet = setOf("\"rank\": \"1\"", "\"rank\": \"3\"", "\"rank\": \"11\"")
			val linkSet = setOf("\"linkRating\": \"2\"", "\"linkRating\": \"5\"", "\"linkRating\": \"10\"")

			Mockito.`when`(dao.getBrowseResults(cardBrowseCriteria, levelSet, rankSet, linkSet))
				.thenReturn(CardBrowseResults(listOf(CardBrowseTestUtil.stratos), 0))

			val results = cardBrowseService.browseResults(cardBrowseCriteria)

			Assertions.assertNotNull(results)
			Assertions.assertEquals(cardBrowseCriteria, results.requestedCriteria)
			Assertions.assertNotNull(results.links)
			Assertions.assertEquals("/card/${TestConstants.STRATOS_ID}?allInfo=true", results.results[0].links.getLink("self").get().href)
			Assertions.assertEquals(TestConstants.STRATOS_TRIMMED_EFFECT, results.results[0].cardEffect)

			// verify mocks are called
			Mockito.verify(dao)
				.getBrowseResults(cardBrowseCriteria, levelSet, rankSet, linkSet)
		}
	}
}