package com.rtomyj.skc.config

import com.rtomyj.skc.constant.TestConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * Blocking work here is always a JDBC query, so the number of threads that may run one at a time is capped at
 * the Hikari pool size. Past that a thread cannot get a connection anyway - it would sit in Hikari's acquire
 * and eventually throw - so the queueing is kept in Reactor instead.
 */
@SpringJUnitConfig(classes = [BlockingJDBCSchedulerConfig::class])
@TestPropertySource(properties = ["spring.datasource.hikari.maximumPoolSize=${TestConstants.JDBC_POOL_SIZE}"])
class BlockingJDBCSchedulerConfigTest {
  @Test
  fun `blocking call runs on the dedicated JDBC scheduler`() {
    val daoThreadName = AtomicReference<String>()

    StepVerifier
        .create(blockingJDBCMono {
          daoThreadName.set(
            Thread
                .currentThread()
                .name
          )
        })
        .expectNextCount(1)
        .verifyComplete()

    Assertions.assertTrue(
      daoThreadName
          .get()
          .startsWith(JDBC_SCHEDULER_NAME),
      "Expected blocking call on the dedicated JDBC scheduler but it ran on ${daoThreadName.get()}"
    )
  }

  @Test
  fun `blocking call runs on a virtual thread`() {
    val virtual = AtomicBoolean()

    StepVerifier
        .create(blockingJDBCMono {
          virtual.set(
            Thread
                .currentThread()
                .isVirtual
          )
        })
        .expectNextCount(1)
        .verifyComplete()

    Assertions.assertTrue(
      virtual.get(),
      "spring.threads.virtual.enabled puts the shared boundedElastic on virtual threads - the dedicated one must not silently opt back out"
    )
  }

  @Test
  fun `concurrent blocking calls are capped at the connection pool size`() {
    val inFlight = AtomicInteger()
    val peakInFlight = AtomicInteger()

    val calls = (1..TestConstants.JDBC_POOL_SIZE * 4).map {
      blockingJDBCMono {
        val current = inFlight.incrementAndGet()
        peakInFlight.updateAndGet { peak -> maxOf(peak, current) }
        Thread.sleep(100)
        inFlight.decrementAndGet()
      }
    }

    StepVerifier
        .create(Flux.merge(calls))
        .expectNextCount(calls.size.toLong())
        .verifyComplete()

    Assertions.assertEquals(
      TestConstants.JDBC_POOL_SIZE,
      peakInFlight.get(),
      "Blocking calls should saturate the pool but never exceed it - more would just block in Hikari"
    )
  }
}
