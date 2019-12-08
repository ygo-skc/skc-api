package com.rtomyj.yugiohAPI.service;

import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.dao.Dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanListDiffService
{
	@Autowired
	@Qualifier("jdbc")
	private Dao dao;


	public List<Map<String, String>>getNewContentOfBanList(String banListDate, String string)	{ return dao.getNewContentOfBanList(banListDate, string); }
	public List<Map<String, String>> getRemovedContentOfBanList(String banListDate)	{ return dao.getRemovedContentOfBanList(banListDate); }
	public String getPreviousBanListDate(String banList)	{ return dao.getPreviousBanListDate(banList); }
}