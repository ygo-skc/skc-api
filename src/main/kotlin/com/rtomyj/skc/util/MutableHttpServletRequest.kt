package com.rtomyj.skc.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.util.*

/**
 * Stolen from https://stackoverflow.com/questions/48240291/adding-custom-header-to-request-via-filter
 * This class will allow us to modify the headers of the request. By default, we cannot do this.
 */
class MutableHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
	// holds custom header and value mapping
	private var customHeaders: MutableMap<String, String> = HashMap()

	fun putHeader(name: String, value: String) {
		customHeaders[name] = value
	}

	override fun getHeader(name: String): String? {
		// check the custom headers first
		val headerValue = customHeaders[name]
		return headerValue ?: (request as HttpServletRequest).getHeader(name)
	}

	override fun getHeaderNames(): Enumeration<String> {
		// create a set of the custom header names
		val set: MutableSet<String> = HashSet(customHeaders.keys)

		// now add the headers from the wrapped request object
		val e = (request as HttpServletRequest).headerNames
		while (e.hasMoreElements()) {
			// add the names of the request headers into the list
			val n = e.nextElement()
			set.add(n)
		}

		// create an enumeration from the set and return
		return Collections.enumeration(set)
	}

	override fun getHeaders(name: String): Enumeration<String> {
		val headerValues: MutableSet<String?> = HashSet()
		headerValues.add(getHeader(name))

		val underlyingHeaderValues = (request as HttpServletRequest).getHeaders(name)
		while (underlyingHeaderValues.hasMoreElements()) {
			headerValues.add(underlyingHeaderValues.nextElement())
		}

		return Collections.enumeration(headerValues)
	}
}