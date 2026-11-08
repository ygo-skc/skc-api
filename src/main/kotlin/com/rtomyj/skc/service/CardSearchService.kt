package com.rtomyj.skc.service

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.dao.CardSearchDao
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.CardSearchParameters
import com.rtomyj.skc.util.Blocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * Service that is used to access card info from DB.
 */
@Service
class CardSearchService
    @Autowired
    constructor(
        @param:Qualifier("ban-list-jdbc") val banListDao: BanListDao,
        @param:Qualifier("jdbc") val dao: CardSearchDao,
    ) {
        companion object {
            private val log = LoggerFactory.getLogger(this::class.java.name)
        }

        fun searchCard(cardSearchParameters: CardSearchParameters): Mono<List<Card>> =
            Blocking.mono {
                val searchResults = dao.searchCard(cardSearchParameters, false)
                if (cardSearchParameters.saveBandwidth) {
                    log.debug("Trimming card effects to save bandwidth.")
                    Card.trimEffects(searchResults)
                }
                searchResults
            }
    }
