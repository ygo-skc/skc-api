package com.rtomyj.yugiohAPI.service.card;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import com.rtomyj.yugiohAPI.model.card.CardBrowseResults;
import com.rtomyj.yugiohAPI.model.card.Card;
import com.rtomyj.yugiohAPI.model.card.CardBrowseCriteria;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

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


    public CardBrowseResults getBrowseResults(final String cardColors, final String attributes, final String monsterLevels)
    {

        final Set<String> cardColorsSet = (cardColors.isBlank())? new HashSet<>() : new HashSet<>(Arrays.asList(cardColors.split(",")));
        final Set<String> attributeSet = (attributes.isBlank())? new HashSet<>() : new HashSet<>(Arrays.asList(attributes.split(",")));

        final Set<String> monsterLevelSet = new HashSet<>();
        if (!monsterLevels.isEmpty())
        {
            for(String level: monsterLevels.split(","))
            {
                monsterLevelSet.add(String.format(levelExpression, level));
            }
        }


        final CardBrowseResults cardBrowseResults = dao.getBrowseResults(cardColorsSet, attributeSet, monsterLevelSet);
        cardBrowseResults.setRequestedCriteria(
                CardBrowseCriteria
                        .builder()
                        .cardColors(cardColorsSet)
                        .attributes(attributeSet)
                        .build()
        );
        cardBrowseResults.setNumResults(cardBrowseResults.getResults().size());

        cardBrowseResults.setLinks();
        Card.trimEffects(cardBrowseResults.getResults());
        return cardBrowseResults;

    }


    @SneakyThrows
    public CardBrowseCriteria getBrowseCriteria()
    {

        final Future<Set<String>> cardColors = dao.getCardColors();
        final Future<Set<String>> monsterAttributes = dao.getMonsterAttributes();
//        Future<Set<Integer>> levels =  dao.getLevels();
//        Future<Set<Integer>> ranks =  dao.getRanks();
//        Future<Set<Integer>> linkRatings =  dao.getLinkRatings();

        return CardBrowseCriteria.
                builder()
                .cardColors(cardColors.get())
                .attributes(monsterAttributes.get())
//                .levels(levels.get())
//                .ranks(ranks.get())
//                .linkRatings(linkRatings.get())
                .build();

    }

}
