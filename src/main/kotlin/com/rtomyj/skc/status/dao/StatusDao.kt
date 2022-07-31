package com.rtomyj.skc.status.dao

import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.browse.card.model.Card
import com.rtomyj.skc.stats.model.DatabaseStats
import com.rtomyj.skc.stats.model.MonsterTypeStats
import com.rtomyj.skc.status.model.DownstreamStatus

/**
 * Contract for database operations.
 */
interface StatusDao {
	@Throws(YgoException::class)
	fun dbConnection(): DownstreamStatus
}