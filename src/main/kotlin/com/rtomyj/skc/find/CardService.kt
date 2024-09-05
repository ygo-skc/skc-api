package com.rtomyj.skc.find

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.dao.ProductDao
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.CardBanListStatus
import com.rtomyj.skc.skcsuggestionengine.TrafficService
import com.rtomyj.skc.util.enumeration.BanListFormat
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Service that is used to access card info from DB.
 */
@Service
class CardService @Autowired constructor(
  @Qualifier("product-jdbc") val productDao: ProductDao,
  @Qualifier("ban-list-jdbc") val banListDao: BanListDao,
  @Qualifier("jdbc") val cardDao: Dao,
  val trafficService: TrafficService,
  @Qualifier("dbDateTimeFormatter") val dbDateFormatter: DateTimeFormatter
) {
  /**
   * @param cardId The unique identifier of the card desired. Must be an 8 digit String.
   * @param fetchAllInfo Whether all info about a card should be fetched and returned.
   * "All Info" includes information about the packs the card is in, the ban lists the card is in, etc.
   * @return Card object containing the information of the card desired.
   */
  @Throws(SKCException::class)
  fun getCardInfo(cardId: String, fetchAllInfo: Boolean, clientIP: String): Mono<Card> = if (fetchAllInfo) Mono
      .zip(
        getCardInfo(cardId),
        Mono.fromCallable { productDao.getProductDetailsForCard(cardId) },
        getCardRestrictionInfo(cardId),
        trafficService.submitTrafficData(TrafficResourceType.CARD, cardId, clientIP)
      )
      .map {
        it.t1.foundIn = it.t2
        it.t1.restrictedIn = it.t3

        it.t1
      } else Mono
      .zip(
        getCardInfo(cardId), trafficService.submitTrafficData(TrafficResourceType.CARD, cardId, clientIP)
      )
      .map {
        it.t1
      }

  fun getCardRestrictionInfo(cardId: String): Mono<Map<BanListFormat, MutableList<CardBanListStatus>>> = Flux
      .merge(Mono.fromCallable { banListDao.getBanListDetailsForCard(cardId, BanListFormat.TCG) },
        Mono.fromCallable { banListDao.getBanListDetailsForCard(cardId, BanListFormat.MD) },
        Mono.fromCallable { banListDao.getBanListDetailsForCard(cardId, BanListFormat.DL) })
      .collectList()
      .map { restrictions ->
        val m = mutableMapOf(
          BanListFormat.TCG to mutableListOf<CardBanListStatus>(),
          BanListFormat.MD to mutableListOf(),
          BanListFormat.DL to mutableListOf()
        )
        Collections.unmodifiableMap(
          restrictions
              .flatten()
              .groupByTo(m) { it.format })
      }

  fun getCardInfo(cardId: String): Mono<Card> = Mono
      .fromCallable {
        cardDao.getCardInfo(cardId)
      }
      .doOnSuccess { card ->
        card.monsterAssociation?.transformMonsterLinkRating()
      }
}