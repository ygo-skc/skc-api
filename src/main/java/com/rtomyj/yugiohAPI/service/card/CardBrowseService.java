package com.rtomyj.yugiohAPI.service.card;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.enumeration.browse.MonsterAssociationExpression;
import com.rtomyj.yugiohAPI.model.card.CardBrowseResults;
import com.rtomyj.yugiohAPI.model.card.Card;
import com.rtomyj.yugiohAPI.model.card.CardBrowseCriteria;
import com.rtomyj.yugiohAPI.model.card.MonsterAssociation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CardBrowseService
{

    private final Dao dao;


    public CardBrowseService(@Autowired @Qualifier("jdbc") final Dao dao)
    {

        this.dao = dao;

    }


    public CardBrowseResults getBrowseResults(final String cardColors, final String attributes, final String monsterTypes, final String monsterLevels
            , final String monsterRanks, final String monsterLinkRatings)
    {

        final Set<String> cardColorsSet = (cardColors.isBlank())? new HashSet<>() : new HashSet<>(Arrays.asList(cardColors.split(",")));
        final Set<String> attributeSet = (attributes.isBlank())? new HashSet<>() : new HashSet<>(Arrays.asList(attributes.split(",")));
        final Set<String> monsterTypeSet = (monsterTypes.isBlank())? new HashSet<>() : new HashSet<>(Arrays.asList(monsterTypes.split(",")));

        final Set<String> monsterLevelSet = transformMonsterAssociationValuesIntoSQL(monsterLevels, MonsterAssociationExpression.LEVEL_EXPRESSION);
        final Set<String> monsterRankSet = transformMonsterAssociationValuesIntoSQL(monsterRanks, MonsterAssociationExpression.RANK_EXPRESSION);
        final Set<String> monsterLinkRatingsSet = transformMonsterAssociationValuesIntoSQL(monsterLinkRatings, MonsterAssociationExpression.LINK_RATING_EXPRESSION);


        final CardBrowseResults cardBrowseResults = dao.getBrowseResults(cardColorsSet, attributeSet, monsterTypeSet, monsterLevelSet
                , monsterRankSet, monsterLinkRatingsSet);
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

        final CardBrowseCriteria cardBrowseCriteria  = CardBrowseCriteria.
                builder()
                .cardColors(dao.getCardColors())
                .attributes(dao.getMonsterAttributes())
                .monsterTypes(dao.getMonsterTypes())
                .levels(uniqueMonsterAssociationField("level").stream().map(MonsterAssociation::getLevel).collect(Collectors.toSet()))
                .ranks(uniqueMonsterAssociationField("rank").stream().map(MonsterAssociation::getRank).collect(Collectors.toSet()))
                .linkRatings(uniqueMonsterAssociationField("linkRating").stream().map(MonsterAssociation::getLinkRating).collect(Collectors.toSet()))
                .build();

        cardBrowseCriteria.setLinks();
        return cardBrowseCriteria;

    }


    private Set<MonsterAssociation> uniqueMonsterAssociationField(final String monsterAssociationField)
    {

        return dao.getMonsterAssociationField(monsterAssociationField);

    }


    /**
     * Parses a comma delimited string supplied by the user that contains values for a specific monster association key that a user wants to retrieve contents for.
     * User also supplies a pattern defining a key-value pair where the key is a valid monster association and the value is a parametrized String token that will be replaced by
     *  String.format()
     *      Eg) "level": "%s"
     * This updated patterns will be inserted in the returned Set for use in a SQL query.
     * @param monsterAssociationUserValueString The comma delimited string containing the browse monster association values wanted by user.
     * @param monsterAssociationAttributeSQLPattern Key-value pair to use in a SQL query, with a parametrized value.
     * @return Set containing {@code monsterAssociationAttributeSQLPattern}s modified with the unique values from monsterAssociationUserValueString.
     */
    private Set<String> transformMonsterAssociationValuesIntoSQL(final String monsterAssociationUserValueString, final MonsterAssociationExpression monsterAssociationAttributeSQLPattern)
    {

        final Set<String> monsterAssociationUserValueSet = new HashSet<>();
        if (!monsterAssociationUserValueString.isEmpty())
        {
            for(String monsterAssociationUserValueToken: monsterAssociationUserValueString.split(","))
            {
                monsterAssociationUserValueSet.add(String.format(monsterAssociationAttributeSQLPattern.toString(), monsterAssociationUserValueToken));
            }
        }

        log.debug("Transformed monster association values to valid SQL using pattern [ {} ], results={}", monsterAssociationAttributeSQLPattern, monsterAssociationUserValueSet);
        return monsterAssociationUserValueSet;

    }

}
