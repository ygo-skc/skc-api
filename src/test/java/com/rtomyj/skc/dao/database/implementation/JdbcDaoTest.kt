package com.rtomyj.skc.dao.database.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.dao.database.Dao
import com.rtomyj.skc.helper.constants.TestConstants
import com.rtomyj.skc.helper.exceptions.YgoException
import com.rtomyj.skc.model.card.Card
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [JDBCDao::class, DateConfig::class, ObjectMapper::class])
@DataJdbcTest
@ActiveProfiles("test") // Loading test props with H2 in memory DB configurations
@SqlGroup(Sql("classpath:sql/drop.sql"), Sql("classpath:sql/schema.sql"), Sql("classpath:sql/data.sql"), Sql("classpath:sql/views.sql"))
class JdbcDaoTest {
    @Autowired
    private lateinit var jdbcNamedTemplate: NamedParameterJdbcTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    @Qualifier("jdbc")
    private lateinit var dao: Dao


    companion object {
        private lateinit var stratosTestCard: Card
        private lateinit var aHeroLivesTestCard: Card
        private lateinit var dMaliTestCard: Card
        
        
        @BeforeAll
        @JvmStatic
        fun before() {
            stratosTestCard = Card
                .builder()
                .cardID(TestConstants.STRATOS_ID)
                .cardName(TestConstants.STRATOS_NAME)
                .monsterType(TestConstants.STRATOS_TYPE)
                .cardColor(TestConstants.STRATOS_COLOR)
                .cardAttribute(TestConstants.STRATOS_ATTRIBUTE)
                .monsterAttack(TestConstants.STRATOS_ATK)
                .monsterDefense(TestConstants.STRATOS_DEF)
                .cardEffect(TestConstants.STRATOS_FULL_EFFECT)
                .build()
            aHeroLivesTestCard = Card
                .builder()
                .cardID(TestConstants.A_HERO_LIVES_ID)
                .cardName(TestConstants.A_HERO_LIVES_NAME)
                .monsterType(null)
                .cardColor(TestConstants.A_HERO_LIVES_COLOR)
                .cardAttribute(TestConstants.A_HERO_LIVES_ATTRIBUTE)
                .monsterAttack(null)
                .monsterDefense(null)
                .cardEffect(TestConstants.A_HERO_LIVES_FULL_EFFECT)
                .build()
            dMaliTestCard = Card
                .builder()
                .cardID(TestConstants.D_MALICIOUS_ID)
                .cardName(TestConstants.D_MALICIOUS_NAME)
                .monsterType(TestConstants.D_MALICIOUS_TYPE)
                .cardColor(TestConstants.D_MALICIOUS_COLOR)
                .cardAttribute(TestConstants.D_MALICIOUS_ATTRIBUTE)
                .monsterAttack(TestConstants.D_MALICIOUS_ATK)
                .monsterDefense(TestConstants.D_MALICIOUS_DEF)
                .cardEffect(TestConstants.D_MALICIOUS_FULL_EFFECT)
                .build()
        }
    }


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


    @Test
    fun testFetchingCardById_Failure() {
        Assertions.assertThrows(NullPointerException::class.java) { dao.getCardInfo(null) }
        Assertions.assertThrows(YgoException::class.java) { dao.getCardInfo("12345678") }
    }


    @Test
    fun testFetchingBanListByStatus_Success() {
        val banListDate = "2015-11-09"
        val forbiddenDbResult = dao.getBanListByBanStatus(banListDate, Dao.Status.FORBIDDEN)
        val limitedDbResult = dao.getBanListByBanStatus(banListDate, Dao.Status.LIMITED)
        val semiLimitedDbResult = dao.getBanListByBanStatus(banListDate, Dao.Status.SEMI_LIMITED)


        Assertions.assertNotEquals(null, forbiddenDbResult)
        Assertions.assertNotEquals(null, limitedDbResult)
        Assertions.assertNotEquals(null, semiLimitedDbResult)
        Assertions.assertEquals(1, forbiddenDbResult.size)
        Assertions.assertEquals(2, limitedDbResult.size)
        Assertions.assertEquals(0, semiLimitedDbResult.size)
        Assertions.assertEquals(stratosTestCard.cardName, forbiddenDbResult[0].cardName)
        Assertions.assertEquals(dMaliTestCard.cardName, limitedDbResult[0].cardName)
        Assertions.assertEquals(aHeroLivesTestCard.cardName, limitedDbResult[1].cardName)
    }
}