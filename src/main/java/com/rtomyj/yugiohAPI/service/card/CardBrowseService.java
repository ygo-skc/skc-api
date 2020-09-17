package com.rtomyj.yugiohAPI.service.card;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import com.rtomyj.yugiohAPI.model.card.CardBrowseResults;
import com.rtomyj.yugiohAPI.model.card.Card;
import com.rtomyj.yugiohAPI.model.card.CardBrowseCriteria;
import com.rtomyj.yugiohAPI.model.card.MonsterAssociation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CardBrowseService
{

    private Dao dao;

    private static final String levelExpression = "\"level\": \"%s\"";

    private static CardBrowseCriteria cachedCardBrowseCriteria;


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


    public CardBrowseCriteria getBrowseCriteria()
    {

        if (cachedCardBrowseCriteria == null)
        {
            synchronized(this)
            {
                cachedCardBrowseCriteria = CardBrowseCriteria.
                        builder()
                        .cardColors(dao.getCardColors())
                        .attributes(dao.getMonsterAttributes())
                        .levels(uniqueMonsterAssociationField("level").stream().map(MonsterAssociation::getLevel).collect(Collectors.toSet()))
                        .ranks(uniqueMonsterAssociationField("rank").stream().map(MonsterAssociation::getRank).collect(Collectors.toSet()))
                        .linkRatings(uniqueMonsterAssociationField("linkRating").stream().map(MonsterAssociation::getLinkRating).collect(Collectors.toSet()))
                        .build();

                cachedCardBrowseCriteria.setLinks();
            }
        }
        return cachedCardBrowseCriteria;

    }


    private Set<MonsterAssociation> uniqueMonsterAssociationField(final String monsterAssociationField)
    {

        return dao.getMonsterAssociationField(monsterAssociationField);

    }

}
