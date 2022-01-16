package com.rtomyj.skc.service.card

import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.dao.ProductDao
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.HateoasLinks
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.model.product.Product
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
    @Qualifier("jdbc") val cardDao: Dao
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
    fun getCardInfo(cardId: String, fetchAllInfo: Boolean): Card {
        log.info("Fetching info for card w/ ID: ( {} )", cardId)

        val card = cardDao.getCardInfo(cardId)
        if (fetchAllInfo) {
            card.foundIn = ArrayList(productDao.getProductDetailsForCard(cardId))
            card.restrictedIn = banListDao.getBanListDetailsForCard(cardId)
            card.monsterAssociation?.transformMonsterLinkRating()

            /*
				Cleaning product info for card by grouping different occurrences of a card (like the same card in different rarity)
				found in the same pack into the same ProductContent object
			 */
            var firstOccurrenceOfProduct: Product? = null
            val it = card.foundIn.iterator()
            while (it.hasNext()) {
                val currentProduct = it.next()
                if (firstOccurrenceOfProduct != null && firstOccurrenceOfProduct.productId == currentProduct.productId && firstOccurrenceOfProduct.productContent[0].productPosition == currentProduct.productContent[0].productPosition) {
                    firstOccurrenceOfProduct.productContent.addAll(currentProduct.productContent)
                    it.remove()
                } else firstOccurrenceOfProduct = currentProduct
            }
        }
        card.setLinks()
        return card
    }


    @Throws(YgoException::class)
    fun getCardSearchResults(
        cardId: String,
        cardName: String,
        cardAttribute: String,
        cardColor: String,
        monsterType: String,
        limit: Int,
        saveBandwidth: Boolean
    ): List<Card> {
        val searchResults =
            cardDao.searchForCardWithCriteria(cardId, cardName, cardAttribute, cardColor, monsterType, limit, false)

        if (saveBandwidth) {
            log.debug("Trimming card effects to save bandwidth.")
            Card.trimEffects(searchResults)
        }

        HateoasLinks.setLinks(searchResults)
        return searchResults
    }
}