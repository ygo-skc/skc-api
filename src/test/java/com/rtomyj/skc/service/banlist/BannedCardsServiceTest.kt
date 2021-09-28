package com.rtomyj.skc.service.banlist

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.enums.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListInstance
import com.rtomyj.skc.model.banlist.BanListNewContent
import com.rtomyj.skc.model.banlist.BanListRemovedContent
import com.rtomyj.skc.model.card.Card
import org.cache2k.io.CacheLoaderException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [BannedCardsService::class, DiffService::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)    // allows usage of init as opposed to static context
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
class BannedCardsServiceTest {
    @MockBean(name = "jdbc")
    private lateinit var dao: Dao

    @MockBean
    private lateinit var diffService: DiffService

    @Autowired
    private lateinit var bannedCardsService: BannedCardsService

    private val banListInstanceFullText: BanListInstance
    private val banListNewContent: BanListNewContent
    private val banListRemovedContent: BanListRemovedContent


    init {
        val mapper = ObjectMapper()

        banListInstanceFullText = mapper.readValue(
            ClassPathResource(TestConstants.BAN_LIST_INSTANCE_JSON_FILE).file, BanListInstance::class.java
        )

        banListNewContent = mapper
            .readValue(ClassPathResource(TestConstants.BAN_LIST_NEW_CONTENT).file, BanListNewContent::class.java)

        banListRemovedContent = mapper
            .readValue(ClassPathResource(TestConstants.BAN_LIST_REMOVED_CONTENT).file, BanListRemovedContent::class.java)
    }


    @Nested
    inner class HappyPath {
        /**
         * Happy path - service object successfully gets data using Dao helper object.
         * This test only focuses on scenario where client requires the full card effect for each card
         *  (as opposed to trimmed effect which as an option the client can use)
         *  , and client doesn't want removed and new cards for the Ban List.
         */
        @Test
        fun `Test Fetching Ban List Instance, From DB, With Save Bandwidth As False, And Fetch All Info As False, Successfully`() {
            happyPath(saveBandwidth = false, fetchAllInfo = false)
        }


        /**
         * Happy path - service object successfully gets data using Dao helper object.
         * This test only focuses on scenario where client requires the trimmed card effect for each card
         *  (as opposed to full card effect which as an option the client can use, trimmed card effect can be used to save bandwidth)
         *  , and client doesn't want removed and new cards for the Ban List.
         */
        @Test
        fun `Test Fetching Ban List Instance, From DB, With Save Bandwidth As True, And Fetch All Info As False, Successfully`() {
            happyPath(saveBandwidth = true, fetchAllInfo = false)
        }


        /**
         * Happy path - service object successfully gets data using Dao helper object.
         * This test only focuses on scenario where client requires the full card effect for each card
         *  (as opposed to trimmed effect which as an option the client can use)
         *  , and client requested removed and new cards for the Ban List.
         */
        @Test
        fun `Test Fetching Ban List Instance, From DB, With Save Bandwidth As False, And Fetch All Info As True, Successfully`() {
            happyPath(saveBandwidth = false, fetchAllInfo = true)
        }


        /**
         * Happy path - service object successfully gets data using Dao helper object.
         * This test only focuses on scenario where client requires the trimmed card effect for each card
         *  (as opposed to full card effect which as an option the client can use, trimmed card effect can be used to save bandwidth)
         *  , and client requested removed and new cards for the Ban List.
         */
        @Test
        fun `Test Fetching Ban List Instance, From DB, With Save Bandwidth As True, And Fetch All Info As True, Successfully`() {
            happyPath(saveBandwidth = true, fetchAllInfo = true)
        }


        private fun happyPath(saveBandwidth: Boolean, fetchAllInfo: Boolean) {
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

            if (fetchAllInfo) {
                Mockito.`when`(
                    diffService.getNewContentForGivenBanList(
                        ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)
                    )
                )
                    .thenReturn(banListNewContent)

                Mockito.`when`(
                    diffService.getRemovedContentForGivenBanList(
                        ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)
                    )
                )
                    .thenReturn(banListRemovedContent)
            }


            // call code being tested
            val banListInstance = bannedCardsService
                .getBanListByDate(TestConstants.BAN_LIST_START_DATE, saveBandwidth, fetchAllInfo)

            val forbidden = banListInstance.forbidden
            val limited = banListInstance.limited
            val semiLimited = banListInstance.semiLimited


            // ensure objects are not null as expected
            Assertions.assertNotNull(banListInstance)
            Assertions.assertNotNull(forbidden)
            Assertions.assertNotNull(limited)
            Assertions.assertNotNull(semiLimited)

            // ensure dates returned are correct
            Assertions.assertEquals(TestConstants.BAN_LIST_START_DATE, banListInstance.effectiveDate)

            // ensure size of array containing cards for forbidden, limited and semi-limited is correct
            Assertions.assertEquals(1, forbidden.size)
            Assertions.assertEquals(1, limited.size)
            Assertions.assertEquals(1, semiLimited.size)

            // ensure field denoting how many cards are in forbidden, limited and semi-limited list is correct, this field is used by consumers
            Assertions.assertEquals(1, banListInstance.numForbidden)
            Assertions.assertEquals(1, banListInstance.numLimited)
            Assertions.assertEquals(1, banListInstance.numSemiLimited)

