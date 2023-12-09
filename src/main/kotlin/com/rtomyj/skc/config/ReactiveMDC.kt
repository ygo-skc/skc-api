package com.rtomyj.skc.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import reactor.core.publisher.Mono

class ReactiveMDC {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @JvmStatic
    fun <T> deferMDC(m: Mono<T>): Mono<T> = Mono.deferContextual {
      MDC.setContextMap(it.getOrDefault<Map<String, String>>("MDC", emptyMap()))
      log.info("defer mdc")
      m
    }
  }
}