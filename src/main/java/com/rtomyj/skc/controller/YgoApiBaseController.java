package com.rtomyj.skc.controller;

import org.springframework.beans.factory.annotation.Value;

public abstract class YgoApiBaseController
{
    /**
     * Base endpoint for the API.
     */
    @Value("${server.servlet.contextPath}")
    public String baseEndpoint;
}
