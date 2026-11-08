package com.rtomyj.skc.util

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

object Blocking {
    // runs blocking call (e.g. JDBC) on boundedElastic
    fun <T : Any> mono(call: () -> T): Mono<T> = Mono.fromCallable { call() }.subscribeOn(Schedulers.boundedElastic())
}
