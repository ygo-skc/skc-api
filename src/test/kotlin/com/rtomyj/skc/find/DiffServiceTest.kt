package com.rtomyj.skc.find

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListNewContent
import com.rtomyj.skc.model.BanListRemovedContent
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [BanListDiffService::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
class DiffServiceTest {
    @MockBean(name = "ban-list-jdbc")
    private lateinit var banListDao: BanListDao

    @Autowired
    private lateinit var banListDiffService: BanListDiffService

    private val banListNewContent: BanListNewContent
    private val banListRemovedContent: BanListRemovedContent


    init {
        val mapper = jacksonObjectMapper()

        banListNewContent = mapper
            .readValue(ClassPathResource(TestConstants.BAN_LIST_NEW_CONTENT).file, BanListNewContent::class.java)

        banListRemovedContent = mapper
            .readValue(
                ClassPathResource(TestConstants.BAN_LIST_REMOVED_CONTENT).file,
                BanListRemovedContent::class.java
            )
    }


    @Nested
    inner class HappyPath {
        @Test
        fun testFetchingBanListNewContent_FromDB_Success() {
            assertNotNull(banListDao)
            Mockito.`when`(
                banListDao.getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.FORBIDDEN),
                    eq("TCG")
                )
            )
                .thenReturn(banListNewContent.newForbidden)
            Mockito.`when`(
                banListDao.getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.LIMITED),
                    eq("TCG")
                )
            )
                .thenReturn(banListNewContent.newLimited)
            Mockito.`when`(
                banListDao.getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.SEMI_LIMITED),
                    eq("TCG")
                )
            )
                .thenReturn(banListNewContent.newSemiLimited)
            Mockito.`when`(
                banListDao.isValidBanList(eq(TestConstants.BAN_LIST_START_DATE))
            )
                .thenReturn(true)
            Mockito.`when`(
                banListDao.getPreviousBanListDate(eq(TestConstants.BAN_LIST_START_DATE), eq("TCG"))
            )
                .thenReturn(TestConstants.PREVIOUS_BAN_LIST_START_DATE)


            // call method with above mocks
            val banListNewContentInstance =
                banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")

            val newForbiddenCards = banListNewContentInstance.newForbidden
            val newLimitedCards = banListNewContentInstance.newLimited
            val newSemiLimitedCards = banListNewContentInstance.newSemiLimited


            // ensure correct dates are used
            Assertions.assertEquals(TestConstants.BAN_LIST_START_DATE, banListNewContentInstance.listRequested)
            Assertions.assertEquals(TestConstants.PREVIOUS_BAN_LIST_START_DATE, banListNewContentInstance.comparedTo)

            // ensure size of each object is returned to consumer, null values will cause issues
            Assertions.assertNotNull(newForbiddenCards)
            Assertions.assertNotNull(newLimitedCards)
            Assertions.assertNotNull(newSemiLimitedCards)

            // ensure returned number of cards is as expected for each status
            Assertions.assertEquals(1, banListNewContentInstance.numNewForbidden)
            Assertions.assertEquals(2, banListNewContentInstance.numNewLimited)
            Assertions.assertEquals(0, banListNewContentInstance.numNewSemiLimited)

            // ensure array returned has the same number of elements being reported by size field, size field is validated in above block
            Assertions.assertEquals(1, newForbiddenCards.size)
            Assertions.assertEquals(2, newLimitedCards.size)
            Assertions.assertEquals(0, newSemiLimitedCards.size)

            // validate correct data is returned in each list for each ban status
            Assertions.assertEquals(TestConstants.STRATOS_ID, newForbiddenCards[0].card.cardID)
            Assertions.assertEquals("Limited", newForbiddenCards[0].previousBanStatus)

            Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, newLimitedCards[0].card.cardID)
            Assertions.assertEquals("Unlimited", newLimitedCards[0].previousBanStatus)

            Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, newLimitedCards[1].card.cardID)
            Assertions.assertEquals("Forbidden", newLimitedCards[1].previousBanStatus)

            // ensure mocks are called appropriate number of times
            Mockito.verify(banListDao, Mockito.times(1))
                .getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.FORBIDDEN),
                    eq("TCG")
                )
            Mockito.verify(banListDao, Mockito.times(1))
                .getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.LIMITED),
                    eq("TCG")
                )
            Mockito.verify(banListDao, Mockito.times(1))
                .getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.SEMI_LIMITED),
                    eq("TCG")
                )
            Mockito.verify(banListDao, Mockito.times(1))
                .isValidBanList(eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(banListDao, Mockito.times(1))
                .getPreviousBanListDate(eq(TestConstants.BAN_LIST_START_DATE), eq("TCG"))
        }


        @Test
        fun testFetchingBanListRemovedContent_FromDB_Success() {
            Mockito.`when`(
                banListDao.getRemovedContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq("TCG")
                )
            )
                .thenReturn(banListRemovedContent.removedCards)
            Mockito.`when`(
                banListDao.getPreviousBanListDate(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq("TCG")
                )
            )
                .thenReturn(TestConstants.PREVIOUS_BAN_LIST_START_DATE)
            Mockito.`when`(
                banListDao.isValidBanList(
                    eq(TestConstants.BAN_LIST_START_DATE)
                )
            )
                .thenReturn(true)


            // call code w/ above mocks
            val banListRemovedContentInstance =
                banListDiffService.getRemovedContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
            val removedCards = banListRemovedContentInstance.removedCards


            // sanity
            Assertions.assertNotNull(removedCards)

            // ensure dates make sense with context of test request
            Assertions.assertEquals(TestConstants.BAN_LIST_START_DATE, banListRemovedContentInstance.listRequested)
            Assertions.assertEquals(
                TestConstants.PREVIOUS_BAN_LIST_START_DATE,
                banListRemovedContentInstance.comparedTo
            )

            // validate number of cards being returned as removed is as expected
            Assertions.assertEquals(3, banListRemovedContentInstance.numRemoved)
            Assertions.assertEquals(3, removedCards.size)

            // ensure correct values are being returned in removedCards array
            Assertions.assertEquals(TestConstants.STRATOS_ID, removedCards[0].card.cardID)
            Assertions.assertEquals(TestConstants.STRATOS_NAME, removedCards[0].card.cardName)
            Assertions.assertEquals("Forbidden", removedCards[0].previousBanStatus)

            Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, removedCards[1].card.cardID)
            Assertions.assertEquals(TestConstants.A_HERO_LIVES_NAME, removedCards[1].card.cardName)
            Assertions.assertEquals("Limited", removedCards[1].previousBanStatus)

            Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, removedCards[2].card.cardID)
            Assertions.assertEquals(TestConstants.D_MALICIOUS_NAME, removedCards[2].card.cardName)
            Assertions.assertEquals("Semi-Limited", removedCards[2].previousBanStatus)

            // verify mocks are called the correct number of times
            Mockito.verify(banListDao, Mockito.times(1))
                .getRemovedContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE), eq("TCG")
                )
            Mockito.verify(banListDao, Mockito.times(1))
                .getPreviousBanListDate(eq(TestConstants.BAN_LIST_START_DATE), eq("TCG"))
            Mockito.verify(banListDao, Mockito.times(1))
                .isValidBanList(eq(TestConstants.BAN_LIST_START_DATE))
        }
    }


    @Nested
    inner class UnhappyPath {
        @Test
        fun testFetchingBanListNewContent_FromDB_Failure() {
            Mockito.`when`(
                banListDao.getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.FORBIDDEN),
                    eq("TCG")
                )
            )
                .thenReturn(ArrayList())
            Mockito.`when`(
                banListDao.getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.LIMITED),
                    eq("TCG")
                )
            )
                .thenReturn(ArrayList())
            Mockito.`when`(
                banListDao.getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.SEMI_LIMITED),
                    eq("TCG")
                )
            )
                .thenReturn(ArrayList())


            Assertions.assertThrows(SKCException::class.java) {
                banListDiffService.getNewContentForGivenBanList(
                    TestConstants.BAN_LIST_START_DATE,
                    eq("TCG")
                )
            }


            Mockito.verify(banListDao, Mockito.times(0))
                .getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.FORBIDDEN),
                    eq("TCG")
                )
            Mockito.verify(banListDao, Mockito.times(0))
                .getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.LIMITED),
                    eq("TCG")
                )
            Mockito.verify(banListDao, Mockito.times(0))
                .getNewContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE),
                    eq(BanListCardStatus.SEMI_LIMITED),
                    eq("TCG")
                )
            Mockito.verify(banListDao, Mockito.times(1))
                .isValidBanList(eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(banListDao, Mockito.times(0))
                .getPreviousBanListDate(any<String>(), eq("TCG"))
        }


        @Test
        fun testFetchingBanListRemovedContent_FromDB_Failure() {
            Mockito.`when`(banListDao.isValidBanList(eq(TestConstants.BAN_LIST_START_DATE)))
                .thenReturn(false)
            Mockito.`when`(
                banListDao.getRemovedContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE), eq("TCG")
                )
            )
                .thenReturn(ArrayList())


            // call code w/ above mocks, expecting an exception
            Assertions.assertThrows(SKCException::class.java) {
                banListDiffService.getRemovedContentForGivenBanList(
                    TestConstants.BAN_LIST_START_DATE,
                    eq("TCG")
                )
            }


            Mockito.verify(banListDao, Mockito.times(1))
                .isValidBanList(eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(banListDao, Mockito.times(0))
                .getRemovedContentOfBanList(
                    eq(TestConstants.BAN_LIST_START_DATE),
                    eq(TestConstants.PREVIOUS_BAN_LIST_START_DATE), eq("TCG")
                )
            Mockito.verify(banListDao, Mockito.times(0))
                .getPreviousBanListDate(eq(TestConstants.BAN_LIST_START_DATE), eq("TCG"))
        }
    }

}