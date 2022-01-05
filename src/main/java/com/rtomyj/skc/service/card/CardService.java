package com.rtomyj.skc.service.card;

import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.dao.ProductDao;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.HateoasLinks;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.model.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service that is used to access card info from DB.
 */
@Service
@Slf4j
public class CardService {
	// fields
	private final ProductDao productDao;

	private final Dao cardDao;


	@Autowired
	public CardService(@Qualifier("jdbc-product") final ProductDao productDao, @Qualifier("jdbc") final Dao cardDao) {
		this.productDao = productDao;
		this.cardDao = cardDao;
	}


	/**
	 * @param cardId The unique identifier of the card desired. Must be an 8 digit String.
	 * @param fetchAllInfo Whether all info about a card should be fetched and returned.
	 *                        "All Info" includes information about the packs the card is in, the ban lists the card is in, etc.
	 * @return Card object containing the information of the card desired.
	 */
	public Card getCardInfo(final String cardId, final boolean fetchAllInfo)
		throws YgoException {
		log.info("Fetching info for card w/ ID: ( {} )", cardId);

		final Card foundCard = cardDao.getCardInfo(cardId);


		if (fetchAllInfo) {
			foundCard.setFoundIn(new ArrayList<>(productDao.getProductDetailsForCard(cardId)));
			foundCard.setRestrictedIn(cardDao.getBanListDetailsForCard(cardId));

			foundCard.getMonsterAssociation().transformMonsterLinkRating();

			/*
				Cleaning product info for card by grouping different occurrences of a card (like the same card in different rarity)
				found in the same pack into the same ProductContent object
			 */
			Product firstOccurrenceOfProduct = null;
			final Iterator<Product> it = foundCard.getFoundIn().iterator();

			while (it.hasNext()) {
				final Product currentProduct = it.next();

				if ( firstOccurrenceOfProduct != null && firstOccurrenceOfProduct.getProductId().equals(currentProduct.getProductId())
						&& firstOccurrenceOfProduct.getProductContent().get(0).getProductPosition().equals(currentProduct.getProductContent().get(0).getProductPosition()) ) {
					firstOccurrenceOfProduct.getProductContent().addAll(currentProduct.getProductContent());
					it.remove();
				}
				else	firstOccurrenceOfProduct = currentProduct;
			}
		}


		foundCard.setLinks();
		return foundCard;
	}


	public List<Card> getCardSearchResults(final String cardId, final String cardName, final String cardAttribute, final String cardColor, final String monsterType
			, final int limit, final boolean saveBandwidth)
		throws YgoException {
		final List<Card> searchResults = cardDao.searchForCardWithCriteria(cardId, cardName, cardAttribute, cardColor, monsterType, limit, false);

		if (saveBandwidth) {
			log.debug("Trimming card effects to save bandwidth.");
			Card.trimEffects(searchResults);
		}

		log.debug("Setting Hateoas links.");
		HateoasLinks.setLinks(searchResults);
		return searchResults;
	}
}