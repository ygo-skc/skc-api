package com.rtomyj.skc.controller

import org.springframework.beans.factory.annotation.Value

abstract class YgoApiBaseController {
	/**
	 * Base endpoint for the API.
	 */
	@Value("\${server.servlet.contextPath}")
	var baseEndpoint: String? = null
}