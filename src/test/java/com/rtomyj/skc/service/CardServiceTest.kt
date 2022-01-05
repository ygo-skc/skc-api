package com.rtomyj.skc.service

import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.service.card.CardService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
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
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the card info after one of the tests executes, ruining other tests
class CardServiceTest {
    @MockBean(name = "jdbc")
    private lateinit var dao: Dao

    @Autowired
    private lateinit var cardService: CardService

    private val successfulCardReceived: Card = Card
        .builder()
        .cardID(TestConstants.STRATOS_ID)
        .cardName(TestConstants.STRATOS_NAME)
        .build()


    @Nested
    inner class HappyPath {
        /**
         * Happy path - flow where dao is used to retrieve card from DB. Dao object is mocked.
         */
        @Test
        fun `Test Fetching Card From DB, Success`() {
            // mock calls
            Mockito.`when`(dao.getCardInfo(ArgumentMatchers.eq(TestConstants.STRATOS_ID)))
                .thenReturn(successfulCardReceived)


            // call code
            val card = cardService.getCardInfo(TestConstants.STRATOS_ID, false)


            // assertions on return value
            Assertions.assertEquals(TestConstants.STRATOS_ID, card.cardID)
            Assertions.assertEquals(TestConstants.STRATOS_NAME, card.cardName)


            // verify mocks are called the exact number of times expected
            Mockito.verify(dao, Mockito.times(1))
                .getCardInfo(ArgumentMatchers.eq(TestConstants.STRATOS_ID))
        }
    }


    @Nested
    inner class UnhappyPath {
        /**
         * Unhappy path - flow where dao is used to retrieve card from DB. Dao object is mocked. An error occurred while fetching using Dao.
         */
        @Test
        fun `Test Fetching Card From DB, Failure`() {
            // mock calls
            Mockito.`when`(dao.getCardInfo(ArgumentMatchers.eq(TestConstants.ID_THAT_CAUSES_FAILURE)))
                .thenThrow(YgoException(
                    String.format("Unable to find card in DB with ID: %s", TestConstants.ID_THAT_CAUSES_FAILURE), ErrorType.D001
                ))


            // call code and assert throws
            Assertions.assertThrows(YgoException::class.java) {
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
}