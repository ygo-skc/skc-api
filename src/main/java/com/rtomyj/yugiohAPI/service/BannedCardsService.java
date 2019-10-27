package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.Dao;
import com.rtomyj.yugiohAPI.dao.Dao.Status;
import com.rtomyj.yugiohAPI.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannedCardsService
{
	@Autowired
	@Qualifier("jdbc")
	private Dao dao;



	/**
	 * @param date
	 * @param status
	 * @return item
	 */
	public List<Card> getBanListByBanStatus(String date, Status status)
	{
		return dao.getBanListByBanStatus(date, status);
	}
}