package com.rtomyj.skc.dao

import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.CardSearchParameters

/**
 * Contract for database operations.
 */
interface CardSearchDao {
  fun searchCard(cardSearchParameters: CardSearchParameters, getBanInfo: Boolean): List<Card>
}