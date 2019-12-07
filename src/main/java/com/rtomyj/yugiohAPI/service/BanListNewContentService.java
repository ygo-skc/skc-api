package com.rtomyj.yugiohAPI.service;

import java.util.List;

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


	public int getBanListPosition(String banListDate)
	{
		return dao.getBanListPosition(banListDate);
	}


	public List<String> getNewContentFromBanList(String banListDate, String status)
	{
		return dao.getNewContentFromBanList(banListDate, status);
	}


	public String getPreviousBanList(String currentBanList) {
		return dao.getPreviousBanList(currentBanList);
	}
}