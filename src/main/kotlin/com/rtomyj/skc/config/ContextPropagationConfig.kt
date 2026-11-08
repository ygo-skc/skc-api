package com.rtomyj.skc.config

import io.micrometer.context.ContextRegistry
import io.micrometer.context.integration.Slf4jThreadLocalAccessor
import org.springframework.context.annotation.Configuration

@Configuration
class ContextPropagationConfig {
    init {
        // restores whole MDC map from Reactor context on thread hops (spring.reactor.context-propagation=auto)
        ContextRegistry.getInstance().registerThreadLocalAccessor(Slf4jThreadLocalAccessor())
    }
}
