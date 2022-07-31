package com.rtomyj.skc.search.card.dao

import com.rtomyj.skc.browse.card.model.Card

/**
 * Contract for database operations.
 */
interface CardSearchDao {
	fun searchForCardWithCriteria(
		cardId: String?,
		cardName: String?,
		cardAttribute: String?,
		cardColor: String?,
		monsterType: String?,
		limit: Int,
		getBanInfo: Boolean
	): List<Card>
}