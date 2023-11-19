package com.rtomyj.skc.config

import com.rtomyj.skc.exception.DownStreamException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Configuration
class WebClientConfig {
  @Bean("skc-suggestion-engine-web-client")
  fun skcSuggestionEngineWebClient(
    @Value("\${api.skc-suggestion-engine.key}") apiKey: String,
    @Value("\${api.skc-suggestion-engine.base-uri}") skcSuggestionEngineBaseUri: String
  ): WebClient = WebClient
      .builder()
      .filter(ExchangeFilterFunction.ofRequestProcessor { request ->
        Mono.just(
          ClientRequest
              .from(request)
              .header("API-Key", apiKey)
              .build()
        )
      })
      .filter(ExchangeFilterFunction.ofResponseProcessor { response ->
        webClientExceptionHandler(response)
      })
      .baseUrl(skcSuggestionEngineBaseUri)
      .build()


  private fun webClientExceptionHandler(response: ClientResponse): Mono<ClientResponse> {
    val statusCode = response.statusCode()
    return if (statusCode.is4xxClientError || statusCode.is5xxServerError) {
      response
          .bodyToMono(String::class.java)
          .flatMap { body ->
            Mono.error(DownStreamException(body, statusCode.value()))
          }
    } else {
      Mono.just(response)
    }
  }
}