package com.rtomyj.skc.filter

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ResponseHeaderFilter : OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        response.setHeader("Cache-Control", "max-age=300")
        response.setHeader("Connection", "Keep-Alive")
        response.setHeader("Keep-Alive", "timeout=60, max=600")
        chain.doFilter(request, response)
    }
}