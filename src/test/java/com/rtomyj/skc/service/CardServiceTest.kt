package com.rtomyj.skc.service

import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.service.card.CardService
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


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CardService::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the card info after one of the tests executes, ruining other tests
class CardServiceTest {
    @MockBean(name = "jdbc")
    private lateinit var dao: Dao

    @Autowired
    private lateinit var cardService: CardService


    companion object {
        private lateinit var successfulCardReceived: Card


        @BeforeAll
        @JvmStatic
        fun before() {
            successfulCardReceived = Card
                .builder()
                .cardID(TestConstants.STRATOS_ID)
                .cardName(TestConstants.STRATOS_NAME)
                .build()
        }
    }


    /**
     * Happy path - flow where dao is used to retrieve card from DB. Dao object is mocked.
     */
    @Test
    fun testFetchingCard_FromDB_Success() {
        // mock calls
        Mockito.`when`(dao.getCardInfo(ArgumentMatchers.eq(TestConstants.STRATOS_ID)))
            .thenReturn(successfulCardReceived)


        // call code
        val card = cardService.getCardInfo(TestConstants.STRATOS_ID, false)


        // assertions on return value
        Assertions.assertEquals(TestConstants.STRATOS_ID, card.cardID)
        Assertions.assertEquals(TestConstants.STRATOS_NAME, card.cardName)


        // verify mocks are called the exact number of times expected
        Mockito.verify(dao, Mockito.times(1)).getCardInfo(ArgumentMatchers.eq(TestConstants.STRATOS_ID))
    }


    /**
     * Unhappy path - flow where dao is used to retrieve card from DB. Dao object is mocked. An error occurred while fetching using Dao.
     */
    @Test
    fun testFetchingCardFromDB_Failure() {
        // mock calls
        Mockito.`when`(dao.getCardInfo(ArgumentMatchers.eq(TestConstants.ID_THAT_CAUSES_FAILURE)))
            .thenThrow(YgoException(
                ErrConstants.NOT_FOUND_DAO_ERR,
                String.format("Unable to find card in DB with ID: %s", TestConstants.ID_THAT_CAUSES_FAILURE)
            ))


        // call code and assert throws
        Assertions.assertThrows(CacheLoaderException::class.java) {
            cardService.getCardInfo(
                TestConstants.ID_THAT_CAUSES_FAILURE
                , false
            )
        }


        // verify mocks are called the exact number of times expected
        Mockito.verify(
            dao
            , Mockito.times(1)
        ).getCardInfo(ArgumentMatchers.eq(TestConstants.ID_THAT_CAUSES_FAILURE))
    }
}