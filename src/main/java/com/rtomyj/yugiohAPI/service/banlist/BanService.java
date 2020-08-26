package com.rtomyj.yugiohAPI.service.banlist;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.banlist.BanListStartDates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Service used to interface with database for basic operations regarding ban lists.
 */
@Service
@Slf4j
public class BanService
{

	/**
	 * Object used to interface with DB.
	 */
	private final Dao dao;


	/**
	 * Create object instance.
	 * @param dao object used to interface with DB.
	 */
	@Autowired
	public BanService(@Qualifier("hibernate") final Dao dao)
	{

		this.dao = dao;

	}


	/**
	 * Uses dao helper object to retrieve start dates of all ban lists in the database.
	 * @return List of BanList objects
	 */
	public BanListStartDates getBanListStartDates()
	{

		log.info("Sending list of ban list start dates.");
		final BanListStartDates banListStartDates = dao.getBanListStartDates();
		banListStartDates.setLinks();

		return banListStartDates;

	}

}
