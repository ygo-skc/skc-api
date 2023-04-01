package com.rtomyj.skc.stats.dao

import com.rtomyj.skc.model.DatabaseStats
import com.rtomyj.skc.model.MonsterTypeStats

/**
 * Contract for database operations.
 */
interface StatsDao {
	fun getMonsterTypeStats(cardColor: String): MonsterTypeStats

	fun getDatabaseStats(): DatabaseStats
}