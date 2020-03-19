package com.rtomyj.yugiohAPI.service.banlist;

import java.util.concurrent.TimeUnit;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.model.BanListInstance;
import com.rtomyj.yugiohAPI.model.Card;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Service class that allows interfacing with the contents of a ban list.
 */
@Service
@Slf4j
public class CardsService
{
	/**
	 * Dao for DB;
	 */
	private final Dao dao;

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 */
	private final Cache<String, BanListInstance> BAN_LIST_CARDS_CACHE;

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 * The difference between this and the above cache is that this cache has trimmed text to prevent using too much bandwidth
	 */
	private final Cache<String, BanListInstance>  BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;



	@Autowired
	public CardsService(@Qualifier("jdbc") final Dao dao)
	{
		this.dao = dao;
		this.BAN_LIST_CARDS_CACHE = new Cache2kBuilder<String, BanListInstance>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onCacheMiss)
			.build();
		this.BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE = new Cache2kBuilder<String, BanListInstance>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onLowBandwidthCacheMiss)
			.build();
	}



	/**
	 * Uses the desired date of the ban list and retrieves the contents of the ban list but with the cards that have the desired status.
	 * @param date The date of the ban list to retrieve from DB. Must follow format: YYYY-DD-MM.
	 * @param status Restriction on what kind of ban list cards to retrieve from DB (forbidden, limited, semi-limited)
	 * @return List of Cards that satisfy the wanted criteria.
	 */
	public BanListInstance getBanListByBanStatus(final String banListStartDate, final boolean saveBandwidth)
		throws YgoException
	{
		/* Determines which cache to use depending on user bandwidth preferences */
		Cache<String, BanListInstance> cache;
		if (saveBandwidth)	cache = BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;
		else	cache = BAN_LIST_CARDS_CACHE;

		return cache.get(banListStartDate);
	}



	private BanListInstance onCacheMiss(final String banListStartDate)
		throws YgoException
	{
		log.info("Ban list w/ start date: ( {} ) not found in cache. Using DB.", banListStartDate);
		final BanListInstance banListInstance = BanListInstance.builder()
			.forbidden(dao.getBanListByBanStatus(banListStartDate, Status.FORBIDDEN))
			.limited(dao.getBanListByBanStatus(banListStartDate, Status.LIMITED))
			.semiLimited(dao.getBanListByBanStatus(banListStartDate, Status.SEMI_LIMITED))
			.startDate(banListStartDate)
			.build();

		banListInstance.setNumForbidden(banListInstance.getForbidden().size());
		banListInstance.setNumLimited(banListInstance.getLimited().size());
		banListInstance.setNumSemiLimited(banListInstance.getSemiLimited().size());

		if (banListInstance.getNumForbidden() == 0 && banListInstance.getNumLimited() == 0
				&& banListInstance.getNumSemiLimited() == 0)
		{
			throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate));
		}

		return banListInstance;
	}



	private BanListInstance onLowBandwidthCacheMiss(final String banListStartDate)
		throws YgoException
	{
		log.info("Ban list w/ start date: ( {} ) not found in low bandwidth cache. Using DB.", banListStartDate);
		final BanListInstance banListInstance = BanListInstance.builder()
			.forbidden(dao.getBanListByBanStatus(banListStartDate, Status.FORBIDDEN))
			.limited(dao.getBanListByBanStatus(banListStartDate, Status.LIMITED))
			.semiLimited(dao.getBanListByBanStatus(banListStartDate, Status.SEMI_LIMITED))
			.startDate(banListStartDate)
			.build();

		if (banListInstance.getForbidden().size() == 0 && banListInstance.getLimited().size() == 0
				&& banListInstance.getSemiLimited().size() == 0)
		{
			throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate));
		}

		Card.trimEffects(banListInstance);
		return banListInstance;
	}
}