package com.rtomyj.skc.skcsuggestionengine.status

import com.rtomyj.skc.skcsuggestionengine.status.model.SuggestionEngineStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class SuggestionEngineStatusService @Autowired constructor(
	private val restTemplate: RestTemplate,
	@Value("\${api.skcSuggestionEngine.uri}") private val skcSuggestionEngineUri: String,
	@Value("\${api.skcSuggestionEngine.endpoints.status}") private val statusEndpoint: String
) {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	/**
	 * Calls the status endpoint of the Suggestion Engine API to determine the health of the Suggestion Engine.
	 */
	fun getStatus(): SuggestionEngineStatus{
		log.info("Retrieving Suggestion Engine status.")
		var suggestionEngineStatus = SuggestionEngineStatus("", emptyList())

		try {
			suggestionEngineStatus = restTemplate.getForEntity(skcSuggestionEngineUri + statusEndpoint, SuggestionEngineStatus::class.java).body!!
		} catch (ex: RestClientException) {
			log.error("Could not send traffic data to SKC Suggestion Engine. Err: {}", ex.toString())
		}

		return suggestionEngineStatus
	}
}