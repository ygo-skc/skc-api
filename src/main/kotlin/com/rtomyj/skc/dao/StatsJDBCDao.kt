package com.rtomyj.skc.dao

import com.rtomyj.skc.model.DatabaseStats
import com.rtomyj.skc.model.MonsterTypeStats
import com.rtomyj.skc.util.constant.DBQueryConstants
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository
@Qualifier("jdbc")
class StatsJDBCDao @Autowired constructor(
	val jdbcNamedTemplate: NamedParameterJdbcTemplate,
) : StatsDao {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	override fun getMonsterTypeStats(cardColor: String): MonsterTypeStats {
		val query =
			"SELECT monster_type, count(*) AS 'Total' FROM card_info WHERE monster_type IS NOT NULL AND card_color = :cardColor GROUP BY monster_type ORDER BY monster_type"
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("cardColor", cardColor)
		val monsterType = MonsterTypeStats(cardColor, HashMap())
		jdbcNamedTemplate.query<Any?>(query, sqlParams) { row: ResultSet, _: Int ->
			monsterType.monsterTypes[row.getString(1)] = row.getInt(2)
			null
		}
		return monsterType
	}

	override fun getDatabaseStats(): DatabaseStats {
		return jdbcNamedTemplate
			.queryForObject(
				DBQueryConstants.GET_DATABASE_TOTALS, MapSqlParameterSource()
			) { row: ResultSet, _: Int ->
				DatabaseStats(
					row.getInt(1),
					row.getInt(2),
					row.getInt(3),
					row.getInt(4)
				)
			}!!
	}
}