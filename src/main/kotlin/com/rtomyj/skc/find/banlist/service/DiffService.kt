package com.rtomyj.skc.find.banlist.service

import com.rtomyj.skc.util.constant.ErrConstants
import com.rtomyj.skc.find.banlist.dao.BanListDao
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.browse.card.model.MonsterAssociation
import com.rtomyj.skc.find.banlist.model.BanListNewContent
import com.rtomyj.skc.find.banlist.model.BanListRemovedContent
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
    
    
    @Throws(SKCException::class)
    fun getNewContentForGivenBanList(banListStartDate: String): BanListNewContent {
        log.info("Fetching new content for ban list from DB w/ start date: ({}).", banListStartDate)

        if (!banListDao.isValidBanList(banListStartDate)) throw SKCException(
            String.format(
                ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE,
                banListStartDate
            ), ErrorType.DB001
        )


        // builds meta data object for new cards request
        val forbidden = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.FORBIDDEN)
        val limited = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.LIMITED)
        val semiLimited = banListDao.getNewContentOfBanList(banListStartDate, BanListCardStatus.SEMI_LIMITED)

        forbidden.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
        limited.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
        semiLimited.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }

        val newCardsMeta = BanListNewContent(
            banListStartDate,
            getPreviousBanListDate(banListStartDate),
            forbidden,
            limited,
            semiLimited,
        )

        newCardsMeta.setLinks()
        return newCardsMeta
    }

    @Throws(SKCException::class)
    fun getRemovedContentForGivenBanList(banListStartDate: String?): BanListRemovedContent {
        log.info("Fetching removed content for ban list from DB w/ start date: ( {} ).", banListStartDate)
        if (!banListDao.isValidBanList(banListStartDate!!)) throw SKCException(
            String.format(
                ErrConstants.NO_REMOVED_BAN_LIST_CONTENT_FOR_START_DATE,
                banListStartDate
            ), ErrorType.DB001
        )
        val removedCards = banListDao.getRemovedContentOfBanList(
            banListStartDate
        )

        removedCards.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }

        // builds meta data object for removed cards request
        val removedCardsMeta = BanListRemovedContent(
            banListStartDate,
            getPreviousBanListDate(banListStartDate),
            removedCards
        )

        removedCardsMeta.setLinks()
        return removedCardsMeta
    }

    private fun getPreviousBanListDate(banList: String?): String {
        return banListDao.getPreviousBanListDate(banList!!)
    }
}