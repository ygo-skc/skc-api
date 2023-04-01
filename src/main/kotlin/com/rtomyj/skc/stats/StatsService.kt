package com.rtomyj.skc.stats

import com.rtomyj.skc.dao.StatsDao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.DatabaseStats
import com.rtomyj.skc.model.MonsterTypeStats
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class StatsService @Autowired constructor(
	@Qualifier("jdbc") val dao: StatsDao
) {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	fun getMonsterTypeStats(cardColor: String): MonsterTypeStats {
		val monsterTypeStats = dao.getMonsterTypeStats(cardColor)

		if (monsterTypeStats.monsterTypes.isEmpty()) {
			throw SKCException("Requested monster type not found in DB", ErrorType.DB001)    // flow ends here on err
		}

		monsterTypeStats.setLinks()
		log.info("Successfully retrieved stats for monster typing's: {}", monsterTypeStats.toString())
		return monsterTypeStats
	}


	fun databaseStats(): DatabaseStats {
		val databaseStats = dao.getDatabaseStats()

		databaseStats.setLinks()
		log.info("Successfully retrieved database stats: {}", databaseStats.toString())
		return databaseStats
	}
}