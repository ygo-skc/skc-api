@file:OptIn(DelicateCoroutinesApi::class)

package com.rtomyj.skc.browse.card

import com.rtomyj.skc.browse.card.dao.CardBrowseDao
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.CardBrowseCriteria
import com.rtomyj.skc.model.CardBrowseResults
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.util.enumeration.MonsterAssociationExpression
import com.rtomyj.skc.util.enumeration.MonsterAssociationType
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch

@Service
class CardBrowseService @Autowired constructor(@Qualifier("jdbc") val dao: CardBrowseDao) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)

        fun criteriaStringToSet(criteria: String): Set<String> =
            if (criteria.isBlank()) emptySet() else criteria.split(",").toTypedArray().toSet()

        fun stringSetToIntSet(set: Set<String>): Set<Int> = set
            .map { s: String -> s.toInt() }
            .toSet()
    }


    fun browseResults(criteria: CardBrowseCriteria): CardBrowseResults {
        val watch = StopWatch()
        watch.start()

        val monsterLevelSet =
            transformMonsterAssociationValuesIntoSQL(criteria.levels, MonsterAssociationExpression.LEVEL_EXPRESSION)
        val monsterRankSet =
            transformMonsterAssociationValuesIntoSQL(criteria.ranks, MonsterAssociationExpression.RANK_EXPRESSION)
        val monsterLinkRatingsSet =
            transformMonsterAssociationValuesIntoSQL(
                criteria.linkRatings,
                MonsterAssociationExpression.LINK_RATING_EXPRESSION
            )

        val cardBrowseResults = dao.getBrowseResults(criteria, monsterLevelSet, monsterRankSet, monsterLinkRatingsSet)

        cardBrowseResults.requestedCriteria = criteria

        cardBrowseResults.setLinks()

        watch.stop()
        Card.trimEffects(cardBrowseResults.results)
        MonsterAssociation.transformMonsterLinkRating(cardBrowseResults.results)

        log.debug("Time taken to build card browse results {}ms", watch.totalTimeMillis)
        return cardBrowseResults
    }


    fun browseCriteria(): CardBrowseCriteria {
        var cardBrowseCriteria: CardBrowseCriteria

        runBlocking {
            var levels: Set<Int> = HashSet()
            var ranks: Set<Int> = HashSet()
            var links: Set<Int> = HashSet()

            val deferredMonsterAssociations = GlobalScope.async {
                levels = dao.getMonsterAssociationField(MonsterAssociationType.LEVEL)
                ranks = dao.getMonsterAssociationField(MonsterAssociationType.RANK)
                links = dao.getMonsterAssociationField(MonsterAssociationType.LINK)
            }

            var cardColors: Set<String> = HashSet()
            var monsterAttributes: Set<String> = HashSet()
            var monsterTypes: Set<String> = HashSet()
            var monsterSubTypes: Set<String> = HashSet()

            val deferredCardFeatures = GlobalScope.async {
                cardColors = dao.getCardColors()
                monsterAttributes = dao.getMonsterAttributes()
                monsterTypes = dao.getMonsterTypes()
                monsterSubTypes = dao.getMonsterSubTypes()
            }

            deferredMonsterAssociations.await()
            deferredCardFeatures.await()

            cardBrowseCriteria = CardBrowseCriteria(cardColors, monsterAttributes, monsterTypes, monsterSubTypes, levels, ranks, links)
            cardBrowseCriteria.setLinks()
        }
        return cardBrowseCriteria
    }


    /**
     * Parses a comma delimited string supplied by the user that contains values for a specific monster association key that a user wants to retrieve contents for.
     * User also supplies a pattern defining a key-value pair where the key is a valid monster association and the value is a parametrized String token that will be replaced by
     * String.format()
     * Eg) "level": "%s"
     * This updated patterns will be inserted in the returned Set for use in a SQL query.
     * @param monsterAssociationValues Set containing the browse monster association values wanted by user.
     * @param monsterAssociationAttributeSQLPattern Key-value pair to use in a SQL query, with a parametrized value.
     * @return Set containing `monsterAssociationAttributeSQLPattern`s modified with the unique values from monsterAssociationUserValueString.
     */
    private fun transformMonsterAssociationValuesIntoSQL(
        monsterAssociationValues: Set<Int>,
        monsterAssociationAttributeSQLPattern: MonsterAssociationExpression
    ): Set<String> {
        val monsterAssociationUserValueSet: MutableSet<String> = HashSet()
        if (monsterAssociationValues.isNotEmpty()) {
            for (monsterAssociationUserValueToken in monsterAssociationValues) {
                monsterAssociationUserValueSet.add(
                    String.format(
                        monsterAssociationAttributeSQLPattern.toString(),
                        monsterAssociationUserValueToken
                    )
                )
            }
        }

        log.debug(
            "Transformed monster association values to valid SQL using pattern [ {} ], results={}",
            monsterAssociationAttributeSQLPattern,
            monsterAssociationUserValueSet
        )
        return monsterAssociationUserValueSet
    }
}