package com.rtomyj.skc.browse

import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.dao.CardBrowseDao
import com.rtomyj.skc.model.CardBrowseResults
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CardBrowseService::class])
@Tag("Service")
class CardBrowseServiceTest {
  @MockitoBean(name = "jdbc")
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

      Mockito
          .`when`(dao.getBrowseResults(cardBrowseCriteria, levelSet, rankSet, linkSet))
          .thenReturn(CardBrowseResults(listOf(CardBrowseTestUtil.stratos, CardBrowseTestUtil.crusader), 2))

      val results = cardBrowseService.browseResults(cardBrowseCriteria)

      Assertions.assertNotNull(results)
      Assertions.assertEquals(2, results.numResults)
      Assertions.assertEquals(2, results.results.size)
      Assertions.assertEquals(cardBrowseCriteria, results.requestedCriteria)

      Assertions.assertEquals(TestConstants.STRATOS_TRIMMED_EFFECT, results.results[0].cardEffect)
      Assertions.assertNotEquals(TestConstants.STRATOS_FULL_EFFECT, results.results[0].cardEffect)
      Assertions.assertNotNull(results.results[0].monsterAssociation)
      Assertions.assertNotNull(results.results[0].monsterAssociation!!.level)
      Assertions.assertEquals(4, results.results[0].monsterAssociation!!.level)

      Assertions.assertEquals(TestConstants.CRUSADER_TRIMMED_EFFECT, results.results[1].cardEffect)
      Assertions.assertNotNull(results.results[1].monsterAssociation)
      Assertions.assertNotNull(results.results[1].monsterAssociation!!.linkArrows)
      Assertions.assertEquals(2, results.results[1].monsterAssociation!!.linkRating)
      Assertions.assertNotEquals(listOf("B-L", "B-R"), results.results[1].monsterAssociation!!.linkArrows)

      // verify mocks are called
      Mockito
          .verify(dao)
          .getBrowseResults(cardBrowseCriteria, levelSet, rankSet, linkSet)
    }
  }
}