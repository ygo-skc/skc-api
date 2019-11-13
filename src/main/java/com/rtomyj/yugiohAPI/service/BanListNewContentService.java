package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.Dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanListNewContentService
{
	@Autowired
	@Qualifier("jdbc")
	private Dao dao;


	public int getNumberOfBanLists()
	{
		return dao.getNumberOfBanLists();
	}


	public int getBanListPosition()
	{
		return dao.getBanListPosition();
	}
}