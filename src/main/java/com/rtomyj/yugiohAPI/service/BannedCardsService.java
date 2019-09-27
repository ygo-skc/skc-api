package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.Dao;
import com.rtomyj.yugiohAPI.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannedCardsService {

	@Autowired
	@Qualifier("mysql_jdbc")
	private Dao dao;

	public List<Card> getBanListByBanStatus(String date, String status) {
		return dao.getBanListByBanStatus(date, status);
	}
}