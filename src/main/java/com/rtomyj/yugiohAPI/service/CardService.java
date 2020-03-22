package com.rtomyj.yugiohAPI.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.Card;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

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
	 * @param cardID The unique identifier of the card desired. Must be an 8 digit String.
	 * @return Card object containing the information of the card desired.
	 */
	public Card getCardInfo(final String cardId)
		throws YgoException
	{
		return CARD_CACHE.get(cardId);
	}



	public Card onCacheMiss(final String cardId)
		throws YgoException
	{
		log.info("Card w/ id: ( {} ) not found in cache. Using DB.", cardId);

		return dao.getCardInfo(cardId);
	}



	public List<Card> getCardSearchResults(final String cardId, final String cardName, final String cardAttribute, final String cardColor, final String monsterType)
		throws YgoException
	{
		return dao.getCardNameByCriteria(cardId, cardName, cardAttribute, cardColor, monsterType);
	}
}