package com.rtomyj.skc.dao.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.constant.TestObjects
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.card.Card
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [JDBCDao::class, DateConfig::class, ObjectMapper::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@JdbcTest
@ActiveProfiles("test") // Loading test props with H2 in memory DB configurations
@SqlGroup(
    Sql("classpath:sql/drop.sql"),
    Sql("classpath:sql/schema.sql"),
    Sql("classpath:sql/data.sql"),
    Sql("classpath:sql/views.sql")
)
class JdbcDaoTest {
    @Autowired
    @Qualifier("jdbc")
    private lateinit var dao: Dao

    private val stratosTestCard: Card = TestObjects.STRATOS_CARD_FULL_TEXT
    private val aHeroLivesTestCard: Card = TestObjects.A_HERO_LIVES_CARD_FULL_TEXT
    private val dMaliTestCard: Card = TestObjects.D_MALI_CARD_FULL_TEXT


    @Nested
    inner class HappyPath {
        @Test
        fun testFetchingCardById_Success() {
            val stratosDbResult = dao.getCardInfo(stratosTestCard.cardID)
            val aHeroLivesDbResult = dao.getCardInfo(aHeroLivesTestCard.cardID)
            val dMaliDbResult = dao.getCardInfo(dMaliTestCard.cardID)


            Assertions.assertNotEquals(null, stratosDbResult)
            Assertions.assertEquals(stratosTestCard.cardID, stratosDbResult.cardID)
            Assertions.assertEquals(stratosTestCard.cardName, stratosDbResult.cardName)
            Assertions.assertEquals(stratosTestCard.monsterType, stratosDbResult.monsterType)
            Assertions.assertEquals(stratosTestCard.cardColor, stratosDbResult.cardColor)
            Assertions.assertEquals(stratosTestCard.cardAttribute, stratosDbResult.cardAttribute)
            Assertions.assertEquals(stratosTestCard.monsterAttack, stratosDbResult.monsterAttack)
            Assertions.assertEquals(stratosTestCard.monsterDefense, stratosDbResult.monsterDefense)
            Assertions.assertEquals(stratosTestCard.cardEffect, stratosDbResult.cardEffect)

            Assertions.assertNotEquals(null, aHeroLivesDbResult)
            Assertions.assertEquals(aHeroLivesTestCard.cardID, aHeroLivesDbResult.cardID)
            Assertions.assertEquals(aHeroLivesTestCard.cardName, aHeroLivesDbResult.cardName)
            Assertions.assertNull(aHeroLivesDbResult.monsterType)
            Assertions.assertEquals(aHeroLivesTestCard.cardColor, aHeroLivesDbResult.cardColor)
            Assertions.assertEquals(aHeroLivesTestCard.cardAttribute, aHeroLivesDbResult.cardAttribute)
            Assertions.assertNull(aHeroLivesDbResult.monsterAttack)
            Assertions.assertNull(aHeroLivesDbResult.monsterDefense)
            Assertions.assertEquals(aHeroLivesTestCard.cardEffect, aHeroLivesDbResult.cardEffect)

            Assertions.assertNotEquals(null, dMaliDbResult)
            Assertions.assertEquals(dMaliTestCard.cardID, dMaliDbResult.cardID)
            Assertions.assertEquals(dMaliTestCard.cardName, dMaliDbResult.cardName)
            Assertions.assertEquals(dMaliTestCard.monsterType, dMaliDbResult.monsterType)
            Assertions.assertEquals(dMaliTestCard.cardColor, dMaliDbResult.cardColor)
            Assertions.assertEquals(dMaliTestCard.cardAttribute, dMaliDbResult.cardAttribute)
            Assertions.assertEquals(dMaliTestCard.monsterAttack, dMaliDbResult.monsterAttack)
            Assertions.assertEquals(dMaliTestCard.monsterDefense, dMaliDbResult.monsterDefense)
            Assertions.assertEquals(dMaliTestCard.cardEffect, dMaliDbResult.cardEffect)
        }
    }


    @Nested
    inner class UnhappyPath {
        @Test
        fun testFetchingCardById_Failure() {
            Assertions.assertThrows(YgoException::class.java) { dao.getCardInfo("12345678") }
        }
    }
}