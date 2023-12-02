package com.rtomyj.skc.skcsuggestionengine

import com.rtomyj.skc.exception.DownStreamException
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.SuggestionEngineStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SuggestionEngineStatusService @Autowired constructor(
  @Qualifier("skc-suggestion-engine-web-client") private val suggestionEngineClient: WebClient,
  @Value("\${api.skc-suggestion-engine.status-endpoint}") private val statusEndpoint: String
) {
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
    private val statusCheckFailedException = SKCException("Suggestion Engine status check failed.", ErrorType.DS001)
  }


  /**
   * Calls the status endpoint of the Suggestion Engine API to determine the health of the Suggestion Engine.
   */
  fun getStatus(): SuggestionEngineStatus {
    log.info("Retrieving Suggestion Engine status.")

    return suggestionEngineClient
        .get()
        .uri(statusEndpoint)
        .retrieve()
        .bodyToMono(SuggestionEngineStatus::class.java)
        .onErrorMap(DownStreamException::class.java) { ex ->
          log.error(
            "Error occurred while fetching SKC Suggestion Engine status. Body: {} HTTP Status Code: {}",
            ex.message,
            ex.statusCode
          )
          throw statusCheckFailedException
        }
        .onErrorMap { ex ->
          log.error(
            "Error occurred while fetching SKC Suggestion Engine status. Body: {}", ex.message
          )
          throw statusCheckFailedException
        }
        .blockOptional()
        .get()
  }
}