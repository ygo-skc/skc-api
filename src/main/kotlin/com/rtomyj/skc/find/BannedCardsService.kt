package com.rtomyj.skc.find

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListInstance
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.util.constant.ErrConstants
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service class that allows interfacing with the contents of a ban list.
 */
@Service
@OptIn(DelicateCoroutinesApi::class)
class BannedCardsService @Autowired constructor(
    @Qualifier("ban-list-jdbc") private val banListDao: BanListDao, private val banListDiffService: BanListDiffService
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
     * @throws SKCException if there is no ban list for given date.
     */
    @Throws(SKCException::class)
    fun getBanListByDate(
        banListStartDate: String,
        saveBandwidth: Boolean,
        format: String,
        fetchAllInfo: Boolean
    ): BanListInstance {
        log.info("Retrieving ban list w/ start date: ( {} ).", banListStartDate)

        lateinit var forbiddenCards: List<Card>

        lateinit var limitedCards: List<Card>
        lateinit var semiLimitedCards: List<Card>

        lateinit var limitedOneCards: List<Card>
        lateinit var limitedTwoCards: List<Card>
        lateinit var limitedThreeCards: List<Card>

        if (format != "DL") {
            val content = fetchStandardFormatContent(banListStartDate, format)
            forbiddenCards = content[BanListCardStatus.FORBIDDEN] ?: emptyList()
            limitedCards = content[BanListCardStatus.LIMITED] ?: emptyList()
            semiLimitedCards = content[BanListCardStatus.SEMI_LIMITED] ?: emptyList()
        } else {
            // init below vars as they will be used later regardless of format
            limitedCards = emptyList()
            semiLimitedCards = emptyList()

            val content = fetchDuelLinksFormatContent(banListStartDate, format)
            forbiddenCards = content[BanListCardStatus.FORBIDDEN] ?: emptyList()
            limitedOneCards = content[BanListCardStatus.LIMITED_ONE] ?: emptyList()
            limitedTwoCards = content[BanListCardStatus.LIMITED_TWO] ?: emptyList()
            limitedThreeCards = content[BanListCardStatus.LIMITED_THREE] ?: emptyList()
        }


        val banListInstance: BanListInstance = BanListInstance(
            banListStartDate,
            banListDao.getPreviousBanListDate(banListStartDate, format), forbiddenCards, limitedCards, semiLimitedCards
        ).apply {
            if (format == "DL") {
                this.limitedOne = limitedOneCards
                this.limitedTwo = limitedTwoCards
                this.limitedThree = limitedThreeCards

                this.numLimitedOne = this.limitedOne!!.size
                this.numLimitedTwo = this.limitedTwo!!.size
                this.numLimitedThree = this.limitedThree!!.size
            }

            validateBanListInstance(this, banListStartDate)
            setLinks()

            if (fetchAllInfo) {
                newContent = banListDiffService.getNewContentForGivenBanList(banListStartDate, format)
                removedContent = banListDiffService.getRemovedContentForGivenBanList(banListStartDate, format)
            }

            if (saveBandwidth) {
                Card.trimEffects(this)
            }
        }

        return banListInstance
    }

    private fun fetchStandardFormatContent(
        banListStartDate: String,
        format: String
    ): Map<BanListCardStatus, List<Card>> {
        val content = Collections.synchronizedMap(mutableMapOf<BanListCardStatus, List<Card>>())

        runBlocking {
            val deferredForbidden = GlobalScope.async {
                val forbiddenCards =
                    banListDao.getBanListByBanStatus(banListStartDate, BanListCardStatus.FORBIDDEN, format)
                MonsterAssociation.transformMonsterLinkRating(forbiddenCards)
                content[BanListCardStatus.FORBIDDEN] = forbiddenCards
            }

            val deferredLimited = GlobalScope.async {
                val limitedCards = banListDao.getBanListByBanStatus(banListStartDate, BanListCardStatus.LIMITED, format)
                MonsterAssociation.transformMonsterLinkRating(limitedCards)
                content[BanListCardStatus.LIMITED] = limitedCards
            }

            val deferredSemiLimited = GlobalScope.async {
                val semiLimitedCards =
                    banListDao.getBanListByBanStatus(banListStartDate, BanListCardStatus.SEMI_LIMITED, format)
                MonsterAssociation.transformMonsterLinkRating(semiLimitedCards)
                content[BanListCardStatus.SEMI_LIMITED] = semiLimitedCards
            }

            deferredForbidden.await()
            deferredLimited.await()
            deferredSemiLimited.await()
        }

        log.info(content.toString())
        return content
    }

    private fun fetchDuelLinksFormatContent(
        banListStartDate: String,
        format: String
    ): Map<BanListCardStatus, List<Card>> {
        val content = Collections.synchronizedMap(mutableMapOf<BanListCardStatus, List<Card>>())

        runBlocking {
            val deferredForbidden = GlobalScope.async {
                val forbiddenCards =
                    banListDao.getBanListByBanStatus(banListStartDate, BanListCardStatus.FORBIDDEN, format)
                MonsterAssociation.transformMonsterLinkRating(forbiddenCards)
                content[BanListCardStatus.FORBIDDEN] = forbiddenCards
            }

            val deferredLimitedOne = GlobalScope.async {
                val limitedOneCards =
                    banListDao.getBanListByBanStatus(banListStartDate, BanListCardStatus.LIMITED_ONE, format)
                MonsterAssociation.transformMonsterLinkRating(limitedOneCards)
                content[BanListCardStatus.LIMITED_ONE] = limitedOneCards
            }

            val deferredLimitedTwo = GlobalScope.async {
                val limitedTwoCards =
                    banListDao.getBanListByBanStatus(banListStartDate, BanListCardStatus.LIMITED_TWO, format)
                MonsterAssociation.transformMonsterLinkRating(limitedTwoCards)
                content[BanListCardStatus.LIMITED_TWO] = limitedTwoCards
            }

            val deferredLimitedThree = GlobalScope.async {
                val limitedThreeCards =
                    banListDao.getBanListByBanStatus(banListStartDate, BanListCardStatus.LIMITED_THREE, format)
                MonsterAssociation.transformMonsterLinkRating(limitedThreeCards)
                content[BanListCardStatus.LIMITED_THREE] = limitedThreeCards
            }

            deferredForbidden.await()
            deferredLimitedOne.await()
            deferredLimitedTwo.await()
            deferredLimitedThree.await()
        }

        return content
    }


    @Throws(SKCException::class)
    private fun validateBanListInstance(banListInstance: BanListInstance, banListStartDate: String) {
        if (banListInstance.numForbidden == 0 && banListInstance.numLimited == 0 && banListInstance.numSemiLimited == 0) {
            throw SKCException(
                String.format(
                    ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate
                ), ErrorType.DB001
            )
        }
    }
}