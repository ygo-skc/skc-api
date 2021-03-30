package com.rtomyj.yugiohAPI.helper;

import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import com.sun.tools.jconsole.JConsoleContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Contains common logging utilities.
 */
@Slf4j
public class Logging
{

    private final static String CLIENT_ID_NAME = "CLIENT_ID";


    /**
     * Configures the global MDC object for all requests. MDC is used to hold useful info that will later be used in logs to better track requests.
     * @param httpServletRequest Contains useful information about new requests to the server from the client that will be used to access IP address and header information.
     */
    public static void configureMDC(final HttpServletRequest httpServletRequest)
    {

        String queryParams = httpServletRequest.getQueryString() == null? "" : "?" + httpServletRequest.getQueryString();

        // proxies and load balancers will forward client IP address in HTTP_X_FORWARDED_FOR header. If header exists, use value. Otherwise, use requests IP
        MDC.put("reqIp", Strings.isNullOrEmpty(httpServletRequest.getHeader(HttpHeaders.X_FORWARDED_FOR))? httpServletRequest.getRemoteHost()
                : httpServletRequest.getHeader(HttpHeaders.X_FORWARDED_FOR));

        MDC.put("reqPath", httpServletRequest.getServletPath() + queryParams);
        MDC.put("reqUUID", UUID.randomUUID().toString());
        MDC.put("clientID", httpServletRequest.getHeader(CLIENT_ID_NAME));
        MDC.put("userAgent", httpServletRequest.getHeader(HttpHeaders.USER_AGENT));

    }

}
