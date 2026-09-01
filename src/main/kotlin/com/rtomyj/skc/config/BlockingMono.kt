package com.rtomyj.skc.config

import org.slf4j.MDC
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

const val MDC_CONTEXT_KEY = "MDC"

fun <T : Any> blockingMono(supplier: () -> T): Mono<T> = Mono
    .deferContextual { ctx ->
      val mdc = ctx.getOrDefault(MDC_CONTEXT_KEY, emptyMap<String, String>())!!

      Mono.fromCallable {
        // the worker is pooled, so an empty map has to clear rather than merely not overwrite
        if (mdc.isEmpty()) MDC.clear() else MDC.setContextMap(mdc)
        supplier()
      }
    }
    .subscribeOn(Schedulers.boundedElastic())
