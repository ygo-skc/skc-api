package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.model.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
	private final Cache<String, Card> CARD_CACHE;



	@Autowired
	public CardService(@Qualifier("jdbc") final Dao dao)
	{
		this.dao = dao;
		this.CARD_CACHE = new Cache2kBuilder<String, Card>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
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
		final Card card = CARD_CACHE.get(cardId);
		if (fetchAllInfo)
		{
			card.setFoundIn(dao.getProductDetailsForCard(cardId));
			card.setRestrictedIn(dao.getBanListDetailsForCard(cardId));

			/*
				Cleaning product info for card by grouping different occurrences of a card (like the same card in different rarity)
				found in the same pack into the same ProductContent object
			 */
			Product firstOccurrenceOfProduct = null;
			final Iterator<Product> it = card.getFoundIn().iterator();

			while (it.hasNext())
			{
				final Product currentProduct = it.next();

				if ( firstOccurrenceOfProduct != null && firstOccurrenceOfProduct.getProductId().equals(currentProduct.getProductId()) )
				{
					firstOccurrenceOfProduct.getProductContent().addAll(currentProduct.getProductContent());
					it.remove();
				}
				else	firstOccurrenceOfProduct = currentProduct;
			}
		}

		return card;
	}



	public Card onCacheMiss(final String cardId)
		throws YgoException
	{
		log.info("Card w/ id: ( {} ) not found in cache. Using DB.", cardId);

		final Card foundCard = dao.getCardInfo(cardId);
		foundCard.setLinks();
		return foundCard;
	}



	public List<Card> getCardSearchResults(
			final String cardId
			, final String cardName
			, final String cardAttribute
			, final String cardColor
			, final String monsterType
			, final int limit
			, final boolean saveBandwidth)
		throws YgoException
	{

		final List<Card> searchResults = dao.getCardNameByCriteria(cardId, cardName, cardAttribute, cardColor, monsterType, limit);

		if (saveBandwidth)	Card.trimEffects(searchResults);
		return searchResults;

	}
}