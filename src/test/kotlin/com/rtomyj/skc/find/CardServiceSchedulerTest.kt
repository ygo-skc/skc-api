package com.rtomyj.skc.find

import com.nhaarman.mockito_kotlin.eq
import com.rtomyj.skc.config.BlockingJDBCSchedulerConfig
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.config.JDBC_SCHEDULER_NAME
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.constant.TestObjects
import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.dao.ProductDao
import com.rtomyj.skc.skcsuggestionengine.TrafficService
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.constant.AppConstants.MDC_CONTEXT_KEY
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicReference

/**
 * Verifies that blocking JDBC work is offloaded from the subscribing thread. Under WebFlux the subscribing
 * thread is a Netty event loop thread, of which there are only a handful, so a blocking call there stalls
 * the whole server.
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CardService::class, DateConfig::class, BlockingJDBCSchedulerConfig::class])
@TestPropertySource(properties = ["spring.datasource.hikari.maximumPoolSize=${TestConstants.JDBC_POOL_SIZE}"])
class CardServiceSchedulerTest {
  @MockitoBean(name = "jdbc")
  private lateinit var cardDao: Dao

  @MockitoBean(name = "product-jdbc")
  private lateinit var productDao: ProductDao

  @MockitoBean(name = "ban-list-jdbc")
  private lateinit var banListDao: BanListDao

  @MockitoBean
  private lateinit var trafficService: TrafficService

  @Autowired
  private lateinit var cardService: CardService

  @BeforeEach
  fun clearMDC() {
    // MDC is a ThreadLocal and test threads are pooled - start from a known-empty state
    MDC.clear()
  }

  private fun stubTrafficService() {
    Mockito
        .`when`(
          trafficService.submitTrafficData(TrafficResourceType.CARD, TestConstants.STRATOS_ID, TestConstants.MOCK_IP)
        )
        .thenReturn(Mono.just(""))
  }

  @Test
  fun `blocking card DAO call does not run on the subscribing thread`() {
    val daoThreadName = AtomicReference<String>()
    Mockito
        .`when`(cardDao.getCardInfo(eq(TestConstants.STRATOS_ID)))
        .thenAnswer {
          daoThreadName.set(
            Thread
                .currentThread()
                .name
          )
          TestObjects.STRATOS_CARD_FULL_TEXT
        }
    stubTrafficService()

    val subscribingThreadName = Thread
        .currentThread()
        .name

    StepVerifier
        .create(cardService.getCardInfo(TestConstants.STRATOS_ID, false, TestConstants.MOCK_IP))
        .assertNext { card -> Assertions.assertEquals(TestConstants.STRATOS_ID, card.cardID) }
        .verifyComplete()

    Assertions.assertNotEquals(
      subscribingThreadName,
      daoThreadName.get(),
      "Blocking JDBC call ran on the subscribing thread - under WebFlux that is a Netty event loop thread"
    )
    Assertions.assertTrue(
      daoThreadName
          .get()
          .startsWith(JDBC_SCHEDULER_NAME),
      "Expected blocking call on the dedicated JDBC scheduler but it ran on ${daoThreadName.get()}"
    )
  }

  @Test
  fun `request MDC is restored onto the thread running the blocking call`() {
    val clientIpSeenByDao = AtomicReference<String>()
    Mockito
        .`when`(cardDao.getCardInfo(eq(TestConstants.STRATOS_ID)))
        .thenAnswer {
          clientIpSeenByDao.set(MDC.get(AppConstants.CLIENT_IP_MDC))
          TestObjects.STRATOS_CARD_FULL_TEXT
        }
    stubTrafficService()

    StepVerifier
        .create(cardService
            .getCardInfo(TestConstants.STRATOS_ID, false, TestConstants.MOCK_IP)
            .contextWrite {
              it.put(MDC_CONTEXT_KEY, mapOf(AppConstants.CLIENT_IP_MDC to TestConstants.MOCK_IP))
            })
        .assertNext { card -> Assertions.assertEquals(TestConstants.STRATOS_ID, card.cardID) }
        .verifyComplete()

    Assertions.assertEquals(
      TestConstants.MOCK_IP,
      clientIpSeenByDao.get(),
      "Request MDC was not carried onto the worker thread, so logs lose request context"
    )
  }
}
