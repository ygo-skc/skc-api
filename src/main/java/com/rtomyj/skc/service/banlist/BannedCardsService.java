package com.rtomyj.skc.service.banlist;

import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.dao.Dao.Status;
import com.rtomyj.skc.constant.ErrConstants;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.banlist.BanListInstance;
import com.rtomyj.skc.model.card.Card;
import lombok.extern.slf4j.Slf4j;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.jetbrains.annotations.NotNull;
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
	private final Cache<String, BanListInstance> banListInstanceCache;

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 * The difference between this and the above cache is that this cache has trimmed text to prevent using too much bandwidth
	 */
	private final Cache<String, BanListInstance> banListInstanceLowBandwidthCache;


	private  final DiffService diffService;


	@Autowired
	public BannedCardsService(@Qualifier("jdbc") final Dao dao, final DiffService diffService)
	{
		this.dao = dao;
		this.diffService = diffService;

		this.banListInstanceCache = new Cache2kBuilder<String, BanListInstance>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onCacheMiss)
			.build();

		this.banListInstanceLowBandwidthCache = new Cache2kBuilder<String, BanListInstance>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onLowBandwidthCacheMiss)
			.build();
	}


	/**
	 * Using a date, retrieves the contents of a ban list (as long as there is a ban list effective for given date).
	 * @param banListStartDate The date of the ban list to retrieve from DB. Must follow format: YYYY-DD-MM.
	 * @param saveBandwidth Restriction on what kind of ban list cards to retrieve from DB (forbidden, limited, semi-limited)
	 * @param fetchAllInfo whether all information should be fetched for a particular ban list. In this case, not only are the contents of the ban list returned
	 *                     , but also information on newly added cards to the ban list and cards no longer on ban list (compared to previous ban list).
	 * @return Object representation of a ban list.
	 * @throws YgoException if there is no ban list for given date.
	 */
	public BanListInstance getBanListByDate(final String banListStartDate, final boolean saveBandwidth, final boolean fetchAllInfo)
		throws YgoException
	{

		// Determines which cache to use depending on user bandwidth preferences
		Cache<String, BanListInstance> cache = (saveBandwidth)? banListInstanceLowBandwidthCache : banListInstanceCache;

		final BanListInstance banListInstance = cache.get(banListStartDate);

		if (fetchAllInfo)
		{
			banListInstance.setNewContent(diffService.getNewContentForGivenBanList(banListStartDate));
			banListInstance.setRemovedContent(diffService.getRemovedContentForGivenBanList(banListStartDate));
		}

		return banListInstance;

	}



	@NotNull
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

		validateDBValue(banListInstance, banListStartDate);

		banListInstance.setLinks();
		return banListInstance;
	}



	@NotNull
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

		validateDBValue(banListInstance, banListStartDate);

		Card.trimEffects(banListInstance);
		banListInstance.setLinks();
		return banListInstance;

	}


	private void validateDBValue(final BanListInstance banListInstance, final String banListStartDate) {
		if (banListInstance.getNumForbidden() == 0 && banListInstance.getNumLimited() == 0
				&& banListInstance.getNumSemiLimited() == 0)
		{
			throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate));
		}
	}
}