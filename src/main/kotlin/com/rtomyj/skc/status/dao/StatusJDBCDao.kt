package com.rtomyj.skc.status.dao

import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.status.model.DownstreamStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository
@Qualifier("jdbc")
class StatusJDBCDao @Autowired constructor(
	val jdbcNamedTemplate: NamedParameterJdbcTemplate
) : StatusDao {
	companion object {
		private const val VERSION_QUERY = "select version() as version"

		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	@Throws(YgoException::class)
	override fun dbConnection(): DownstreamStatus {
		val dbName = "SKC DB"
		var versionMajor = "---"
		var status = "down"
		var downstreamStatus: DownstreamStatus

		try {
			val version =
				jdbcNamedTemplate.queryForObject(VERSION_QUERY, MapSqlParameterSource(), String::class.java)
			status = "up"
			assert(version != null)

			val versionStringTokens = version!!.split("-").toTypedArray()
			versionMajor = if (versionStringTokens.isNotEmpty()) versionStringTokens[0].split(".")[0] else "---"
			downstreamStatus = DownstreamStatus(dbName, versionMajor, status)
		} catch (e: DataAccessException) {
			log.error("Could not get version of the DB. Exception occurred: {}", e.toString())
			downstreamStatus = DownstreamStatus(dbName, versionMajor, status)
		} catch (e: AssertionError) {
			log.error("Could not get version of the DB. Exception occurred: {}", e.toString())
			downstreamStatus = DownstreamStatus(dbName, versionMajor, status)
		}
		return downstreamStatus
	}
}