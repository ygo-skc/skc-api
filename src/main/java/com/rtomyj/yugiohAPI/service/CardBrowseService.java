package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.BrowseResults;
import com.rtomyj.yugiohAPI.model.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class CardBrowseService
{

    private Dao dao;

    private static String levelExpression = "\"level\": \"%s\"";


    public CardBrowseService(@Autowired @Qualifier("jdbc") final Dao dao)
    {

        this.dao = dao;

    }


    public BrowseResults getBrowseResults(final String cardColors, final String monsterLevels)
    {

        Set<String> cardColorsSet = (cardColors.isBlank())? new HashSet<>() : new HashSet<>(Arrays.asList(cardColors.split(",")));
        log.info("Getting browse results for {}", cardColorsSet);

        Set<String> monsterLevelSet = new HashSet<>();
        for(String level: monsterLevels.split(","))
        {
            monsterLevelSet.add(String.format(levelExpression, level));
        }
        log.info(monsterLevelSet.toString());


        final BrowseResults browseResults = dao.getBrowseResults(cardColorsSet, monsterLevelSet);
        Card.setLinks(browseResults.getResults());
        Card.trimEffects(browseResults.getResults());
        return browseResults;

    }

}
