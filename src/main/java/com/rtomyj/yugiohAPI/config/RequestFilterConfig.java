package com.rtomyj.yugiohAPI.config;

import com.rtomyj.yugiohAPI.helper.Logging;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
public class RequestFilterConfig {

    @Bean
    public FilterRegistrationBean<MCDFilter> getMCDRequestFilter()
    {
        final FilterRegistrationBean<MCDFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MCDFilter());
        registrationBean.setEnabled(true);
        registrationBean.setOrder(2);

        return registrationBean;
    }


    private static class MCDFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException
        {
            try
            {
                Logging.configureMDC(request);
                filterChain.doFilter(request, response);
            } finally
            {
                MDC.clear();
            }
        }

    }

}
