package com.rtomyj.skc.browse.card.dao

import com.rtomyj.skc.enums.MonsterAssociationType
import com.rtomyj.skc.browse.card.model.CardBrowseCriteria
import com.rtomyj.skc.browse.card.model.CardBrowseResults

/**
 * Contract for database operations.
 */
interface CardBrowseDao {
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