package com.rtomyj.skc.util.filter

import com.google.common.base.Strings
import com.google.common.net.HttpHeaders
import com.rtomyj.skc.util.MutableHttpServletRequest
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.logging.Logging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class RequestFilter : OncePerRequestFilter() {
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
		try {
			val clientIP = if (Strings.isNullOrEmpty(request.getHeader(HttpHeaders.X_FORWARDED_FOR))) request.remoteHost else request.getHeader(HttpHeaders.X_FORWARDED_FOR)

			val mutableRequest = MutableHttpServletRequest(request)
			mutableRequest.putHeader(AppConstants.CLIENT_IP, clientIP)

			Logging.configureMDC(mutableRequest)
			chain.doFilter(request, response)
		} finally {
			MDC.clear()
		}
	}
}