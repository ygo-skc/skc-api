package com.rtomyj.yugiohAPI.helper;

import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

public class Logging
{

    public static void configureMDC(final HttpServletRequest httpServletRequest, final String endpoint)
    {

        MDC.put("reqIp", httpServletRequest.getRemoteHost());
        MDC.put("reqRes", endpoint);

    }

}
