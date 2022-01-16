package com.rtomyj.skc.service.stats

import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.stats.DatabaseStats
import com.rtomyj.skc.model.stats.MonsterTypeStats
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class StatsService @Autowired constructor(@Qualifier("jdbc") val dao: Dao) {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	fun getMonsterTypeStats(cardColor: String): MonsterTypeStats {
		val monsterTypeStats = dao.getMonsterTypeStats(cardColor)

		if (monsterTypeStats.monsterTypes.isEmpty()) {
			throw YgoException("Requested monster type not found in DB", ErrorType.D001)    // flow ends here on err
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