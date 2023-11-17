package com.rtomyj.skc.config

import com.rtomyj.skc.exception.DownStreamException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono


@Configuration
class WebClientConfig {
    @Bean("skc-suggestion-engine-web-client")
    fun skcSuggestionEngineWebClient(
        @Value("\${api.skcSuggestionEngine.key}") apiKey: String,
        @Value("\${api.skcSuggestionEngine.uri}") skcSuggestionEngineUri: String
    ): WebClient = WebClient.builder().filter(skcSuggestionEngineRequestFilter(apiKey))
        .filter(ExchangeFilterFunction.ofResponseProcessor { response ->
            webClientExceptionHandler(response)
        })
        .baseUrl(skcSuggestionEngineUri).build()

    private fun skcSuggestionEngineRequestFilter(apiKey: String): ExchangeFilterFunction {
        return ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
            next.exchange(ClientRequest.from(request).header("API-Key", apiKey).build())
        }
    }

    private fun webClientExceptionHandler(response: ClientResponse): Mono<ClientResponse> {
        val statusCode = response.statusCode()
        return if (statusCode.is4xxClientError || statusCode.is5xxServerError) {
            response.bodyToMono(String::class.java).flatMap { body ->
                Mono.error(DownStreamException(body, statusCode.value()))
            }
        } else {
            Mono.just(response)
        }
    }
}