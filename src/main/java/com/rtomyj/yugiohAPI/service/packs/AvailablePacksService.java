package com.rtomyj.yugiohAPI.service.packs;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.pack.Pack;
import com.rtomyj.yugiohAPI.model.pack.Packs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AvailablePacksService
{

	@Autowired
	@Qualifier("jdbc")
	private Dao dao;

	public Packs getAvailablePacks()
	{
		final Packs allPacks = Packs
			.builder()
			.packs(dao.getAvailablePacks())
			.build();

		Pack.setLinks(allPacks.getPacks());

		return allPacks;
	}



	public Pack getPack(final String packId)
	{
		final Pack pack = dao.getPackContents(packId);
		pack.setLink();

		return pack;
	}
}