package com.rtomyj.skc.dao.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.model.card.Card
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [BanListJDBCDao::class, DateConfig::class, ObjectMapper::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DataJdbcTest
@ActiveProfiles("test") // Loading test props with H2 in memory DB configurations
@SqlGroup(Sql("classpath:sql/drop.sql"), Sql("classpath:sql/schema.sql"), Sql("classpath:sql/data.sql"), Sql("classpath:sql/views.sql"))
class BanListJDBCDaoTest {
    @Autowired
    @Qualifier("ban-list-jdbc")
    private lateinit var banListDao: BanListDao

    private val stratosTestCard: Card = Card
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

    private val aHeroLivesTestCard: Card = Card
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

    private val dMaliTestCard: Card = Card
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


    @Nested
    inner class HappyPath {
        @Test
        fun testFetchingBanListByStatus_Success() {
            val banListDate = "2015-11-09"
            val forbiddenDbResult = banListDao.getBanListByBanStatus(banListDate, Dao.Status.FORBIDDEN)
            val limitedDbResult = banListDao.getBanListByBanStatus(banListDate, Dao.Status.LIMITED)
            val semiLimitedDbResult = banListDao.getBanListByBanStatus(banListDate, Dao.Status.SEMI_LIMITED)


            Assertions.assertNotNull(forbiddenDbResult)
            Assertions.assertNotNull(limitedDbResult)
            Assertions.assertNotNull(semiLimitedDbResult)
            Assertions.assertEquals(1, forbiddenDbResult.size)
            Assertions.assertEquals(2, limitedDbResult.size)
            Assertions.assertEquals(0, semiLimitedDbResult.size)
            Assertions.assertEquals(stratosTestCard.cardName, forbiddenDbResult[0].cardName)
            Assertions.assertEquals(dMaliTestCard.cardName, limitedDbResult[0].cardName)
            Assertions.assertEquals(aHeroLivesTestCard.cardName, limitedDbResult[1].cardName)
        }
    }
}