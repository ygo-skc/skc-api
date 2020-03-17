package com.rtomyj.yugiohAPI.service.banlist;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.model.BanListNewContent;
import com.rtomyj.yugiohAPI.model.BanListRemovedContent;
import com.rtomyj.yugiohAPI.model.NewCards;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DiffService
{

	private final Dao dao;

	/**
	 * Cache for requests/data produced by requests.
	 */

	private final Cache<String, BanListNewContent> NEW_CARDS_CACHE;

	private final Cache<String, BanListRemovedContent> REMOVED_CARDS_CACHE;



	@Autowired
	public DiffService(@Qualifier("jdbc") final Dao dao)
	{
		this.dao = dao;
		this.NEW_CARDS_CACHE = new Cache2kBuilder<String, BanListNewContent>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onNewContentCacheMiss)
			.build();
		this.REMOVED_CARDS_CACHE = new Cache2kBuilder<String, BanListRemovedContent>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.permitNullValues(false)
			.loader(this::onRemovedContentCacheMiss)
			.build();
	}




	public BanListNewContent getNewContentOfBanList(final String banListStartDate)
		throws YgoException
	{

		return NEW_CARDS_CACHE.get(banListStartDate);

	}



	private BanListNewContent onNewContentCacheMiss(final String banListStartDate)
		throws YgoException
	{

		log.info("Ban list new content w/ start date: ( {} ) not found in cache. Using DB.", banListStartDate);
		final NewCards newCards = NewCards.builder()
				.forbidden(dao.getNewContentOfBanList(banListStartDate, Status.FORBIDDEN))
				.limited(dao.getNewContentOfBanList(banListStartDate, Status.LIMITED))
				.semiLimited(dao.getNewContentOfBanList(banListStartDate, Status.SEMI_LIMITED))
				.build();

		if ( newCards.getForbidden().isEmpty()|| newCards.getLimited().isEmpty() || newCards.getSemiLimited().isEmpty() )
			throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE, banListStartDate));


		return BanListNewContent.builder()
			.listRequested(banListStartDate)
			.comparedTo(this.getPreviousBanListDate(banListStartDate))
			.newCards(newCards)
			.build();

	}



	public BanListRemovedContent getRemovedContentOfBanList(final String banListStartDate) throws YgoException
	{

		return REMOVED_CARDS_CACHE.get(banListStartDate);

	}



	private BanListRemovedContent onRemovedContentCacheMiss(final String banListStartDate)
		throws YgoException
	{

		log.info("Ban list removed content w/ start date: ( {} ) not found in cache. Using DB.", banListStartDate);
		final BanListRemovedContent removedCardsMeta = BanListRemovedContent.builder()
			.listRequested(banListStartDate)
			.comparedTo(this.getPreviousBanListDate(banListStartDate))
			.removedCards(dao.getRemovedContentOfBanList(banListStartDate))
			.build();

		if (removedCardsMeta.getRemovedCards().isEmpty())
			throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format(ErrConstants.NO_REMOVED_BAN_LIST_CONTENT_FOR_START_DATE, banListStartDate));


		return removedCardsMeta;

	}



	private String getPreviousBanListDate(final String banList)	{ return dao.getPreviousBanListDate(banList); }
}