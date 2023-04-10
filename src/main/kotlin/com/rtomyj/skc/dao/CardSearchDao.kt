package com.rtomyj.skc.dao

import com.rtomyj.skc.model.Card

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