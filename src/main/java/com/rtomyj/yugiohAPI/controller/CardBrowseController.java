package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.service.CardBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/card/browse", produces = "application/json; charset=UTF-8")
@CrossOrigin("*")
public class CardBrowseController
{

    private HttpServletRequest httpServletRequest;

    private CardBrowseService cardBrowseService;


    public CardBrowseController(@Autowired HttpServletRequest httpServletRequest, @Autowired CardBrowseService cardBrowseService)
    {

        this.httpServletRequest = httpServletRequest;
        this.cardBrowseService = cardBrowseService;

    }


    @GetMapping()
    public String browse()
    {

        return cardBrowseService.getBrowseResults();

    }

}
