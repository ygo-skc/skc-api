package com.rtomyj.skc.skcsuggestionengine

import com.rtomyj.skc.exception.DownStreamException
import com.rtomyj.skc.model.ResourceUtilized
import com.rtomyj.skc.model.Source
import com.rtomyj.skc.model.Traffic
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import reactor.core.publisher.Mono

@Service
class TrafficService @Autowired constructor(
  @param:Qualifier("skc-suggestion-engine-web-client") private val suggestionEngineClient: WebClient,
  @param:Value($$"${api.skc-suggestion-engine.traffic-endpoint}") val trafficEndpoint: String
) {
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }


  fun submitTrafficData(resourceType: TrafficResourceType, resourceValue: String, ip: String): Mono<String> {
    val trafficData = Traffic(
      ip = ip, source = Source(
        systemName = AppConstants.APP_NAME, version = AppConstants.APP_VERSION
      ), resourceUtilized = ResourceUtilized(
        name = resourceType, value = resourceValue
      )
    )

    return suggestionEngineClient
        .post()
        .uri(trafficEndpoint)
        .body(BodyInserters.fromValue(trafficData))
        .retrieve()
        .bodyToMono(String::class.java)
        .doOnError(DownStreamException::class.java) { error ->
          log.error(
            "Could not send traffic data to SKC Suggestion Engine. Status code: {}, Err: {}",
            error.statusCode,
            error.message
          )
        }
        .doOnError(WebClientRequestException::class.java) { wcError ->
          log.error("General exception occurred while sending traffic data: {}", wcError.message)
        }
        .onErrorResume {
          Mono.just("")
        }
  }
}