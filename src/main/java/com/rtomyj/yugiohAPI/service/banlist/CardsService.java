package com.rtomyj.yugiohAPI.service.banlist;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.model.BanListInstance;
import com.rtomyj.yugiohAPI.model.Card;

import org.cache2k.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class that allows interfacing with the contents of a ban list.
 */
@Service
public class CardsService
{
	/**
	 * Dao for DB;
	 */
	@Autowired
	@Qualifier("jdbc")
	private Dao dao;

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 */
	@Autowired
	@Qualifier("banListCardsCache")
	private Cache<String, BanListInstance> BAN_LIST_CARDS_CACHE;

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 * The difference between this and the above cache is that this cache has trimmed text to prevent using too much bandwidth
	 */
	@Autowired
	@Qualifier("banListCardsCacheLowBandwidth")
	private Cache<String, BanListInstance>  BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;



	/**
	 * Uses the desired date of the ban list and retrieves the contents of the ban list but with the cards that have the desired status.
	 * @param date The date of the ban list to retrieve from DB. Must follow format: YYYY-DD-MM.
	 * @param status Restriction on what kind of ban list cards to retrieve from DB (forbidden, limited, semi-limited)
	 * @return List of Cards that satisfy the wanted criteria.
	 */
	public ServiceLayerHelper getBanListByBanStatus(String banListStartDate, boolean saveBandwidth) throws YgoException
	{
		/* Determines which cache to use depending on user bandwidth preferences */
		Cache<String, BanListInstance> cache;
		if (saveBandwidth)	cache = BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;
		else	cache = BAN_LIST_CARDS_CACHE;

		final ServiceLayerHelper serviceLayerHelper = ServiceLayerHelper.builder()
			.requestedResource(cache.get(banListStartDate))
			.inCache(false)
			.isContentReturned(false)
			.build();


		/* If the requested ban list is cached, access the cache and return the contents of the ban list. */
		if ( serviceLayerHelper.getRequestedResource() != null)
		{
			serviceLayerHelper.setStatus(HttpStatus.OK);
			serviceLayerHelper.setInCache(true);
			serviceLayerHelper.setIsContentReturned(true);
		}
		/* If not in cache, try to retrieve ban list contents from DB */
		else
		{
			/* Retrieves ban lists from DB by status */

			BanListInstance banListInstance = BanListInstance.builder()
					.forbidden(dao.getBanListByBanStatus(banListStartDate, Status.FORBIDDEN))
					.limited(dao.getBanListByBanStatus(banListStartDate, Status.LIMITED))
					.semiLimited(dao.getBanListByBanStatus(banListStartDate, Status.SEMI_LIMITED))
					.startDate(banListStartDate).build();

			banListInstance = banListInstance
				.withNumForbidden(banListInstance.getForbidden().size())
				.withNumLimited(banListInstance.getLimited().size())
				.withNumSemiLimited(banListInstance.getSemiLimited().size());

			if (saveBandwidth)	Card.trimEffects(banListInstance);


			/* If DB doesn't return at least one card for at least one status, the users ban list isn't in the DB */
			if (banListInstance.getForbidden().size() == 0 && banListInstance.getLimited().size() == 0
					&& banListInstance.getSemiLimited().size() == 0)
					{
						throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, banListStartDate));
					}
			/* If ban list is in DB, put ban list in cache and return the contents of ban list o the user. */
			else
			{
				serviceLayerHelper.setStatus(HttpStatus.OK);
				serviceLayerHelper.setIsContentReturned(true);

				cache.put(banListStartDate, banListInstance);
				serviceLayerHelper.setRequestedResource(banListInstance);
			}
		}


		return serviceLayerHelper;
	}
}