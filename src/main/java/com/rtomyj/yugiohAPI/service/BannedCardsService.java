package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.YGO_API_dao;
import com.rtomyj.yugiohAPI.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannedCardsService {

	@Autowired
	@Qualifier("mysql_jdbc")
	private YGO_API_dao dao;

	public List<Card> getBanListByBanStatus(String date, String status) {
		return dao.getBanListByBanStatus(date, status);
	}
}