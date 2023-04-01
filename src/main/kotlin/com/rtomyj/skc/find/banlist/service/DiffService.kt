package com.rtomyj.skc.find.banlist.service

import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.find.banlist.dao.BanListDao
import com.rtomyj.skc.model.BanListNewContent
import com.rtomyj.skc.model.BanListRemovedContent
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.util.constant.ErrConstants
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class DiffService @Autowired constructor(
	@Qualifier("ban-list-jdbc") val banListDao: BanListDao
) {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	fun newContentNormalFormat(banListStartDate: String, format: String): BanListNewContent {
		val forbidden = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.FORBIDDEN, format)
		val limited = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.LIMITED, format)
		val semiLimited = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.SEMI_LIMITED, format)

		forbidden.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
		limited.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
		semiLimited.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }

		return BanListNewContent(
			banListStartDate,
			getPreviousBanListDate(banListStartDate, format),
			forbidden,
			limited,
			semiLimited,
			emptyList(),
			emptyList(),
			emptyList()
		)
	}

	fun newContentDuelLinksFormat(banListStartDate: String, format: String): BanListNewContent {
		val forbidden = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.FORBIDDEN, format)
		val limitedOne = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.LIMITED_ONE, format)
		val limitedTwo = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.LIMITED_TWO, format)
		val limitedThree = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.LIMITED_THREE, format)

		forbidden.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
		limitedOne.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
		limitedTwo.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
		limitedThree.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }

		return BanListNewContent(
			banListStartDate,
			getPreviousBanListDate(banListStartDate, format),
			forbidden,
			emptyList(),
			emptyList(),
			limitedOne,
			limitedTwo,
			limitedThree
		)
	}


	@Throws(SKCException::class)
	fun getNewContentForGivenBanList(banListStartDate: String, format: String): BanListNewContent {
		log.info("Fetching new content for ban list from DB w/ start date: ({}).", banListStartDate)

		if (!banListDao.isValidBanList(banListStartDate)) throw SKCException(
			String.format(
				ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE,
				banListStartDate
			), ErrorType.DB001
		)


		// builds meta data object for new cards request
		val newCardsMeta = if (format == "DL") newContentDuelLinksFormat(banListStartDate, format) else newContentNormalFormat(banListStartDate, format)

		newCardsMeta.setLinks()
		return newCardsMeta
	}

	@Throws(SKCException::class)
	fun getRemovedContentForGivenBanList(banListStartDate: String?, format: String): BanListRemovedContent {
		log.info("Fetching removed content for ban list from DB w/ start date: ( {} ).", banListStartDate)
		if (!banListDao.isValidBanList(banListStartDate!!)) throw SKCException(
			String.format(
				ErrConstants.NO_REMOVED_BAN_LIST_CONTENT_FOR_START_DATE,
				banListStartDate
			), ErrorType.DB001
		)
		val removedCards = banListDao.getRemovedContentOfBanList(
			banListStartDate, format
		)

		removedCards.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }

		// builds meta data object for removed cards request
		val removedCardsMeta = BanListRemovedContent(
			banListStartDate,
			getPreviousBanListDate(banListStartDate, format),
			removedCards
		)

		removedCardsMeta.setLinks()
		return removedCardsMeta
	}

	private fun getPreviousBanListDate(banList: String?, format: String): String {
		return banListDao.getPreviousBanListDate(banList!!, format)
	}
}