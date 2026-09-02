package com.rtomyj.skc.config

import com.rtomyj.skc.util.constant.AppConstants.MDC_CONTEXT_KEY
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

const val JDBC_SCHEDULER_NAME = "jdbc"

private val SCHEDULER_FACTORY = object : Schedulers.Factory {}

/**
 * Replaced at startup by [BlockingJDBCSchedulerConfig.initJDBCScheduler]
 */
@Volatile
private var blockingJDBCScheduler: Scheduler = Schedulers.boundedElastic()

@Configuration
class BlockingJDBCSchedulerConfig(
  @param:Value($$"${spring.datasource.hikari.maximumPoolSize}") private val maximumPoolSize: Int) {
  /**
   * Configuring schedular for jdbc blocking tasks to avoid blocking flux event loops
   */
  @PostConstruct
  fun initJDBCScheduler() {
    blockingJDBCScheduler = SCHEDULER_FACTORY
        .newThreadPerTaskBoundedElastic(
          maximumPoolSize,
          Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE,
          Thread
              .ofVirtual()
              .name("$JDBC_SCHEDULER_NAME-", 1)
              .factory()
        )
        .apply { init() }
  }

  @PreDestroy
  fun disposeJDBCScheduler() = blockingJDBCScheduler.dispose()
}

fun <T : Any> blockingJDBCMono(supplier: () -> T): Mono<T> = Mono
    .deferContextual { ctx ->
      val mdc = ctx.getOrDefault(MDC_CONTEXT_KEY, emptyMap<String, String>())!!

      Mono.fromCallable {
        // the worker may be reused depending on the scheduler, so an empty map has to clear rather than merely not overwrite
        if (mdc.isEmpty()) MDC.clear() else MDC.setContextMap(mdc)
        supplier()
      }
    }
    .subscribeOn(blockingJDBCScheduler)