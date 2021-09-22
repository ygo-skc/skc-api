package com.rtomyj.skc.service.banlist

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListInstance
import com.rtomyj.skc.model.card.Card
import org.cache2k.io.CacheLoaderException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [BannedCardsService::class, DiffService::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
// TODO: add ban list assertion that checks if forbidden, limited, etc length matches the length/num field for each associated list
class BannedCardsServiceTest {
    @MockBean(name = "jdbc")
    private lateinit var dao: Dao

    @Autowired
    private lateinit var diffService: DiffService

    @Autowired
    private lateinit var bannedCardsService: BannedCardsService


    companion object {
        private lateinit var banListInstanceFullText: BanListInstance

        @BeforeAll
        @JvmStatic
        fun before() {
            val mapper = ObjectMapper()

            banListInstanceFullText = mapper.readValue(
                ClassPathResource(TestConstants.BAN_LIST_INSTANCE_JSON_FILE).file, BanListInstance::class.java
            )
        }
    }


    /**
     * Happy path - service object successfully gets data using Dao helper object.
     * This test only focuses on scenario where client requires the full card effect for each card
     *  (as opposed to trimmed effect which as an option the client can use)
     */
    @Test
    fun testFetchingBanListInstance_FromDB_WithFullText_Successfully() {
        // create mocks
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
            )
        )
            .thenReturn(banListInstanceFullText.forbidden)
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.LIMITED)
            )
        )
            .thenReturn(banListInstanceFullText.limited)
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
            )
        )
            .thenReturn(banListInstanceFullText.semiLimited)


        // call code being tested
        val banListInstance = bannedCardsService
            .getBanListByDate(TestConstants.BAN_LIST_START_DATE, false, false)

        val forbidden = banListInstance.forbidden
        val limited = banListInstance.limited
        val semiLimited = banListInstance.semiLimited


        // assertions on return values from code being tested
        Assertions.assertNotNull(banListInstance)
        Assertions.assertNotNull(forbidden)
        Assertions.assertNotNull(limited)
        Assertions.assertNotNull(semiLimited)

        Assertions.assertEquals(1, forbidden.size)
        Assertions.assertEquals(1, limited.size)
        Assertions.assertEquals(1, semiLimited.size)

        Assertions.assertEquals(1, banListInstance.numForbidden)
        Assertions.assertEquals(1, banListInstance.numLimited)
        Assertions.assertEquals(1, banListInstance.numSemiLimited)

        Assertions.assertEquals(TestConstants.STRATOS_ID, forbidden[0].cardID)
        Assertions.assertEquals(TestConstants.STRATOS_NAME, forbidden[0].cardName)
        Assertions.assertEquals(TestConstants.STRATOS_FULL_EFFECT, forbidden[0].cardEffect)

        Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, limited[0].cardID)
        Assertions.assertEquals(TestConstants.A_HERO_LIVES_NAME, limited[0].cardName)
        Assertions.assertEquals(TestConstants.A_HERO_LIVES_FULL_EFFECT, limited[0].cardEffect)

        Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, semiLimited[0].cardID)
        Assertions.assertEquals(TestConstants.D_MALICIOUS_NAME, semiLimited[0].cardName)
        Assertions.assertEquals(TestConstants.D_MALICIOUS_FULL_EFFECT, semiLimited[0].cardEffect)


        // verify mocks are called the exact number of times expected
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
        )
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.LIMITED)
        )
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
        )
    }


    /**
     * Happy path - service object successfully gets data using Dao helper object.
     * This test only focuses on scenario where client requires the trimmed card effect for each card
     *  (as opposed to full card effect which as an option the client can use, trimmed card effect can be used to save bandwidth)
     */
    @Test
    fun testFetchingBanListInstance_FromDB_WithTrimmedText_Successfully() {
        // mock calls
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
            )
        )
            .thenReturn(banListInstanceFullText.forbidden)
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.LIMITED)
            )
        )
            .thenReturn(banListInstanceFullText.limited)
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
            )
        )
            .thenReturn(banListInstanceFullText.semiLimited)


        // call code being tested, assign certain values to vars for easier usage
        val banListInstance = bannedCardsService.getBanListByDate(TestConstants.BAN_LIST_START_DATE, true, false)

        val forbiddenTrimmed = banListInstance.forbidden
        val limitedTrimmed = banListInstance.limited
        val semiLimitedTrimmed = banListInstance.semiLimited


        // assertions on values returned by code being tested
        Assertions.assertNotNull(banListInstance)
        Assertions.assertNotNull(forbiddenTrimmed)
        Assertions.assertNotNull(limitedTrimmed)
        Assertions.assertNotNull(semiLimitedTrimmed)

        Assertions.assertEquals(1, forbiddenTrimmed.size)
        Assertions.assertEquals(1, limitedTrimmed.size)
        Assertions.assertEquals(1, semiLimitedTrimmed.size)

        Assertions.assertEquals(1, banListInstance.numForbidden)
        Assertions.assertEquals(1, banListInstance.numLimited)
        Assertions.assertEquals(1, banListInstance.numSemiLimited)

        Assertions.assertEquals(TestConstants.STRATOS_ID, forbiddenTrimmed[0].cardID)
        Assertions.assertEquals(TestConstants.STRATOS_NAME, forbiddenTrimmed[0].cardName)
        Assertions.assertEquals(Card.trimEffect(TestConstants.STRATOS_FULL_EFFECT), forbiddenTrimmed[0].cardEffect)

        Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, limitedTrimmed[0].cardID)
        Assertions.assertEquals(TestConstants.A_HERO_LIVES_NAME, limitedTrimmed[0].cardName)
        Assertions.assertEquals(Card.trimEffect(TestConstants.A_HERO_LIVES_FULL_EFFECT), limitedTrimmed[0].cardEffect)

        Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, semiLimitedTrimmed[0].cardID)
        Assertions.assertEquals(TestConstants.D_MALICIOUS_NAME, semiLimitedTrimmed[0].cardName)
        Assertions.assertEquals(Card.trimEffect(TestConstants.D_MALICIOUS_FULL_EFFECT), semiLimitedTrimmed[0].cardEffect)


        // verify mocks are called the exact number of times expected
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
        )
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.LIMITED)
        )
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
        )
    }


    /**
     * Unhappy path - using Dao helper object resulted in an error. Ban list not found in database .
     */
    @Test
    fun testFetchingBanListInstance_FromDB_WithFullText_BanListNotInDB() {
        dbError_BanListNotInDB(false)
    }


    /**
     * Unhappy path - using Dao helper object resulted in an error. Ban list not found in database .
     */
    @Test
    fun testFetchingBanListInstance_FromDB_WithTrimmedText_BanListNotInDB() {
        dbError_BanListNotInDB(true)
    }


    /**
     * Utility method that will set up mocks, call getBanListByBanStatus(), and verify mock calls for unhappy path - Ban list not found in database.
     */
    private fun dbError_BanListNotInDB(isSaveBandwidth: Boolean) {
        // mock calls
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
            )
        )
            .thenReturn(ArrayList())
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.LIMITED)
            )
        )
            .thenReturn(ArrayList())
        Mockito.`when`(
            dao.getBanListByBanStatus(
                ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
            )
        )
            .thenReturn(ArrayList())


        // call code and assert throws
        val ex = Assertions.assertThrows(CacheLoaderException::class.java) {
            bannedCardsService.getBanListByDate(
                TestConstants.BAN_LIST_START_DATE,
                isSaveBandwidth,
                false
            )
        }

        Assertions.assertTrue(ex.cause is YgoException)
        val exCause = ex.cause as YgoException  // previous assertion passed, we know the type of ex
        Assertions.assertEquals(ErrConstants.NOT_FOUND_DAO_ERR, exCause.code)
        Assertions.assertEquals(String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, TestConstants.BAN_LIST_START_DATE), exCause.message)


        // verify mocks are called the exact number of times expected
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
        )
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.LIMITED)
        )
        Mockito.verify(dao, Mockito.times(1)).getBanListByBanStatus(
            ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
            ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
        )
    }
}