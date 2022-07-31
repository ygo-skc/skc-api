package com.rtomyj.skc.skcsuggestionengine.traffic

import com.rtomyj.skc.skcsuggestionengine.traffic.model.ResourceUtilized
import com.rtomyj.skc.skcsuggestionengine.traffic.model.Source
import com.rtomyj.skc.skcsuggestionengine.traffic.model.Traffic
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class TrafficService @Autowired constructor(
	private val restTemplate: RestTemplate
) {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	fun submitTrafficData(resourceType: TrafficResourceType, resourceValue: String) {
		val trafficInstane = Traffic(
			ip = "24.15.81.106",
			source = Source(
				systemName = "skc-api",
				version = "1.0.1"
			),
			resourceUtilized = ResourceUtilized(
				name = resourceType,
				value = resourceValue
			)
		)

		try {
			restTemplate.postForEntity("", trafficInstane, String::class.java)
		} catch (ex: RestClientException) {
			log.error("Could not send traffic data to SKC Suggestion Engine. Err: {}", ex)
		}
	}
}