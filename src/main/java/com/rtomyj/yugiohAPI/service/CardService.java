package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	 * @param cardID The unique identifier of the card desired. Must be an 8 digit String.
	 * @return Card object containing the information of the card desired.
	 */
	public Card getCardInfo(String cardID)
	{
		return dao.getCardInfo(cardID);
	}
}