            // ensure card info is as expected
            Assertions.assertEquals(TestConstants.STRATOS_NAME, forbidden[0].cardName)
            Assertions.assertEquals(TestConstants.STRATOS_TYPE, forbidden[0].monsterType)
            Assertions.assertEquals(TestConstants.STRATOS_COLOR, forbidden[0].cardColor)
            Assertions.assertEquals(TestConstants.STRATOS_ID, forbidden[0].cardID)
            Assertions.assertEquals(TestConstants.STRATOS_ATTRIBUTE, forbidden[0].cardAttribute)
            Assertions.assertEquals(TestConstants.STRATOS_ATK, forbidden[0].monsterAttack)
            Assertions.assertEquals(TestConstants.STRATOS_DEF, forbidden[0].monsterDefense)

            Assertions.assertEquals(TestConstants.A_HERO_LIVES_NAME, limited[0].cardName)
            Assertions.assertNull(limited[0].monsterType)
            Assertions.assertEquals(TestConstants.A_HERO_LIVES_COLOR, limited[0].cardColor)
            Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, limited[0].cardID)
            Assertions.assertEquals(TestConstants.A_HERO_LIVES_ATTRIBUTE, limited[0].cardAttribute)
            Assertions.assertNull(limited[0].monsterAttack)
            Assertions.assertNull(limited[0].monsterDefense)

            Assertions.assertEquals(TestConstants.D_MALICIOUS_NAME, semiLimited[0].cardName)
            Assertions.assertEquals(TestConstants.D_MALICIOUS_TYPE, semiLimited[0].monsterType)
            Assertions.assertEquals(TestConstants.D_MALICIOUS_COLOR, semiLimited[0].cardColor)
            Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, semiLimited[0].cardID)
            Assertions.assertEquals(TestConstants.D_MALICIOUS_ATTRIBUTE, semiLimited[0].cardAttribute)
            Assertions.assertEquals(TestConstants.D_MALICIOUS_ATK, semiLimited[0].monsterAttack)
            Assertions.assertEquals(TestConstants.D_MALICIOUS_DEF, semiLimited[0].monsterDefense)

            // ensure HATEOAS links are sent
            Assertions.assertNotNull(banListInstance.links.getLink("self"))
            Assertions.assertNotNull(banListInstance.links.getLink("Ban List New Content"))
            Assertions.assertNotNull(banListInstance.links.getLink("Ban List Removed Content"))

            Assertions.assertNotNull(forbidden[0].links.getLink("self"))
            Assertions.assertNotNull(limited[0].links.getLink("self"))
            Assertions.assertNotNull(semiLimited[0].links.getLink("self"))

            // ensure href is as expected
            Assertions.assertEquals("/ban_list/${TestConstants.BAN_LIST_START_DATE}/cards?saveBandwidth=false&allInfo=true"
                , banListInstance.links.getLink("self").get().href)
            Assertions.assertEquals("/ban_list/${TestConstants.BAN_LIST_START_DATE}/new"
                , banListInstance.links.getLink("Ban List New Content").get().href)
            Assertions.assertEquals("/ban_list/${TestConstants.BAN_LIST_START_DATE}/removed"
                , banListInstance.links.getLink("Ban List Removed Content").get().href)

            Assertions.assertEquals("/card/${TestConstants.STRATOS_ID}?allInfo=true"
                , forbidden[0].links.getLink("self").get().href)
            Assertions.assertEquals("/card/${TestConstants.A_HERO_LIVES_ID}?allInfo=true"
                , limited[0].links.getLink("self").get().href)
            Assertions.assertEquals("/card/${TestConstants.D_MALICIOUS_ID}?allInfo=true"
                , semiLimited[0].links.getLink("self").get().href)

            // verify effects of cards returned in ban list array depending on option used for saveBandwidth
            if (saveBandwidth) {
                Assertions.assertEquals(Card.trimEffect(TestConstants.STRATOS_FULL_EFFECT), forbidden[0].cardEffect)
                Assertions.assertEquals(Card.trimEffect(TestConstants.A_HERO_LIVES_FULL_EFFECT), limited[0].cardEffect)
                Assertions.assertEquals(Card.trimEffect(TestConstants.D_MALICIOUS_FULL_EFFECT), semiLimited[0].cardEffect)
            } else {
                Assertions.assertEquals(TestConstants.STRATOS_FULL_EFFECT, forbidden[0].cardEffect)
                Assertions.assertEquals(TestConstants.A_HERO_LIVES_FULL_EFFECT, limited[0].cardEffect)
                Assertions.assertEquals(TestConstants.D_MALICIOUS_FULL_EFFECT, semiLimited[0].cardEffect)
            }

            // fetch all info was false so new/removed cards from ban list should not have value
            if (fetchAllInfo) {
                Assertions.assertNotNull(banListInstance.newContent)
                Assertions.assertNotNull(banListInstance.removedContent)
            }


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

            if (fetchAllInfo) {
                Mockito.verify(diffService, Mockito.times(1)).getNewContentForGivenBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)
                )
                Mockito.verify(diffService, Mockito.times(1)).getRemovedContentForGivenBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)
                )
            }
        }
    }


    @Nested
    inner class UnhappyPath {
        /**
         * Unhappy path - using Dao helper object resulted in an error. Ban list not found in database .
         */
        @Test
        fun `Test Fetching BanList Instance, From DB, With Full Text, Ban List Not In DB`() {
            dbError_BanListNotInDB(false)
        }


        /**
         * Unhappy path - using Dao helper object resulted in an error. Ban list not found in database .
         */
        @Test
        fun `Test Fetching BanList Instance, From DB, With Trimmed Text, Ban List Not In DB`() {
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
            Assertions.assertEquals(String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, TestConstants.BAN_LIST_START_DATE), exCause.message)
            Assertions.assertEquals(HttpStatus.NOT_FOUND, exCause.httpStatus)
            Assertions.assertEquals(ErrorType.D001, exCause.errorType)


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
}