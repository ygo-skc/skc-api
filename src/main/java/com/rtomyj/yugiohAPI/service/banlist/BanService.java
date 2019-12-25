package com.rtomyj.yugiohAPI.service.banlist;

import java.util.List;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.BanLists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service used to interface with the ban_lists table in DB.
 */
@Service
public class BanService
{
	/**
	 * DB access object.
	 */
	@Autowired
	@Qualifier("hibernate")
	private Dao dao;



	/**
	 * @return List of BanLists objects
	 */
	public List<BanLists> getBanListStartDates()
	{
		return dao.getBanListStartDates();
	}
}
