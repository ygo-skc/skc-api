package com.rtomyj.skc.find

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListNewContent
import com.rtomyj.skc.model.BanListRemovedContent
import com.rtomyj.skc.model.CardsPreviousBanListStatus
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.util.constant.ErrConstants
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
class BanListDiffService @Autowired constructor(
    @Qualifier("ban-list-jdbc") val banListDao: BanListDao
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }

    /**
     * This method should be used when newly added cards are needed for a given ban list.
     * Using the BanListCardStatus, this method will use the DAO to fetch appropriate cards.
     */
    private fun getNewContent(
        banStatus: BanListCardStatus,
        newContents: MutableMap<BanListCardStatus, List<CardsPreviousBanListStatus>>,
        banListStartDate: String,
        previousBanListDate: String,
        format: String
    ) {
        val newlyAdded = banListDao.getNewContentOfBanList(banListStartDate, previousBanListDate, banStatus, format)
        newlyAdded.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
        newContents[banStatus] = newlyAdded
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun newContentNormalFormat(
        banListStartDate: String,
        previousBanListDate: String,
        format: String
    ): BanListNewContent {
        val content = Collections.synchronizedMap(mutableMapOf<BanListCardStatus, List<CardsPreviousBanListStatus>>())


        runBlocking {
            val deferredNewlyForbidden = GlobalScope.async {
                getNewContent(BanListCardStatus.FORBIDDEN, content, banListStartDate, previousBanListDate, format)
            }

            val deferredNewlyLimited = GlobalScope.async {
                getNewContent(BanListCardStatus.LIMITED, content, banListStartDate, previousBanListDate, format)
            }

            val deferredNewlySemiLimited = GlobalScope.async {
                getNewContent(BanListCardStatus.SEMI_LIMITED, content, banListStartDate, previousBanListDate, format)
            }

            deferredNewlyForbidden.await()
            deferredNewlyLimited.await()
            deferredNewlySemiLimited.await()
        }

        return BanListNewContent(
            banListStartDate,
            previousBanListDate,
            content[BanListCardStatus.FORBIDDEN] ?: emptyList(),
            content[BanListCardStatus.LIMITED] ?: emptyList(),
            content[BanListCardStatus.SEMI_LIMITED] ?: emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun newContentDuelLinksFormat(
        banListStartDate: String,
        previousBanListDate: String,
        format: String
    ): BanListNewContent {
        val content = Collections.synchronizedMap(mutableMapOf<BanListCardStatus, List<CardsPreviousBanListStatus>>())

        runBlocking {
            val deferredNewlyForbidden = GlobalScope.async {
                getNewContent(BanListCardStatus.FORBIDDEN, content, banListStartDate, previousBanListDate, format)
            }

            val deferredNewlyLimitedOne = GlobalScope.async {
                getNewContent(BanListCardStatus.LIMITED_ONE, content, banListStartDate, previousBanListDate, format)
            }

            val deferredNewlyLimitedTwo = GlobalScope.async {
                getNewContent(BanListCardStatus.LIMITED_TWO, content, banListStartDate, previousBanListDate, format)
            }

            val deferredNewlyLimitedThree = GlobalScope.async {
                getNewContent(BanListCardStatus.LIMITED_THREE, content, banListStartDate, previousBanListDate, format)
            }

            deferredNewlyForbidden.await()
            deferredNewlyLimitedOne.await()
            deferredNewlyLimitedTwo.await()
            deferredNewlyLimitedThree.await()
        }

        return BanListNewContent(
            banListStartDate,
            previousBanListDate,
            content[BanListCardStatus.FORBIDDEN] ?: emptyList(),
            emptyList(),
            emptyList(),
            content[BanListCardStatus.LIMITED_ONE] ?: emptyList(),
            content[BanListCardStatus.LIMITED_TWO] ?: emptyList(),
            content[BanListCardStatus.LIMITED_THREE] ?: emptyList(),
        )
    }


    @Throws(SKCException::class)
    fun getNewContentForGivenBanList(banListStartDate: String, format: String): BanListNewContent {
        if (!banListDao.isValidBanList(banListStartDate)) throw SKCException(
            String.format(
                ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE,
                banListStartDate
            ), ErrorType.DB001
        )

        val previousBanListDate = getPreviousBanListDate(banListStartDate, format)

        // builds meta data object for new cards request
        val newCardsMeta =
            if (format == "DL") newContentDuelLinksFormat(banListStartDate, previousBanListDate, format)
            else newContentNormalFormat(banListStartDate, previousBanListDate, format)

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

        val previousBanListDate = getPreviousBanListDate(banListStartDate, format)

        val removedCards = banListDao.getRemovedContentOfBanList(banListStartDate, previousBanListDate, format)
        removedCards.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }

        // builds meta data object for removed cards request
        val removedCardsMeta = BanListRemovedContent(
            banListStartDate,
            previousBanListDate,
            removedCards
        )

        removedCardsMeta.setLinks()
        return removedCardsMeta
    }

    private fun getPreviousBanListDate(banList: String?, format: String): String {
        return banListDao.getPreviousBanListDate(banList!!, format)
    }
}