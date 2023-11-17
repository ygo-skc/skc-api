package com.rtomyj.skc.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig {
    @Bean("skc-suggestion-engine-web-client")
    fun skcSuggestionEngineWebClient(
        @Value("\${api.skcSuggestionEngine.key}") apiKey: String,
        @Value("\${api.skcSuggestionEngine.uri}") skcSuggestionEngineUri: String
    ): WebClient =
        WebClient.builder().filter(skcSuggestionEngineRequestFilter(apiKey)).baseUrl(skcSuggestionEngineUri).build()

    private fun skcSuggestionEngineRequestFilter(apiKey: String): ExchangeFilterFunction {
        return ExchangeFilterFunction { clientRequest: ClientRequest, next: ExchangeFunction ->
            next.exchange(ClientRequest.from(clientRequest).header("API-Key", apiKey).build())
        }
    }
}