package com.rtomyj.skc.service.banlist

import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListInstance
import com.rtomyj.skc.model.card.Card
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

/**
 * Service class that allows interfacing with the contents of a ban list.
 */
@Service
class BannedCardsService @Autowired constructor(
	@Qualifier("ban-list-jdbc") private val banListDao: BanListDao
	, private val diffService: DiffService
) {
	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}


	/**
	 * Using a date, retrieves the contents of a ban list (as long as there is a ban list effective for given date).
	 * @param banListStartDate The date of the ban list to retrieve from DB. Must follow format: YYYY-DD-MM.
	 * @param saveBandwidth Restriction on what kind of ban list cards to retrieve from DB (forbidden, limited, semi-limited)
	 * @param fetchAllInfo whether all information should be fetched for a particular ban list. In this case, not only are the contents of the ban list returned
	 * , but also information on newly added cards to the ban list and cards no longer on ban list (compared to previous ban list).
	 * @return Object representation of a ban list.
	 * @throws YgoException if there is no ban list for given date.
	 */
	@Throws(YgoException::class)
	fun getBanListByDate(banListStartDate: String, saveBandwidth: Boolean, fetchAllInfo: Boolean): BanListInstance {
		log.info("Retrieving ban list w/ start date: ( {} ).", banListStartDate)

		val banListInstance: BanListInstance = BanListInstance().apply {
			forbidden = banListDao.getBanListByBanStatus(banListStartDate, Dao.Status.FORBIDDEN)
			limited = banListDao.getBanListByBanStatus(banListStartDate, Dao.Status.LIMITED)
			semiLimited = banListDao.getBanListByBanStatus(banListStartDate, Dao.Status.SEMI_LIMITED)
			effectiveDate = banListStartDate
			comparedTo = banListDao.getPreviousBanListDate(banListStartDate)

			numForbidden = forbidden!!.size
			numLimited = limited!!.size
			numSemiLimited = semiLimited!!.size

			validateBanListInstance(this, banListStartDate)
			setLinks()

			if (fetchAllInfo) {
				newContent = diffService.getNewContentForGivenBanList(banListStartDate)
				removedContent = diffService.getRemovedContentForGivenBanList(banListStartDate)
			}

			if (saveBandwidth) {
				Card.trimEffects(this)
			}
		}

		return banListInstance
	}


	@Throws(YgoException::class)
	private fun validateBanListInstance(banListInstance: BanListInstance, banListStartDate: String) {
		if (banListInstance.numForbidden == 0 && banListInstance.numLimited == 0 && banListInstance.numSemiLimited == 0) {
			throw YgoException(
				String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate), ErrorType.D001
			)
		}
	}
}