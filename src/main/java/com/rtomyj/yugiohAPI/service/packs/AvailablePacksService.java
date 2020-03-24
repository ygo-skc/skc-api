package com.rtomyj.yugiohAPI.service.packs;

import java.util.List;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.pack.Pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AvailablePacksService
{

	@Autowired
	@Qualifier("jdbc")
	private Dao dao;

	public List<Pack> getAvailablePacks()
	{
		return dao.getAvailablePacks();
	}



	public Pack getPack(final String packId)
	{
		return dao.getPackContents(packId);
	}
}