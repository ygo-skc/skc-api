package com.rtomyj.skc.service

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListInstance
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.util.constant.ErrConstants
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

/**
 * Service class that allows interfacing with the contents of a ban list.
 */
@Service
class BannedCardsService
    @Autowired
    constructor(
        @param:Qualifier("ban-list-jdbc") private val banListDao: BanListDao,
        private val banListDiffService: BanListDiffService,
        @param:Qualifier("jdbc-dispatcher") private val jdbcDispatcher: CoroutineDispatcher,
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
            fetchAllInfo: Boolean,
        ): BanListInstance {
            log.info("Retrieving ban list content for ban list w/ start date {} & format {}", banListStartDate, format)

            if (!banListDao.isBanListValid(banListStartDate, format)) {
                throw SKCException(String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate), ErrorType.DB001)
            }

            val isDuelLinksFormat = format == "DL"
            val content =
                fetchContent(
                    if (isDuelLinksFormat) {
                        BanListCardStatus.DUEL_LINKS_FORMAT_STATUSES
                    } else {
                        BanListCardStatus.STANDARD_FORMAT_STATUSES
                    },
                    banListStartDate,
                    format,
                )

            val banListInstance: BanListInstance =
                BanListInstance(
                    banListStartDate,
                    banListDao.getPreviousBanListDate(banListStartDate, format),
                    content[BanListCardStatus.FORBIDDEN] ?: emptyList(),
                    content[BanListCardStatus.LIMITED] ?: emptyList(),
                    content[BanListCardStatus.SEMI_LIMITED] ?: emptyList(),
                ).apply {
                    if (isDuelLinksFormat) {
                        val limitedOneContent = content[BanListCardStatus.LIMITED_ONE] ?: emptyList()
                        val limitedTwoContent = content[BanListCardStatus.LIMITED_TWO] ?: emptyList()
                        val limitedThreeContent = content[BanListCardStatus.LIMITED_THREE] ?: emptyList()

                        this.limitedOne = limitedOneContent
                        this.limitedTwo = limitedTwoContent
                        this.limitedThree = limitedThreeContent

                        this.numLimitedOne = limitedOneContent.size
                        this.numLimitedTwo = limitedTwoContent.size
                        this.numLimitedThree = limitedThreeContent.size
                    }

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

        // each status is an independent query - fan them out and collect the results rather than mutating a shared map
        private fun fetchContent(
            statuses: List<BanListCardStatus>,
            banListStartDate: String,
            format: String,
        ): Map<BanListCardStatus, List<Card>> =
            runBlocking {
                statuses
                    .map { status ->
                        async(jdbcDispatcher) {
                            status to
                                banListDao
                                    .getBanListByBanStatus(banListStartDate, status, format)
                                    .also { MonsterAssociation.transformMonsterLinkRating(it) }
                        }
                    }.awaitAll()
                    .toMap()
            }
    }
