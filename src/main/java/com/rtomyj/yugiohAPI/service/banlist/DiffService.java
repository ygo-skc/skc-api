package com.rtomyj.yugiohAPI.service.banlist;

import java.util.List;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.model.BanListComparisonResults;
import com.rtomyj.yugiohAPI.model.BanListNewContent;
import com.rtomyj.yugiohAPI.model.BanListRemovedContent;
import com.rtomyj.yugiohAPI.model.NewCards;

import org.cache2k.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DiffService
{
	@Autowired
	@Qualifier("jdbc")
	private Dao dao;

	/**
	 * Cache for requests/data produced by requests.
	 */
	@Autowired
	@Qualifier("banListNewCardsCache")
	private Cache<String, BanListNewContent> NEW_CARDS_CACHE;

	@Autowired
	@Qualifier("banListRemovedCardsCache")
	private Cache<String, BanListRemovedContent> REMOVED_CARDS_CACHE;


	public ServiceLayerHelper getNewContentOfBanList(final String banListStartDate) throws YgoException
	{
		final ServiceLayerHelper serviceLayerHelper = ServiceLayerHelper.builder()
			.inCache(false)
			.isContentReturned(false)
			.requestedResource(NEW_CARDS_CACHE.get(banListStartDate))
			.status(HttpStatus.OK)
			.build();


		// Resource isn't in cache and ban list date passed validation
		if ( serviceLayerHelper.getRequestedResource() == null )
		{
			// retrieving new cards by ban list status
			final NewCards newCards = NewCards.builder()
				.forbidden(dao.getNewContentOfBanList(banListStartDate, Status.FORBIDDEN))
				.limited(dao.getNewContentOfBanList(banListStartDate, Status.LIMITED))
				.semiLimited(dao.getNewContentOfBanList(banListStartDate, Status.SEMI_LIMITED))
				.build();

			// There are changes for requested date - ie, requested date found in DB
			if ( newCards.getForbidden().size() != 0 || newCards.getLimited().size() != 0 || newCards.getSemiLimited().size() != 0 )
			{
				// builds meta data object for new cards request
				final BanListNewContent newCardsMeta = BanListNewContent.builder()
					.listRequested(banListStartDate)
					.comparedTo(this.getPreviousBanListDate(banListStartDate))
					.newCards(newCards)
					.build();

				NEW_CARDS_CACHE.put(banListStartDate, newCardsMeta);

				serviceLayerHelper.setRequestedResource(newCardsMeta);
				serviceLayerHelper.setIsContentReturned(true);
			}
			// There are no changes for requested date - ie, requested date not found in DB.
			else	throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE, banListStartDate));
		}
		// Resource in cache and ban list date passed validation
		else
		{
			serviceLayerHelper.setInCache(true);
			serviceLayerHelper.setIsContentReturned(true);
		}

		return serviceLayerHelper;
	}



	public ServiceLayerHelper getRemovedContentOfBanList(final String banListStartDate) throws YgoException
	{
		final ServiceLayerHelper serviceLayerHelper = ServiceLayerHelper.builder()
			.inCache(false)
			.isContentReturned(false)
			.requestedResource(REMOVED_CARDS_CACHE.get(banListStartDate))
			.status(HttpStatus.OK)
			.build();


		if ( serviceLayerHelper.getRequestedResource() == null )
		{
			// retrieving removed cards by ban list status

			List<BanListComparisonResults> removedCards = dao.getRemovedContentOfBanList(banListStartDate);

			// There are changes for requested date - ie, requested date found in DB
			if ( removedCards.size() != 0 )
			{
				// builds meta data object for removed cards request
				final BanListRemovedContent removedCardsMeta = BanListRemovedContent.builder()
					.listRequested(banListStartDate)
					.comparedTo(this.getPreviousBanListDate(banListStartDate))
					.removedCards(removedCards)
					.build();


				REMOVED_CARDS_CACHE.put(banListStartDate, removedCardsMeta);

				serviceLayerHelper.setRequestedResource(removedCardsMeta);
				serviceLayerHelper.setIsContentReturned(true);
			}
			// There are no changes for requested date - ie, requested date not found in DB.
			else	throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.NO_REMOVED_BAN_LIST_CONTENT_FOR_START_DATE, banListStartDate));
		}
		// Resource in cache and ban list date passed validation
		else
		{
			serviceLayerHelper.setInCache(true);
			serviceLayerHelper.setIsContentReturned(true);
		}


		return serviceLayerHelper;
	}



	public String getPreviousBanListDate(String banList)	{ return dao.getPreviousBanListDate(banList); }
}