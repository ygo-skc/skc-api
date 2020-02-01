package com.rtomyj.yugiohAPI.service.banlist;

import java.util.List;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.BanListComparisonResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DiffService
{
	@Autowired
	@Qualifier("jdbc")
	private Dao dao;


	public List<BanListComparisonResults> getNewContentOfBanList(String banListDate, String string)	{ return dao.getNewContentOfBanList(banListDate, string); }
	public List<BanListComparisonResults> getRemovedContentOfBanList(String banListDate)	{ return dao.getRemovedContentOfBanList(banListDate); }
	public String getPreviousBanListDate(String banList)	{ return dao.getPreviousBanListDate(banList); }
}