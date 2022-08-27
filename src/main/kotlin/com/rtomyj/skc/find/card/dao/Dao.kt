package com.rtomyj.skc.find.card.dao

import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.browse.card.model.Card

/**
 * Contract for database operations.
 */
interface Dao {
	/**
	 * Retrieve the information about a Card given the ID.
	 * @param cardID The ID of a Yugioh card.
	 * @return The Card requested.
	 */
	@Throws(SKCException::class)
	fun getCardInfo(cardID: String): Card
}