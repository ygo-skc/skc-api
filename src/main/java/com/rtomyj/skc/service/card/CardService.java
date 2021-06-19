package com.rtomyj.skc.service.card;

import com.rtomyj.skc.dao.database.Dao;
import com.rtomyj.skc.helper.exceptions.YgoException;
import com.rtomyj.skc.model.HateoasLinks;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.model.product.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service that is used to access card info from DB.
 */
@Service
@Slf4j
public class CardService
{
	private final Dao dao;

	/**
	 * Cache used to store card data to prevent querying DB.
	 */
	private final Cache<CardRequest, Card> CARD_CACHE;


	@Autowired
	public CardService(@Qualifier("jdbc") final Dao dao)
	{

		this.dao = dao;
		this.CARD_CACHE = new Cache2kBuilder<CardRequest, Card>() {}
			.expireAfterWrite(1, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onCacheMiss)
			.build();

	}


	/**
	 * @param cardId The unique identifier of the card desired. Must be an 8 digit String.
	 * @param fetchAllInfo Whether all info about a card should be fetched and returned.
	 *                        "All Info" includes information about the packs the card is in, the ban lists the card is in, etc.
	 * @return Card object containing the information of the card desired.
	 */
	public Card getCardInfo(final String cardId, final boolean fetchAllInfo)
		throws YgoException
	{

		return CARD_CACHE.get(new CardRequest(cardId, fetchAllInfo));

	}


	// todo: add javadoc
	public Card onCacheMiss(final CardRequest cardRequest)
		throws YgoException
	{

		log.info("Card w/ id: ( {} ) not found in cache. Using DB.", cardRequest.cardId);

		final Card foundCard = dao.getCardInfo(cardRequest.cardId);


		if (cardRequest.fetchAllInfo)
		{
			foundCard.setFoundIn(new ArrayList<>(dao.getProductDetailsForCard(cardRequest.cardId)));
			foundCard.setRestrictedIn(dao.getBanListDetailsForCard(cardRequest.cardId));

			/*
				Cleaning product info for card by grouping different occurrences of a card (like the same card in different rarity)
				found in the same pack into the same ProductContent object
			 */
			Product firstOccurrenceOfProduct = null;
			final Iterator<Product> it = foundCard.getFoundIn().iterator();

			while (it.hasNext())
			{
				final Product currentProduct = it.next();

				if ( firstOccurrenceOfProduct != null && firstOccurrenceOfProduct.getProductId().equals(currentProduct.getProductId())
						&& firstOccurrenceOfProduct.getProductContent().get(0).getProductPosition().equals(currentProduct.getProductContent().get(0).getProductPosition()) )
				{
					firstOccurrenceOfProduct.getProductContent().addAll(currentProduct.getProductContent());
					it.remove();
				}
				else	firstOccurrenceOfProduct = currentProduct;
			}
		}


		foundCard.setLinks();
		return foundCard;

	}


	// todo: add javadoc
	public List<Card> getCardSearchResults(final String cardId, final String cardName, final String cardAttribute, final String cardColor, final String monsterType
			, final int limit, final boolean saveBandwidth)
		throws YgoException
	{

		final List<Card> searchResults = dao.searchForCardWithCriteria(cardId, cardName, cardAttribute, cardColor, monsterType, limit, false);

		if (saveBandwidth)
		{
			log.debug("Trimming card effects to save bandwidth.");
			Card.trimEffects(searchResults);
		}

		log.debug("Setting Hateoas links.");
		HateoasLinks.setLinks(searchResults);
		return searchResults;

	}


	// todo: add javadoc
	@Getter
	@EqualsAndHashCode
	private class CardRequest
	{

		private final String cardId;
		private final boolean fetchAllInfo;

		public CardRequest(final String cardId, final boolean fetchAllInfo)
		{

			this.cardId = cardId;
			this.fetchAllInfo = fetchAllInfo;

		}

	}

}