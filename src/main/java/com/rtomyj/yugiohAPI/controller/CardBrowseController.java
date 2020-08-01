package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.helper.Logging;
import com.rtomyj.yugiohAPI.model.BrowseResults;
import com.rtomyj.yugiohAPI.service.CardBrowseService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/card/browse", produces = "application/json; charset=UTF-8")
@CrossOrigin("*")
public class CardBrowseController
{

    private static final String endpoint = "/api/v1/card/browse";

    private final HttpServletRequest httpServletRequest;

    private final CardBrowseService cardBrowseService;


    public CardBrowseController(@Autowired final HttpServletRequest httpServletRequest, @Autowired final CardBrowseService cardBrowseService)
    {

        this.httpServletRequest = httpServletRequest;
        this.cardBrowseService = cardBrowseService;

    }


    @GetMapping()
    public BrowseResults browse(@RequestParam(value = "cardColors", defaultValue = "") final String cardColors, @RequestParam(value = "levels", defaultValue = "") final String monsterLevels)
    {

        Logging.configureMDC(httpServletRequest, endpoint);
        final BrowseResults browseResults = cardBrowseService.getBrowseResults(cardColors, monsterLevels);
        MDC.clear();

        return browseResults;

    }

}
