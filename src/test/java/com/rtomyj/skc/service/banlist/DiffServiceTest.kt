package com.rtomyj.skc.service.banlist

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.dao.database.Dao
import com.rtomyj.skc.helper.constants.TestConstants
import com.rtomyj.skc.helper.exceptions.YgoException
import com.rtomyj.skc.model.banlist.BanListNewContent
import com.rtomyj.skc.model.banlist.BanListRemovedContent
import org.cache2k.io.CacheLoaderException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.io.File


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DiffService::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
class DiffServiceTest {
    @MockBean(name = "jdbc")
    private lateinit var dao: Dao

    @Autowired
    private lateinit var diffService: DiffService


    companion object {
        private lateinit var banListNewContent: BanListNewContent
        private lateinit var banListRemovedContent: BanListRemovedContent
        
        
        @BeforeAll
        @JvmStatic
        fun before() {
            val mapper = ObjectMapper()
            banListNewContent =
                mapper.readValue(File(TestConstants.BAN_LIST_NEW_CONTENT), BanListNewContent::class.java)
            banListRemovedContent = mapper.readValue(
                File(TestConstants.BAN_LIST_REMOVED_CONTENT),
                BanListRemovedContent::class.java
            )
        }
    }
    
    
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
        Mockito.`when`(dao.isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
            .thenReturn(true)
        Mockito.`when`(dao.getPreviousBanListDate(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
            .thenReturn(TestConstants.PREVIOUS_BAN_LIST_START_DATE)
        
        
        val banListNewContentInstance = diffService.getNewContentOfBanList(TestConstants.BAN_LIST_START_DATE)

        val newForbiddenCards = banListNewContentInstance.newForbidden
        val newLimitedCards = banListNewContentInstance.newLimited
        val newSemiLimitedCards = banListNewContentInstance.newSemiLimited


        Assertions.assertEquals(TestConstants.BAN_LIST_START_DATE, banListNewContentInstance.listRequested)
        Assertions.assertEquals(TestConstants.PREVIOUS_BAN_LIST_START_DATE, banListNewContentInstance.comparedTo)
        Assertions.assertNotNull(newForbiddenCards)
        Assertions.assertNotNull(newLimitedCards)
        Assertions.assertNotNull(newSemiLimitedCards)
        Assertions.assertEquals(1, newForbiddenCards.size)
        Assertions.assertEquals(1, newLimitedCards.size)
        Assertions.assertEquals(1, newSemiLimitedCards.size)
        Assertions.assertEquals(TestConstants.STRATOS_ID, newForbiddenCards[0].cardId)
        Assertions.assertEquals("Limited", newForbiddenCards[0].previousBanStatus)
        Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, newLimitedCards[0].cardId)
        Assertions.assertEquals("Unlimited", newLimitedCards[0].previousBanStatus)
        Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, newSemiLimitedCards[0].cardId)
        Assertions.assertEquals("Forbidden", newSemiLimitedCards[0].previousBanStatus)


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


        Assertions.assertThrows(CacheLoaderException::class.java) {
            diffService.getNewContentOfBanList(
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
    @Throws(YgoException::class)
    fun testFetchingBanListRemovedContent_FromDB_Success() {
        Mockito.`when`(dao.getRemovedContentOfBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
            .thenReturn(banListRemovedContent.removedCards)
        Mockito.`when`(dao.getPreviousBanListDate(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
            .thenReturn(TestConstants.PREVIOUS_BAN_LIST_START_DATE)
        Mockito.`when`(dao.isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
            .thenReturn(true)


        val banListRemovedContentInstance = diffService.getRemovedContentOfBanList(TestConstants.BAN_LIST_START_DATE)
        val removedCards = banListRemovedContentInstance.removedCards


        Assertions.assertNotNull(removedCards)
        Assertions.assertEquals(TestConstants.BAN_LIST_START_DATE, banListRemovedContentInstance.listRequested)
        Assertions.assertEquals(TestConstants.PREVIOUS_BAN_LIST_START_DATE, banListRemovedContentInstance.comparedTo)
        Assertions.assertEquals(3, removedCards.size)
        Assertions.assertEquals(TestConstants.STRATOS_ID, removedCards[0].cardId)
        Assertions.assertEquals("Forbidden", removedCards[0].previousBanStatus)
        Assertions.assertEquals(TestConstants.A_HERO_LIVES_ID, removedCards[1].cardId)
        Assertions.assertEquals("Limited", removedCards[1].previousBanStatus)
        Assertions.assertEquals(TestConstants.D_MALICIOUS_ID, removedCards[2].cardId)
        Assertions.assertEquals("Semi-Limited", removedCards[2].previousBanStatus)


        Mockito.verify(dao, Mockito.times(1))
            .getRemovedContentOfBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
        Mockito.verify(dao, Mockito.times(1))
            .getPreviousBanListDate(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
        Mockito.verify(dao, Mockito.times(1))
            .isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE))
    }

    
    @Test
    fun testFetchingBanListRemovedContent_FromDB_Failure() {
        Mockito.`when`(dao.isValidBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
            .thenReturn(false)
        Mockito.`when`(dao.getRemovedContentOfBanList(ArgumentMatchers.eq(TestConstants.BAN_LIST_START_DATE)))
            .thenReturn(ArrayList())


        Assertions.assertThrows(CacheLoaderException::class.java) {
            diffService.getRemovedContentOfBanList(
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