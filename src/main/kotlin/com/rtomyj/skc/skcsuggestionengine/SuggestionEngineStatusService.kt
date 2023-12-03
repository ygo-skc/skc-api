package com.rtomyj.skc.skcsuggestionengine

import com.rtomyj.skc.exception.DownStreamException
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.DownstreamStatus
import com.rtomyj.skc.model.SuggestionEngineDownstreamStatus
import com.rtomyj.skc.model.SuggestionEngineStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class SuggestionEngineStatusService @Autowired constructor(
  @Qualifier("skc-suggestion-engine-web-client") private val suggestionEngineClient: WebClient,
  @Value("\${api.skc-suggestion-engine.status-endpoint}") private val statusEndpoint: String
) {
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }


  /**
   * Calls the status endpoint of the Suggestion Engine API to determine the health of the Suggestion Engine.
   */
  fun getStatus(): Mono<DownstreamStatus> {
    return suggestionEngineClient
        .get()
        .uri(statusEndpoint)
        .retrieve()
        .bodyToMono(SuggestionEngineStatus::class.java)
        .doOnError(DownStreamException::class.java) { ex ->
          log.error(
            "Error occurred while fetching SKC Suggestion Engine status. Body: {} HTTP Status Code: {}",
            ex.message,
            ex.statusCode
          )
          throw SKCException("Suggestion Engine status check failed.", ErrorType.DS001)
        }
        .doOnError { ex ->
          log.error(
            "Error occurred while fetching SKC Suggestion Engine status. Body: {}", ex.message
          )
          throw SKCException("Suggestion Engine status check failed.", ErrorType.DS001)
        }
        .doOnRequest {
          log.info("Retrieving Suggestion Engine status.")
        }
        .map { suggestionEngineStatus ->
          // get a list of services used by Suggestion Engine whose status isn't "Up"
          val failedSuggestionEngineDownstreamServices = suggestionEngineStatus.downstream
              .filter { status -> status.status.equals("down", ignoreCase = true) }
              .map(
                SuggestionEngineDownstreamStatus::serviceName
              )

          DownstreamStatus(
            "SKC Suggestion Engine", suggestionEngineStatus.version,
            // either display a happy status, or display the names of all services used by Suggestion Engine that are down.
            if (failedSuggestionEngineDownstreamServices.isEmpty()) "All good 😛" else "The following services are offline: ${failedSuggestionEngineDownstreamServices.joinToString()}"
          )
        }
        .onErrorReturn(SKCException::class.java, DownstreamStatus("SKC Suggestion Engine", "N/A", "down"))
  }
}