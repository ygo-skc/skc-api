package com.rtomyj.yugiohAPI.helper;

import com.google.common.base.Strings;
import com.sun.tools.jconsole.JConsoleContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
public class Logging
{

    private final static String CLIENT_UUID_NAME = "CLIENT_UUID";
    private final static String CLIENT_USER_AGENT = "User-Agent";
    private final static String CLIENT_IP = "HTTP_X_FORWARDED_FOR";


    public static void configureMDC(final HttpServletRequest httpServletRequest)
    {

        String queryParams = httpServletRequest.getQueryString() == null? "" : "?" + httpServletRequest.getQueryString();

        // proxies and load balancers will forward client IP address in HTTP_X_FORWARDED_FOR header. If header exists, use value. Otherwise, use requests IP
        MDC.put("reqIp", Strings.isNullOrEmpty(httpServletRequest.getHeader(CLIENT_IP))? httpServletRequest.getRemoteHost() : httpServletRequest.getHeader(CLIENT_IP));
        MDC.put("reqUrl", httpServletRequest.getServletPath() + queryParams);
        MDC.put("reqUUID", UUID.randomUUID().toString());
        MDC.put("clientUUID", httpServletRequest.getHeader(CLIENT_UUID_NAME));
        MDC.put("userAgent", httpServletRequest.getHeader(CLIENT_USER_AGENT));

    }

}
