package com.rtomyj.skc.config

import com.rtomyj.skc.exception.DownStreamException
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig {
  @Bean("skc-suggestion-engine-web-client")
  fun skcSuggestionEngineWebClient(
    @Value("\${api.skc-suggestion-engine.key}") apiKey: String,
    @Value("\${api.skc-suggestion-engine.base-uri}") skcSuggestionEngineBaseUri: String
  ): WebClient = WebClient
      .builder()
      .clientConnector(
        ReactorClientHttpConnector(
          HttpClient
              .create(
                ConnectionProvider
                    .builder("skc-suggestion-engine-pool")
                    .maxConnections(50)
                    .maxIdleTime(Duration.ofMinutes(5))
                    .maxLifeTime(Duration.ofMinutes(10))
                    .pendingAcquireTimeout(Duration.ofSeconds(5))
                    .evictInBackground(Duration.ofSeconds(5))
                    .build()
              )
              .responseTimeout(Duration.ofMillis(250))
              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 250)
              .option(ChannelOption.SO_KEEPALIVE, true)
              .doOnConnected { conn ->
                conn
                    .addHandlerLast(ReadTimeoutHandler(800, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(800, TimeUnit.MILLISECONDS))
              }
        )
      )
      .defaultHeaders { headers ->
        headers["API-Key"] = apiKey
      }
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
      Mono.fromCallable {
        response
      }
    }
  }
}