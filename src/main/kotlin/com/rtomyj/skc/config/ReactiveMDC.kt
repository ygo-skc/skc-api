package com.rtomyj.skc.config

import org.slf4j.MDC
import reactor.core.publisher.Mono

class ReactiveMDC {
  companion object {
    @JvmStatic
    fun <T : Any> deferMDC(m: Mono<T>): Mono<T> = Mono.deferContextual {
      MDC.setContextMap(it.getOrDefault("MDC", emptyMap()))
      m
    }
  }
}