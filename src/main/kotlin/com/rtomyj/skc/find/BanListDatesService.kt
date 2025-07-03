package com.rtomyj.skc.find

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListDates
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.format.DateTimeFormatter

/**
 * Service used to interface with database for basic operations regarding ban lists.
 */
@Service
class BanListDatesService
/**
 * Create object instance.
 * @param banListDao object used to interface with DB.
 */ @Autowired constructor(
  /**
   * Object used to interface with DB.
   */
  @param:Qualifier("ban-list-hibernate") val banListDao: BanListDao,
  @param:Qualifier("dbDateTimeFormatter") val dbDateFormatter: DateTimeFormatter
) {
  /**
   * Uses banListDao helper object to retrieve start dates of all ban lists in the database.
   * @return List of BanList objects
   */
  @Throws(SKCException::class)
  fun retrieveBanListStartDates(format: String): Mono<BanListDates> = Flux
      .fromIterable(banListDao.getBanListDates(format).dates)
      .collectList()
      .map { dates ->
        BanListDates(dates.filterNotNull())
      }
}