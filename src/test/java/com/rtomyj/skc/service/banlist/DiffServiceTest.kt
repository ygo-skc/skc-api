package com.rtomyj.skc.service.banlist

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListNewContent
import com.rtomyj.skc.model.banlist.BanListRemovedContent
import org.junit.jupiter.api.*
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
@ContextConfiguration(classes = [DiffService::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
class DiffServiceTest {
    @MockBean(name = "jdbc")
    private lateinit var dao: Dao

    @Autowired
    private lateinit var diffService: DiffService

    private val banListNewContent: BanListNewContent
    private val banListRemovedContent: BanListRemovedContent


    init {
        val mapper = ObjectMapper()

        banListNewContent = mapper
            .readValue(ClassPathResource(TestConstants.BAN_LIST_NEW_CONTENT).file, BanListNewContent::class.java)

        banListRemovedContent = mapper
            .readValue(ClassPathResource(TestConstants.BAN_LIST_REMOVED_CONTENT).file, BanListRemovedContent::class.java)
    }
    

    @Nested
    inner class HappyPath {
        @Test
        fun testFetchingBanListNewContent_FromDB_Success() {
            Mockito.`when`(
                dao.getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
                )
            )
                .thenReturn(banListNewContent.newForbidden)
            Mockito.`when`(
                dao.getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.LIMITED)
                )
            )
                .thenReturn(banListNewContent.newLimited)
            Mockito.`when`(
                dao.getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
                )
            )
                .thenReturn(banListNewContent.newSemiLimited)
            Mockito.`when`(
                dao.isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
            )
                .thenReturn(true)
            Mockito.`when`(
                dao.getPreviousBanListDate(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
            )
                .thenReturn(TestConstants.PREVIOUS_BAN_LIST_START_DATE)


            // call method with above mocks
            val banListNewContentInstance = diffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)

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
            Assertions.assertEquals(TestConstants.STRATOS_ID, newForbiddenCards[0].cardId)
            Assertions.assertEquals("Limited", newForbiddenCards[0].previousBanStatus)

            Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, newLimitedCards[0].cardId)
            Assertions.assertEquals("Unlimited", newLimitedCards[0].previousBanStatus)

            Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, newLimitedCards[1].cardId)
            Assertions.assertEquals("Forbidden", newLimitedCards[1].previousBanStatus)

            // ensure self link is present
            Assertions.assertNotNull(newForbiddenCards[0].links.getLink("self"))
            Assertions.assertNotNull(newLimitedCards[0].links.getLink("self"))
            Assertions.assertNotNull(newLimitedCards[1].links.getLink("self"))

            // ensure href is as expected
            Assertions.assertEquals("/card/${TestConstants.STRATOS_ID}?allInfo=false"
                , newForbiddenCards[0].links.getLink("self").get().href)
            Assertions.assertEquals("/card/${TestConstants.A_HERO_LIVES_ID}?allInfo=false"
                , newLimitedCards[0].links.getLink("self").get().href)
            Assertions.assertEquals("/card/${TestConstants.D_MALICIOUS_ID}?allInfo=false"
                , newLimitedCards[1].links.getLink("self").get().href)


            // ensure mocks are called appropriate number of times
            Mockito.verify(dao, Mockito.times(1))
                .getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
                )
            Mockito.verify(dao, Mockito.times(1))
                .getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.LIMITED)
                )
            Mockito.verify(dao, Mockito.times(1))
                .getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
                )
            Mockito.verify(dao, Mockito.times(1))
                .isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(dao, Mockito.times(1))
                .getPreviousBanListDate(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
        }


        @Test
        fun testFetchingBanListRemovedContent_FromDB_Success() {
            Mockito.`when`(
                dao.getRemovedContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)
                )
            )
                .thenReturn(banListRemovedContent.removedCards)
            Mockito.`when`(
                dao.getPreviousBanListDate(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)
                )
            )
                .thenReturn(TestConstants.PREVIOUS_BAN_LIST_START_DATE)
            Mockito.`when`(
                dao.isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)
                )
            )
                .thenReturn(true)


            // call code w/ above mocks
            val banListRemovedContentInstance = diffService.getRemovedContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
            val removedCards = banListRemovedContentInstance.removedCards


            // sanity
            Assertions.assertNotNull(removedCards)

            // ensure dates make sense with context of test request
            Assertions.assertEquals(TestConstants.BAN_LIST_START_DATE, banListRemovedContentInstance.listRequested)
            Assertions.assertEquals(TestConstants.PREVIOUS_BAN_LIST_START_DATE, banListRemovedContentInstance.comparedTo)

            // validate number of cards being returned as removed is as expected
            Assertions.assertEquals(3, banListRemovedContentInstance.numRemoved)
            Assertions.assertEquals(3, removedCards.size)

            // ensure correct values are being returned in removedCards array
            Assertions.assertEquals(TestConstants.STRATOS_ID, removedCards[0].cardId)
            Assertions.assertEquals(TestConstants.STRATOS_NAME, removedCards[0].cardName)
            Assertions.assertEquals("Forbidden", removedCards[0].previousBanStatus)

            Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, removedCards[1].cardId)
            Assertions.assertEquals(TestConstants.A_HERO_LIVES_NAME, removedCards[1].cardName)
            Assertions.assertEquals("Limited", removedCards[1].previousBanStatus)

            Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, removedCards[2].cardId)
            Assertions.assertEquals(TestConstants.D_MALICIOUS_NAME, removedCards[2].cardName)
            Assertions.assertEquals("Semi-Limited", removedCards[2].previousBanStatus)

            // ensure self link is present
            Assertions.assertNotNull(removedCards[0].links.getLink("self"))
            Assertions.assertNotNull(removedCards[1].links.getLink("self"))
            Assertions.assertNotNull(removedCards[2].links.getLink("self"))

            // ensure href is as expected
            Assertions.assertEquals(
                "/card/${TestConstants.STRATOS_ID}?allInfo=false"
                , removedCards[0].links.getLink("self").get().href
            )
            Assertions.assertEquals(
                "/card/${TestConstants.A_HERO_LIVES_ID}?allInfo=false"
                , removedCards[1].links.getLink("self").get().href
            )
            Assertions.assertEquals(
                "/card/${TestConstants.D_MALICIOUS_ID}?allInfo=false"
                , removedCards[2].links.getLink("self").get().href
            )


            // verify mocks are called the correct number of times
            Mockito.verify(dao, Mockito.times(1))
                .getRemovedContentOfBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(dao, Mockito.times(1))
                .getPreviousBanListDate(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(dao, Mockito.times(1))
                .isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
        }
    }


    @Nested
    inner class UnhappyPath {
        @Test
        fun testFetchingBanListNewContent_FromDB_Failure() {
            Mockito.`when`(
                dao.getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
                )
            )
                .thenReturn(ArrayList())
            Mockito.`when`(
                dao.getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.LIMITED)
                )
            )
                .thenReturn(ArrayList())
            Mockito.`when`(
                dao.getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
                )
            )
                .thenReturn(ArrayList())


            Assertions.assertThrows(YgoException::class.java) {
                diffService.getNewContentForGivenBanList(
                    TestConstants.BAN_LIST_START_DATE
                )
            }


            Mockito.verify(dao, Mockito.times(0))
                .getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.FORBIDDEN)
                )
            Mockito.verify(dao, Mockito.times(0))
                .getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.LIMITED)
                )
            Mockito.verify(dao, Mockito.times(0))
                .getNewContentOfBanList(
                    ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE),
                    ArgumentMatchers.eq(Dao.Status.SEMI_LIMITED)
                )
            Mockito.verify(dao, Mockito.times(1))
                .isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(dao, Mockito.times(0))
                .getPreviousBanListDate(ArgumentMatchers.any(String::class.java))
        }


        @Test
        fun testFetchingBanListRemovedContent_FromDB_Failure() {
            Mockito.`when`(dao.isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
                .thenReturn(false)
            Mockito.`when`(dao.getRemovedContentOfBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
                .thenReturn(ArrayList())


            // call code w/ above mocks, expecting an exception
            Assertions.assertThrows(YgoException::class.java) {
                diffService.getRemovedContentForGivenBanList(
                    TestConstants.BAN_LIST_START_DATE
                )
            }


            Mockito.verify(dao, Mockito.times(1))
                .isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(dao, Mockito.times(0))
                .getRemovedContentOfBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
            Mockito.verify(dao, Mockito.times(0))
                .getPreviousBanListDate(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
        }
    }

}