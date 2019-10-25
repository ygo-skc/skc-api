package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.Dao;
import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CardService
{
	@Autowired
	@Qualifier("mysql_jdbc")
	private Dao dao;


	public Card getCardInfo(String cardID)
	{
		return dao.getCardInfo(cardID);
	}
}