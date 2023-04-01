package com.rtomyj.skc.status.dao

import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.DownstreamStatus

/**
 * Contract for database operations.
 */
interface StatusDao {
	@Throws(SKCException::class)
	fun dbConnection(): DownstreamStatus
}