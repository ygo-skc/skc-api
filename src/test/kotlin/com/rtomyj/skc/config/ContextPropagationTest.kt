package com.rtomyj.skc.config

import com.rtomyj.skc.util.Blocking
import io.micrometer.context.integration.Slf4jThreadLocalAccessor
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.MDC
import org.springframework.core.PropagationContextElement
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier

class ContextPropagationTest {
    companion object {
        private val MDC_VALUES = mapOf("reqIp" to "10.20.30.40")

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            ContextPropagationConfig()
            Hooks.enableAutomaticContextPropagation() // what spring.reactor.context-propagation=auto does
        }

        @AfterAll
        @JvmStatic
        fun afterAll() = Hooks.disableAutomaticContextPropagation()
    }

    @Test
    fun `MDC is restored inside blocking call on boundedElastic`() {
        StepVerifier
            .create(
                Blocking
                    .mono { MDC.get("reqIp") }
                    .contextWrite { it.put(Slf4jThreadLocalAccessor.KEY, MDC_VALUES) },
            ).expectNext("10.20.30.40")
            .verifyComplete()
    }

    @Test
    fun `MDC is restored after publishOn hop - mirrors WebFlux offloading handler to virtual thread`() {
        StepVerifier
            .create(
                Mono
                    .just("")
                    .publishOn(Schedulers.boundedElastic())
                    .map { MDC.get("reqIp") }
                    .contextWrite { it.put(Slf4jThreadLocalAccessor.KEY, MDC_VALUES) },
            ).expectNext("10.20.30.40")
            .verifyComplete()
    }

    @Test
    fun `MDC is restored inside defer after publishOn hop - mirrors controller building service Mono`() {
        StepVerifier
            .create(
                Mono
                    .just("")
                    .publishOn(Schedulers.boundedElastic())
                    .flatMap { Mono.defer { Mono.just(MDC.get("reqIp")) } }
                    .contextWrite { it.put(Slf4jThreadLocalAccessor.KEY, MDC_VALUES) },
            ).expectNext("10.20.30.40")
            .verifyComplete()
    }

    @Test
    fun `MDC is restored without thread hop - mirrors handler invoked on event loop`() {
        StepVerifier
            .create(
                Mono
                    .just("")
                    .flatMap { Mono.just(MDC.get("reqIp")) }
                    .contextWrite { it.put(Slf4jThreadLocalAccessor.KEY, MDC_VALUES) },
            ).expectNext("10.20.30.40")
            .verifyComplete()
    }

    @Test
    fun `MDC is restored inside coroutine on jdbc dispatcher - mirrors runBlocking fan-out in services`() {
        val jdbcDispatcher = DispatcherConfig().jdbcDispatcher(2)

        StepVerifier
            .create(
                Blocking
                    .mono {
                        runBlocking(PropagationContextElement()) {
                            async(jdbcDispatcher) { MDC.get("reqIp") }.await()
                        }
                    }.contextWrite { it.put(Slf4jThreadLocalAccessor.KEY, MDC_VALUES) },
            ).expectNext("10.20.30.40")
            .verifyComplete()
    }
}
