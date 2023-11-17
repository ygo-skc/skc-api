package com.rtomyj.skc.skcsuggestionengine

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
import org.springframework.web.reactive.function.client.WebClientResponseException

@Service
class TrafficService @Autowired constructor(
    @Qualifier("skc-suggestion-engine-web-client") private val suggestionEngineClient: WebClient,
    @Value("\${api.skcSuggestionEngine.endpoints.traffic}") val trafficEndpoint: String
) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    fun submitTrafficData(resourceType: TrafficResourceType, resourceValue: String, ip: String) {
        val traffic = Traffic(
            ip = ip, source = Source(
                systemName = "skc-api", version = AppConstants.APP_VERSION
            ), resourceUtilized = ResourceUtilized(
                name = resourceType, value = resourceValue
            )
        )

        try {
            suggestionEngineClient.post().uri(trafficEndpoint).body(BodyInserters.fromValue(traffic)).retrieve()
                .bodyToMono(String::class.java).block()
        } catch (ex: WebClientResponseException) {
            log.error("Could not send traffic data to SKC Suggestion Engine. Err: {}", ex.toString())
        }
    }
}