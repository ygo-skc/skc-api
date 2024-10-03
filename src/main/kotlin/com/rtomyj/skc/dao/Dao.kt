package com.rtomyj.skc.dao

import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.Card

/**
 * Contract for database operations.
 */
interface Dao {
  /**
   * Retrieve the information about a Card given the ID.
   * @param cardID The ID of a Yu-Gi-Oh! card.
   * @return The Card requested.
   */
  @Throws(SKCException::class)
  fun getCardInfo(cardID: String): Card
}