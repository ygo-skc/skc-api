package com.rtomyj.skc.find

import com.nhaarman.mockito_kotlin.eq
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.constant.TestObjects
import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.dao.ProductDao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.skcsuggestionengine.TrafficService
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.lang.Thread.sleep


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CardService::class, DateConfig::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the card info after one of the tests executes, ruining other tests
class CardServiceTest {
  @MockBean(name = "jdbc")
  private lateinit var cardDao: Dao

  @MockBean(name = "product-jdbc")
  private lateinit var productDao: ProductDao

  @MockBean(name = "ban-list-jdbc")
  private lateinit var banListDao: BanListDao

  @MockBean
  private lateinit var trafficService: TrafficService

  @Autowired
  private lateinit var cardService: CardService

  private val successfulCardReceived: Card = TestObjects.STRATOS_CARD_FULL_TEXT

  @Nested
  inner class HappyPath {
    /**
     * Happy path - flow where cardDao is used to retrieve card from DB. Dao object is mocked.
     */
    @Test
    fun `Test Fetching Card From DB, Success`() {
      // mock calls
      Mockito
          .`when`(cardDao.getCardInfo(eq(TestConstants.STRATOS_ID)))
          .thenReturn(successfulCardReceived)
      Mockito
          .`when`(
            trafficService.submitTrafficData(TrafficResourceType.CARD, TestConstants.STRATOS_ID, TestConstants.MOCK_IP)
          )
          .thenReturn(Mono.just(""))

      // call code
      StepVerifier
          .create(cardService.getCardInfo(TestConstants.STRATOS_ID, false, TestConstants.MOCK_IP))
          .assertNext { card ->
            // assertions on return value
            Assertions.assertEquals(TestConstants.STRATOS_ID, card.cardID)
            Assertions.assertEquals(TestConstants.STRATOS_NAME, card.cardName)
          }
          .verifyComplete()

      // verify mocks are called the exact number of times expected
      sleep(200)  // sleep as traffic call is async, and we will verify the call below
      Mockito
          .verify(cardDao, Mockito.times(1))
          .getCardInfo(eq(TestConstants.STRATOS_ID))
      Mockito
          .verify(trafficService)
          .submitTrafficData(TrafficResourceType.CARD, TestConstants.STRATOS_ID, TestConstants.MOCK_IP)
    }
  }

  @Nested
  inner class UnhappyPath {
    /**
     * Unhappy path - flow where cardDao is used to retrieve card from DB. Dao object is mocked. An error occurred while fetching using Dao.
     */
    @Test
    fun `Test Fetching Card From DB, Failure`() {
      // mock calls
      Mockito
          .`when`(cardDao.getCardInfo(eq(TestConstants.ID_THAT_CAUSES_FAILURE)))
          .thenThrow(
            SKCException(
              String.format("Unable to find card in DB with ID: %s", TestConstants.ID_THAT_CAUSES_FAILURE),
              ErrorType.DB001
            )
          )
      Mockito
          .`when`(
            trafficService.submitTrafficData(
              TrafficResourceType.CARD,
              TestConstants.ID_THAT_CAUSES_FAILURE,
              TestConstants.MOCK_IP
            )
          )
          .thenReturn(Mono.just(""))

      // call code and assert throws
      StepVerifier
          .create(
            cardService.getCardInfo(
              TestConstants.ID_THAT_CAUSES_FAILURE, false, TestConstants.MOCK_IP
            )
          )
          .verifyError()
//      Assertions.assertThrows(SKCException::class.java) { todo how ot catch speicifc error from stepverififier
//
//      }

      // verify mocks are called the exact number of times expected
      sleep(200)  // sleep as traffic call is async, and we will verify the call below
      Mockito
          .verify(
            cardDao, Mockito.times(1)
          )
          .getCardInfo(eq(TestConstants.ID_THAT_CAUSES_FAILURE))
      Mockito
          .verify(trafficService)
          .submitTrafficData(TrafficResourceType.CARD, TestConstants.ID_THAT_CAUSES_FAILURE, TestConstants.MOCK_IP)
    }
  }
}