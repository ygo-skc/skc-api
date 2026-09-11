package com.rtomyj.skc.service

import com.rtomyj.skc.dao.CardBrowseDao
import com.rtomyj.skc.model.CardBrowseResults
import com.rtomyj.skc.util.CardBrowseTestUtil
import com.rtomyj.skc.util.constant.TestConstants
import com.rtomyj.skc.util.enumeration.MonsterAssociationType
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

        /**
         * Each criterion is fetched by a separate DAO call - distinct sizes per criteria so a mis-wired argument is caught.
         */
        @Test
        fun `Retrieving Browse Criteria Maps Every DAO Call To The Correct Field`() {
            val cardColors = setOf("Effect")
            val attributes = setOf("Dark", "Light")
            val monsterTypes = setOf("Aqua", "Beast", "Warrior")
            val monsterSubTypes = setOf("Flip", "Toon", "Tuner", "Union")
            val levels = setOf(4)
            val ranks = setOf(7, 8)
            val linkRatings = setOf(1, 2, 3)

            Mockito.`when`(dao.getCardColors()).thenReturn(cardColors)
            Mockito.`when`(dao.getMonsterAttributes()).thenReturn(attributes)
            Mockito.`when`(dao.getMonsterTypes()).thenReturn(monsterTypes)
            Mockito.`when`(dao.getMonsterSubTypes()).thenReturn(monsterSubTypes)
            Mockito.`when`(dao.getMonsterAssociationField(MonsterAssociationType.LEVEL)).thenReturn(levels)
            Mockito.`when`(dao.getMonsterAssociationField(MonsterAssociationType.RANK)).thenReturn(ranks)
            Mockito.`when`(dao.getMonsterAssociationField(MonsterAssociationType.LINK)).thenReturn(linkRatings)

            val criteria = cardBrowseService.browseCriteria()

            Assertions.assertEquals(cardColors, criteria.cardColors)
            Assertions.assertEquals(attributes, criteria.attributes)
            Assertions.assertEquals(monsterTypes, criteria.monsterTypes)
            Assertions.assertEquals(monsterSubTypes, criteria.monsterSubTypes)
            Assertions.assertEquals(levels, criteria.levels)
            Assertions.assertEquals(ranks, criteria.ranks)
            Assertions.assertEquals(linkRatings, criteria.linkRatings)

            // every criterion must be sourced exactly once
            Mockito.verify(dao).getCardColors()
            Mockito.verify(dao).getMonsterAttributes()
            Mockito.verify(dao).getMonsterTypes()
            Mockito.verify(dao).getMonsterSubTypes()
            Mockito.verify(dao).getMonsterAssociationField(MonsterAssociationType.LEVEL)
            Mockito.verify(dao).getMonsterAssociationField(MonsterAssociationType.RANK)
            Mockito.verify(dao).getMonsterAssociationField(MonsterAssociationType.LINK)
        }
    }
}
