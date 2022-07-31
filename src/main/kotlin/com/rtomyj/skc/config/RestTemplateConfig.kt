package com.rtomyj.skc.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestTemplate


@Configuration
class RestTemplateConfig {
	@Bean
	fun skcSuggestionEngineRestTemplate(@Value("\${api.skcSuggestionEngine.key}") apiKey: String): RestTemplate {
		val restTemplate = RestTemplate()
		restTemplate.interceptors.add(SKCSuggestionEngineRestTemplateHeaderInterceptor(apiKey))

		return restTemplate
	}

	internal class SKCSuggestionEngineRestTemplateHeaderInterceptor(
		val apiKey: String
	) : ClientHttpRequestInterceptor {
		override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
			request.headers.set("API-Key", apiKey)
			return execution.execute(request, body)
		}
	}
}