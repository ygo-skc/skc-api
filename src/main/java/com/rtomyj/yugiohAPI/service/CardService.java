package com.rtomyj.yugiohAPI.service;

import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.model.CardSearchCriteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service that is used to access card info from DB.
 */
@Service
public class CardService
{
	@Autowired
	@Qualifier("jdbc")
	private Dao dao;

	/**
	 * Cache used to store card data to prevent querying DB.
	 */
	@Autowired
	@Qualifier("cardsCache")
	private Map<String, Card> CARD_CACHE;



	/**
	 * @param cardID The unique identifier of the card desired. Must be an 8 digit String.
	 * @return Card object containing the information of the card desired.
	 */
	public ServiceLayerHelper getCardInfo(String cardId) throws YgoException
	{
		ServiceLayerHelper serviceLayerHelper = new ServiceLayerHelper();


		Card requestedCard = CARD_CACHE.get(cardId);
		/* If requested card was not found in cache - use DB */
		if (requestedCard != null)
		{
			serviceLayerHelper.setInCache(true);
			serviceLayerHelper.setIsContentReturned(true);
			serviceLayerHelper.setStatus(HttpStatus.OK);
			serviceLayerHelper.setRequestedResource(requestedCard);
		} else
		{
			requestedCard = dao.getCardInfo(cardId);

			serviceLayerHelper.setIsContentReturned(true);
			serviceLayerHelper.setRequestedResource(requestedCard);
			serviceLayerHelper.setStatus(HttpStatus.OK);
			CARD_CACHE.put(cardId, requestedCard);	// puts card into cache
		}

		return serviceLayerHelper;
	}



	public List<Card> getCardSearchResults(final CardSearchCriteria cardSearchCriteria) throws YgoException
	{
		return dao.getCardNameByCriteria(cardSearchCriteria);
	}
}