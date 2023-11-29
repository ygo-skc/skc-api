package com.rtomyj.skc.config.filter

import com.google.common.base.Strings
import com.google.common.net.HttpHeaders.USER_AGENT
import com.rtomyj.skc.util.constant.AppConstants.CLIENT_IP_MDC
import org.slf4j.MDC
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*

@Component
class RequestFilter : WebFilter {
  companion object {
    private const val X_FORWARDED_FOR = "X-Forwarded-For"
    private const val CLIENT_ID_NAME = "CLIENT_ID"

    /**
     * Configures the global MDC object for all requests. MDC is used to hold useful info that will later be used in logs to better track requests.
     * @param request Contains useful information about new requests to the server from the client that will be used to access IP address and header information.
     */
    fun configureMDC(request: ServerHttpRequest) {
      val headers = request.headers
      val clientIP =
        if (StringUtils.hasLength(headers.getFirst(X_FORWARDED_FOR))) headers.getFirst(X_FORWARDED_FOR) else request.remoteAddress?.address?.hostAddress
          ?: ""
      val queryParams = if (Strings.isNullOrEmpty(request.uri.query)) "" else "?" + request.uri.query

      // proxies and load balancers will forward client IP address in HTTP_X_FORWARDED_FOR header. If header exists, use value. Otherwise, use requests IP
      MDC.put(
        CLIENT_IP_MDC,
        clientIP
            .replace("[", "")
            .replace("]", "")
      )
      MDC.put("reqPath", request.uri.path + queryParams)
      MDC.put(
        "reqUUID",
        UUID
            .randomUUID()
            .toString()
      )
      MDC.put("clientID", headers.getFirst(CLIENT_ID_NAME))
      MDC.put("userAgent", headers.getFirst(USER_AGENT))
    }
  }

  override fun filter(
    serverWebExchange: ServerWebExchange, chain: WebFilterChain
  ): Mono<Void> = chain
      .filter(serverWebExchange)
      .doOnSubscribe {
        configureMDC(serverWebExchange.request)
      }
      .doFinally {
        MDC.clear()
      }
}