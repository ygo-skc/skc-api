package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.service.banlist.BanService;

import javax.servlet.http.HttpServletRequest;

public abstract class YgoApiBaseController
{

    /**
     * Base endpoint for the API.
     */
    public static final String BASE_ENDPOINT = "/api/v1";

    /**
     * Object containing info about clients request.
     */
    protected HttpServletRequest request;

}
