package com.rtomyj.skc.find.card.dao

import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.browse.card.model.Card
import com.rtomyj.skc.stats.model.DatabaseStats
import com.rtomyj.skc.stats.model.MonsterTypeStats
import com.rtomyj.skc.status.model.DownstreamStatus

/**
 * Contract for database operations.
 */
interface Dao {
	/**
	 * Retrieve the information about a Card given the ID.
	 * @param cardID The ID of a Yugioh card.
	 * @return The Card requested.
	 */
	@Throws(YgoException::class)
	fun getCardInfo(cardID: String): Card
}