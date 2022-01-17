package com.rtomyj.skc.service.banlist

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListDates
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

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
    @Qualifier("ban-list-hibernate") val banListDao: BanListDao
) {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    /**
     * Uses banListDao helper object to retrieve start dates of all ban lists in the database.
     * @return List of BanList objects
     */
    @Throws(YgoException::class)
    fun retrieveBanListStartDates(): BanListDates {
        val banListDates = banListDao.getBanListDates()

        log.info("Retrieving ban list dates.")
        banListDates.setLinks()
        return banListDates
    }
}