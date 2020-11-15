package com.rtomyj.yugiohAPI.controller;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

public abstract class YgoApiBaseController
{

    /**
     * Base endpoint for the API.
     */
    public static String BASE_ENDPOINT;


    @Value("${server.servlet.contextPath}")
    public void setBaseEndpoint(String baseEndpoint)
    {
        BASE_ENDPOINT = baseEndpoint;
    }

}
