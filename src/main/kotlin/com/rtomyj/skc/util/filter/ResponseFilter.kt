package com.rtomyj.skc.util.filter

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class ResponseFilter : WebFilter {
    override fun filter(
        serverWebExchange: ServerWebExchange, chain: WebFilterChain
    ): Mono<Void> = chain.filter(serverWebExchange).doOnSubscribe {
        serverWebExchange.response.headers.add("Cache-Control", "max-age=300")
    }
}