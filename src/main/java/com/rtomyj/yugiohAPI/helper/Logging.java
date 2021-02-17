package com.rtomyj.yugiohAPI.helper;

import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class Logging
{

    private final static String CLIENT_UUID_NAME = "CLIENT_UUID";
    private final static String CLIENT_USER_AGENT = "User-Agent";


    public static void configureMDC(final HttpServletRequest httpServletRequest)
    {

        String queryParams = httpServletRequest.getQueryString() == null? "" : "?" + httpServletRequest.getQueryString();

        MDC.put("reqIp", httpServletRequest.getRemoteHost());
        MDC.put("reqUrl", httpServletRequest.getServletPath() +queryParams);
        MDC.put("reqUUID", UUID.randomUUID().toString());
        MDC.put("clientUUID", httpServletRequest.getHeader(CLIENT_UUID_NAME));
        MDC.put("userAgent", httpServletRequest.getHeader(CLIENT_USER_AGENT));

    }

}
