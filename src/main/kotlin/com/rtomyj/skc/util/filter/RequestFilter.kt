package com.rtomyj.skc.util.filter

import com.google.common.base.Strings
import com.google.common.net.HttpHeaders
import com.rtomyj.skc.util.MutableHttpServletRequest
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.logging.Logging
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestFilter : OncePerRequestFilter() {
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
		try {
			val clientIP = if (Strings.isNullOrEmpty(request.getHeader(HttpHeaders.X_FORWARDED_FOR))) request.remoteHost else request.getHeader(HttpHeaders.X_FORWARDED_FOR)

			val mutableRequest = MutableHttpServletRequest(request)
			mutableRequest.putHeader(AppConstants.CLIENT_IP, clientIP)

			Logging.configureMDC(mutableRequest)
			chain.doFilter(mutableRequest, response)
		} finally {
			MDC.clear()
		}
	}
}