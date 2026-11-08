package com.rtomyj.skc.config.filter

import com.rtomyj.skc.util.constant.AppConstants.CLIENT_IP_MDC
import io.micrometer.context.integration.Slf4jThreadLocalAccessor
import org.springframework.http.HttpHeaders.USER_AGENT
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class RequestFilter : WebFilter {
    companion object {
        private const val X_FORWARDED_FOR = "X-Forwarded-For"
        private const val CLIENT_ID_NAME = "CLIENT_ID"

        /**
         * Builds MDC values for a request. MDC is used to hold useful info that will later be used in logs to better track requests.
         * @param request Contains useful information about new requests to the server from the client that will be used to access IP address and header information.
         */
        fun mdcValues(request: ServerHttpRequest): Map<String, String?> {
            val headers = request.headers
            val clientIP =
                if (headers
                        .getFirst(X_FORWARDED_FOR)
                        .isNullOrBlank()
                ) {
                    request.remoteAddress?.address?.hostAddress ?: ""
                } else {
                    headers.getFirst(X_FORWARDED_FOR)
                }
            val queryParams = if (request.uri.query.isNullOrBlank()) "" else "?" + request.uri.query

            // proxies and load balancers will forward client IP address in HTTP_X_FORWARDED_FOR header. If header exists, use value. Otherwise, use requests IP
            return mapOf(
                CLIENT_IP_MDC to
                    clientIP
                        ?.replace("[", "")
                        ?.replace("]", ""),
                "reqPath" to request.uri.path + queryParams,
                "reqUUID" to
                    UUID
                        .randomUUID()
                        .toString(),
                "clientID" to headers.getFirst(CLIENT_ID_NAME),
                "userAgent" to headers.getFirst(USER_AGENT),
            )
        }
    }

    // MDC is restored from this context entry on every thread hop, see ContextPropagationConfig
    override fun filter(
        serverWebExchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> =
        chain
            .filter(serverWebExchange)
            .contextWrite { it.put(Slf4jThreadLocalAccessor.KEY, mdcValues(serverWebExchange.request)) }
}
