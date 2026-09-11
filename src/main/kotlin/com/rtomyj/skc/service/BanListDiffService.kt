package com.rtomyj.skc.service

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListNewContent
import com.rtomyj.skc.model.BanListRemovedContent
import com.rtomyj.skc.model.CardsPreviousBanListStatus
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.util.constant.ErrConstants
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class BanListDiffService
    @Autowired
    constructor(
        @param:Qualifier("ban-list-jdbc") val banListDao: BanListDao,
        @param:Qualifier("jdbc-dispatcher") private val jdbcDispatcher: CoroutineDispatcher,
    ) {
        @Throws(SKCException::class)
        fun getNewContentForGivenBanList(
            banListStartDate: String,
            format: String,
        ): BanListNewContent {
            if (!banListDao.isBanListValid(banListStartDate, format)) {
                throw SKCException(String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate), ErrorType.DB001)
            }

            val previousBanListDate = banListDao.getPreviousBanListDate(banListStartDate, format)
            val statuses =
                if (format == "DL") {
                    BanListCardStatus.DUEL_LINKS_FORMAT_STATUSES
                } else {
                    BanListCardStatus.STANDARD_FORMAT_STATUSES
                }

            // statuses not fetched for the requested format are simply absent from the map
            val newContent = fetchNewContent(statuses, banListStartDate, previousBanListDate, format)

            return BanListNewContent(
                banListStartDate,
                previousBanListDate,
                newContent[BanListCardStatus.FORBIDDEN] ?: emptyList(),
                newContent[BanListCardStatus.LIMITED] ?: emptyList(),
                newContent[BanListCardStatus.SEMI_LIMITED] ?: emptyList(),
                newContent[BanListCardStatus.LIMITED_ONE] ?: emptyList(),
                newContent[BanListCardStatus.LIMITED_TWO] ?: emptyList(),
                newContent[BanListCardStatus.LIMITED_THREE] ?: emptyList(),
            )
        }

        @Throws(SKCException::class)
        fun getRemovedContentForGivenBanList(
            banListStartDate: String,
            format: String,
        ): BanListRemovedContent {
            if (!banListDao.isBanListValid(banListStartDate, format)) {
                throw SKCException(String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate), ErrorType.DB001)
            }

            val previousBanListDate = banListDao.getPreviousBanListDate(banListStartDate, format)

            val removedCards = banListDao.getRemovedContentOfBanList(banListStartDate, previousBanListDate, format)
            removedCards.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }

            // builds metadata object for removed cards request
            val removedCardsMeta =
                BanListRemovedContent(
                    banListStartDate,
                    previousBanListDate,
                    removedCards,
                )

            return removedCardsMeta
        }

        /**
         * Queries each status concurrently - statuses absent from [statuses] are absent from the returned map.
         */
        private fun fetchNewContent(
            statuses: List<BanListCardStatus>,
            banListStartDate: String,
            previousBanListDate: String,
            format: String,
        ): Map<BanListCardStatus, List<CardsPreviousBanListStatus>> =
            runBlocking {
                statuses
                    .map { status ->
                        async(jdbcDispatcher) {
                            // create a pair where key is ban status and value is new content for that status
                            status to
                                banListDao
                                    .getNewContentOfBanList(banListStartDate, previousBanListDate, status, format)
                                    .also { newlyAdded ->
                                        newlyAdded.forEach { MonsterAssociation.transformMonsterLinkRating(it.card) }
                                    }
                        }
                    }.awaitAll()
                    .toMap()
            }
    }
