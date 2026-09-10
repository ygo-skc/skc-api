package com.rtomyj.skc.dao

import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.util.constant.TestObjects
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit.jupiter.SpringExtension
import tools.jackson.databind.json.JsonMapper

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [BanListJDBCDao::class, DateConfig::class, JsonMapper::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@JdbcTest
@ActiveProfiles("test") // Loading test props with H2 in memory DB configurations
@SqlGroup(
    Sql("classpath:sql/drop.sql"),
    Sql("classpath:sql/schema.sql"),
    Sql("classpath:sql/data.sql"),
    Sql("classpath:sql/views.sql"),
)
class BanListJDBCDaoTest {
    @Autowired
    @Qualifier("ban-list-jdbc")
    private lateinit var banListDao: BanListDao

    private val stratosTestCard: Card = TestObjects.STRATOS_CARD_FULL_TEXT
    private val aHeroLivesTestCard = TestObjects.A_HERO_LIVES_CARD_FULL_TEXT
    private val dMaliTestCard: Card = TestObjects.D_MALI_CARD_FULL_TEXT

    @Nested
    inner class HappyPath {
        @Test
        fun testFetchingBanListByStatus_Success() {
            val banListDate = "2015-11-09"
            val forbiddenDbResult = banListDao.getBanListByBanStatus(banListDate, BanListCardStatus.FORBIDDEN, "TCG")
            val limitedDbResult = banListDao.getBanListByBanStatus(banListDate, BanListCardStatus.LIMITED, "TCG")
            val semiLimitedDbResult = banListDao.getBanListByBanStatus(banListDate, BanListCardStatus.SEMI_LIMITED, "TCG")

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

        @Test
        fun testFetchingPreviousBanListDate_Success() {
            // the DL list on 2015-09-01 falls between the two TCG lists and must not be picked
            Assertions.assertEquals("2015-07-06", banListDao.getPreviousBanListDate("2015-11-09", "TCG"))
        }

        @Test
        fun testFetchingPreviousBanListDateForOldestBanList_ReturnsEmpty() {
            Assertions.assertEquals("", banListDao.getPreviousBanListDate("2015-07-06", "TCG"))
        }

        @Test
        fun testValidatingBanList_Success() {
            Assertions.assertTrue(banListDao.isBanListValid("2015-11-09", "TCG"))
            Assertions.assertTrue(banListDao.isBanListValid("2015-07-06", "TCG"))
            Assertions.assertTrue(banListDao.isBanListValid("2015-09-01", "DL"))
        }
    }

    @Nested
    inner class UnhappyPath {
        @Test
        fun testFetchingPreviousBanListDateForUnknownDate_DoesNotThrow() {
            Assertions.assertEquals("2015-11-09", banListDao.getPreviousBanListDate("2030-01-01", "TCG"))
            Assertions.assertEquals("", banListDao.getPreviousBanListDate("1999-01-01", "TCG"))
        }

        @Test
        fun testValidatingBanListForUnknownDate_ReturnsFalse() {
            Assertions.assertFalse(banListDao.isBanListValid("2030-01-01", "TCG"))
        }

        @Test
        fun testValidatingBanListForWrongFormat_ReturnsFalse() {
            // 2015-11-09 only exists for TCG and 2015-09-01 only for DL
            Assertions.assertFalse(banListDao.isBanListValid("2015-11-09", "DL"))
            Assertions.assertFalse(banListDao.isBanListValid("2015-09-01", "TCG"))
        }
    }
}
