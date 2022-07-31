package com.rtomyj.skc.find.card

import com.rtomyj.skc.banlist.dao.BanListDao
import com.rtomyj.skc.browse.card.model.Card
import com.rtomyj.skc.browse.product.dao.ProductDao
import com.rtomyj.skc.browse.product.model.Product
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.find.card.dao.Dao
import com.rtomyj.skc.skcsuggestionengine.traffic.TrafficService
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

/**
 * Service that is used to access card info from DB.
 */
@Service
class CardService @Autowired constructor(
	@Qualifier("product-jdbc") val productDao: ProductDao,
	@Qualifier("ban-list-jdbc") val banListDao: BanListDao,
	@Qualifier("jdbc") val cardDao: Dao,
	val trafficService: TrafficService
) {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	/**
	 * @param cardId The unique identifier of the card desired. Must be an 8 digit String.
	 * @param fetchAllInfo Whether all info about a card should be fetched and returned.
	 * "All Info" includes information about the packs the card is in, the ban lists the card is in, etc.
	 * @return Card object containing the information of the card desired.
	 */
	@Throws(YgoException::class)
	fun getCardInfo(cardId: String, fetchAllInfo: Boolean, clientIP: String): Card {
		log.info("Fetching info for card w/ ID: ( {} )", cardId)

		GlobalScope.launch {
			trafficService.submitTrafficData(TrafficResourceType.CARD, cardId, clientIP)
		}

		val card = cardDao.getCardInfo(cardId)
		card.monsterAssociation?.transformMonsterLinkRating()

		if (fetchAllInfo) {
			card.foundIn = ArrayList(productDao.getProductDetailsForCard(cardId))
			card.restrictedIn = banListDao.getBanListDetailsForCard(cardId).toMutableList()

			/*
				Cleaning product info for card by grouping different occurrences of a card (like the same card in different rarity)
				found in the same pack into the same ProductContent object
			 */
			var firstOccurrenceOfProduct: Product? = null
			val it = card.foundIn?.listIterator()
			while (it != null && it.hasNext()) {
				val currentProduct = it.next()
				if (firstOccurrenceOfProduct?.productId == currentProduct.productId
					&& firstOccurrenceOfProduct.productContent[0].productPosition == currentProduct.productContent[0].productPosition
				) {
					firstOccurrenceOfProduct.productContent.addAll(currentProduct.productContent)
					it.remove()
				} else firstOccurrenceOfProduct = currentProduct
			}
		}

		card.setLinks()
		return card
	}
}