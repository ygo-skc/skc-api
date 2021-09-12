package com.rtomyj.skc.service.banlist;

import com.rtomyj.skc.dao.database.Dao;
import com.rtomyj.skc.dao.database.Dao.Status;
import com.rtomyj.skc.helper.constants.ErrConstants;
import com.rtomyj.skc.helper.exceptions.YgoException;
import com.rtomyj.skc.model.banlist.BanListInstance;
import com.rtomyj.skc.model.card.Card;
import lombok.extern.slf4j.Slf4j;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service class that allows interfacing with the contents of a ban list.
 */
@Service
@Slf4j
public class BannedCardsService
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


	private  final DiffService diffService;


	@Autowired
	public BannedCardsService(@Qualifier("jdbc") final Dao dao, final DiffService diffService)
	{
		this.dao = dao;
		this.diffService = diffService;

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



	// ToDo: update javaDoc
	/**
	 * Uses the desired date of the ban list and retrieves the contents of the ban list but with the cards that have the desired status.
	 * @param banListStartDate The date of the ban list to retrieve from DB. Must follow format: YYYY-DD-MM.
	 * @param saveBandwidth Restriction on what kind of ban list cards to retrieve from DB (forbidden, limited, semi-limited)
	 * @return List of Cards that satisfy the wanted criteria.
	 */
	public BanListInstance getBanListByBanStatus(final String banListStartDate, final boolean saveBandwidth, final boolean fetchAllInfo)
		throws YgoException
	{

		/* Determines which cache to use depending on user bandwidth preferences */
		Cache<String, BanListInstance> cache = (saveBandwidth)? BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE : BAN_LIST_CARDS_CACHE;

		final BanListInstance banListInstance = cache.get(banListStartDate);

		if (fetchAllInfo)
		{
			banListInstance.setNewContent(diffService.getNewContentOfBanList(banListStartDate));
			banListInstance.setRemovedContent(diffService.getRemovedContentOfBanList(banListStartDate));
		}

		return banListInstance;

	}



	private BanListInstance onCacheMiss(final String banListStartDate)
		throws YgoException
	{
		log.info("Ban list w/ start date: ( {} ) not found in cache. Using DB.", banListStartDate);
		final BanListInstance banListInstance = BanListInstance.builder()
			.forbidden(dao.getBanListByBanStatus(banListStartDate, Status.FORBIDDEN))
			.limited(dao.getBanListByBanStatus(banListStartDate, Status.LIMITED))
			.semiLimited(dao.getBanListByBanStatus(banListStartDate, Status.SEMI_LIMITED))
			.effectiveDate(banListStartDate)
			.build();

		banListInstance.setNumForbidden(banListInstance.getForbidden().size());
		banListInstance.setNumLimited(banListInstance.getLimited().size());
		banListInstance.setNumSemiLimited(banListInstance.getSemiLimited().size());

		if (banListInstance.getNumForbidden() == 0 && banListInstance.getNumLimited() == 0
				&& banListInstance.getNumSemiLimited() == 0)
		{
			throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate));
		}

		banListInstance.setLinks();
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
			.effectiveDate(banListStartDate)
			.build();

		banListInstance.setNumForbidden(banListInstance.getForbidden().size());
		banListInstance.setNumLimited(banListInstance.getLimited().size());
		banListInstance.setNumSemiLimited(banListInstance.getSemiLimited().size());

		if (banListInstance.getForbidden().size() == 0 && banListInstance.getLimited().size() == 0
				&& banListInstance.getSemiLimited().size() == 0)
		{
			throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate));
		}

		Card.trimEffects(banListInstance);
		banListInstance.setLinks();
		return banListInstance;

	}
}