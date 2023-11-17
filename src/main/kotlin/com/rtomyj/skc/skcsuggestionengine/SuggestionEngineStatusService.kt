package com.rtomyj.skc.skcsuggestionengine

import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.SuggestionEngineStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException

@Service
class SuggestionEngineStatusService @Autowired constructor(
    @Qualifier("skc-suggestion-engine-web-client") private val suggestionEngineClient: WebClient,
    @Value("\${api.skcSuggestionEngine.endpoints.status}") private val statusEndpoint: String
) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    /**
     * Calls the status endpoint of the Suggestion Engine API to determine the health of the Suggestion Engine.
     */
    fun getStatus(): SuggestionEngineStatus {
        log.info("Retrieving Suggestion Engine status.")

        try {
            return suggestionEngineClient.get().uri(statusEndpoint).retrieve()
                .bodyToMono(SuggestionEngineStatus::class.java).block()!!
        } catch (ex: WebClientResponseException) {
            log.error("Could not send traffic data to SKC Suggestion Engine. Err: {}", ex.toString())
            throw SKCException("Suggestion Engine status check failed.", ErrorType.DS001)
        }
    }
}