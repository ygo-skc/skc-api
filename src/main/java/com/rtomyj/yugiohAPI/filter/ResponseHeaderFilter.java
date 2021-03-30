package com.rtomyj.yugiohAPI.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ResponseHeaderFilter extends OncePerRequestFilter
{

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
		throws IOException, ServletException
	{

		response.setHeader("Cache-Control", "max-age=300");
		response.setHeader("Connection", "Keep-Alive");
		response.setHeader("Keep-Alive", "timeout=60, max=100");
		chain.doFilter(request, response);

	}

}