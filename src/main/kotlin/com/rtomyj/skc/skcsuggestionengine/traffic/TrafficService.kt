package com.rtomyj.skc.skcsuggestionengine.traffic

import com.rtomyj.skc.model.ResourceUtilized
import com.rtomyj.skc.model.Source
import com.rtomyj.skc.model.Traffic
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class TrafficService @Autowired constructor(
	private val restTemplate: RestTemplate,
	@Value("\${api.skcSuggestionEngine.uri}") val skcSuggestionEngineUri: String,
	@Value("\${api.skcSuggestionEngine.endpoints.traffic}") val trafficEndpoint: String
) {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	fun submitTrafficData(resourceType: TrafficResourceType, resourceValue: String, ip: String) {
		val traffic = Traffic(
			ip = ip,
			source = Source(
				systemName = "skc-api",
				version = AppConstants.APP_VERSION
			),
			resourceUtilized = ResourceUtilized(
				name = resourceType,
				value = resourceValue
			)
		)

		try {
			restTemplate.postForEntity(skcSuggestionEngineUri + trafficEndpoint, traffic, String::class.java)
		} catch (ex: RestClientException) {
			log.error("Could not send traffic data to SKC Suggestion Engine. Err: {}", ex.toString())
		}
	}
}