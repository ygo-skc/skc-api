package com.rtomyj.skc.dao

import com.rtomyj.skc.enums.MonsterAssociationType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.DownstreamStatus
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.model.card.CardBrowseCriteria
import com.rtomyj.skc.model.card.CardBrowseResults
import com.rtomyj.skc.model.card.MonsterAssociation
import com.rtomyj.skc.model.stats.DatabaseStats
import com.rtomyj.skc.model.stats.MonsterTypeStats

/**
 * Contract for database operations.
 */
interface Dao {
    @Throws(YgoException::class)
    fun dbConnection(): DownstreamStatus

    /**
     * Retrieve the information about a Card given the ID.
     * @param cardID The ID of a Yugioh card.
     * @return The Card requested.
     */
    @Throws(YgoException::class)
    fun getCardInfo(cardID: String): Card

    fun searchForCardWithCriteria(
        cardId: String?,
        cardName: String?,
        cardAttribute: String?,
        cardColor: String?,
        monsterType: String?,
        limit: Int,
        getBanInfo: Boolean
    ): List<Card>

    fun getMonsterTypeStats(cardColor: String): MonsterTypeStats

    fun getDatabaseStats(): DatabaseStats

    fun getBrowseResults(
        criteria: CardBrowseCriteria,
        monsterLevelSet: Set<String>,
        monsterRankSet: Set<String>,
        monsterLinkRatingsSet: Set<String>
    ): CardBrowseResults

    fun getCardColors(): Set<String>

    fun getMonsterAttributes(): Set<String>

    fun getMonsterTypes(): Set<String>

    fun getMonsterSubTypes(): Set<String>

    fun getMonsterAssociationField(monsterAssociationType: MonsterAssociationType): Set<Int>
}