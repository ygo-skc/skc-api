package com.rtomyj.yugiohAPI.service.banlist;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.BanListStartDates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Service used to interface with the ban_lists table in DB.
 */
@Service
@Slf4j
public class BanService
{
	/**
	 * DB access object.
	 */
	@Autowired
	@Qualifier("hibernate")
	private Dao dao;



	/**
	 * @return List of BanList objects
	 */
	public BanListStartDates getBanListStartDates()
	{
		log.info("Sending list of ban list start dates.");
		return dao.getBanListStartDates();
	}
}
