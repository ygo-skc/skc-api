package com.rtomyj.yugiohAPI.service;

import java.util.List;

import com.rtomyj.yugiohAPI.dao.Dao;
import com.rtomyj.yugiohAPI.model.BanLists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BanListService
{
	@Autowired
	@Qualifier("hibernate")
	private Dao dao;



	/**
	 * @return item
	 */
	public List<BanLists> getBanListStartDates()
	{
		return dao.getBanListStartDates();
	}
}
