package com.rtomyj.skc.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.constant.TestObjects
import com.rtomyj.skc.dao.BanListJDBCDao
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.util.enumeration.BanListCardStatus
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
@ContextConfiguration(classes = [BanListJDBCDao::class, DateConfig::class, ObjectMapper::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@JdbcTest
@ActiveProfiles("test") // Loading test props with H2 in memory DB configurations
@SqlGroup(Sql("classpath:sql/drop.sql"), Sql("classpath:sql/schema.sql"), Sql("classpath:sql/data.sql"), Sql("classpath:sql/views.sql"))
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
	}
}