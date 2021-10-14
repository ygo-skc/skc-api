package com.rtomyj.skc.service.banlist;

import com.rtomyj.skc.constant.ErrConstants;
import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.dao.Dao.Status;
import com.rtomyj.skc.exception.ErrorType;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.banlist.BanListNewContent;
import com.rtomyj.skc.model.banlist.BanListRemovedContent;
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus;
import lombok.extern.slf4j.Slf4j;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DiffService
{
	private final Dao dao;

	/**
	 * Cache for requests/data produced by requests.
	 */

	private final Cache<String, BanListNewContent> newContentCache;

	private final Cache<String, BanListRemovedContent> removedContentCache;


	@Autowired
	public DiffService(@Qualifier("jdbc") final Dao dao)
	{
		this.dao = dao;

		this.newContentCache = new Cache2kBuilder<String, BanListNewContent>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onNewContentCacheMiss)
			.build();

		this.removedContentCache = new Cache2kBuilder<String, BanListRemovedContent>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onRemovedContentCacheMiss)
			.build();
	}



	public BanListNewContent getNewContentForGivenBanList(final String banListStartDate)
		throws YgoException
	{
		return newContentCache.get(banListStartDate);
	}


	@NotNull
	private BanListNewContent onNewContentCacheMiss(final String banListStartDate)
		throws YgoException
	{
		log.info("New content for ban list w/ start date: ({}) not found in cache. Using DB.", banListStartDate);

		if ( !dao.isValidBanList(banListStartDate) )
			throw new YgoException(String.format(ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE, banListStartDate), ErrorType.D001);


		// builds meta data object for new cards request
		final List<CardsPreviousBanListStatus> forbidden = dao.getNewContentOfBanList(banListStartDate, Status.FORBIDDEN);
		final List<CardsPreviousBanListStatus> limited = dao.getNewContentOfBanList(banListStartDate, Status.LIMITED);
		final List<CardsPreviousBanListStatus> semiLimited = dao.getNewContentOfBanList(banListStartDate, Status.SEMI_LIMITED);

		final BanListNewContent newCardsMeta = BanListNewContent.builder()
				.listRequested(banListStartDate)
				.comparedTo(this.getPreviousBanListDate(banListStartDate))
				.numNewForbidden(forbidden.size())
				.numNewLimited(limited.size())
				.numNewSemiLimited(semiLimited.size())
				.newForbidden(forbidden)
				.newLimited(limited)
				.newSemiLimited(semiLimited)
				.build();

		newCardsMeta.setLinks();

		return newCardsMeta;
	}


	public BanListRemovedContent getRemovedContentForGivenBanList(final String banListStartDate) throws YgoException
	{
		return removedContentCache.get(banListStartDate);
	}


	@NotNull
	private BanListRemovedContent onRemovedContentCacheMiss(final String banListStartDate)
		throws YgoException
	{

		log.info("Ban list removed content w/ start date: ( {} ) not found in cache. Using DB.", banListStartDate);

		if ( !dao.isValidBanList(banListStartDate) )
			throw new YgoException(String.format(ErrConstants.NO_REMOVED_BAN_LIST_CONTENT_FOR_START_DATE, banListStartDate), ErrorType.D001);


		final List<CardsPreviousBanListStatus> removedCards = dao.getRemovedContentOfBanList(banListStartDate);

    	// builds meta data object for removed cards request
		final BanListRemovedContent removedCardsMeta = BanListRemovedContent.builder()
			.listRequested(banListStartDate)
			.comparedTo(this.getPreviousBanListDate(banListStartDate))
			.removedCards(removedCards)
			.numRemoved(removedCards.size())
			.build();
		removedCardsMeta.setLinks();


		return removedCardsMeta;

	}


	private String getPreviousBanListDate(final String banList)	{ return dao.getPreviousBanListDate(banList); }
}