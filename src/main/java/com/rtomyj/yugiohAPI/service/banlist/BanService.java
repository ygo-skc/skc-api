package com.rtomyj.yugiohAPI.service.banlist;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
	 * @return List of BanList objects
	 */
	public ServiceLayerHelper getBanListStartDates()
	{
		final ServiceLayerHelper serviceLayerHelper = ServiceLayerHelper
			.builder()
			.requestedResource(dao.getBanListStartDates())
			.status(HttpStatus.OK)
			.build();

		return serviceLayerHelper;
	}
}
