package com.rtomyj.yugiohAPI.configuration.cache;

import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.model.Card;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Class used to remove content of cache in hopes of freeing memory when needed.
 */
@Configuration
public class PurgeCache
{
	/**
	 * Cache for cards of specific ban lists.
	 */
	@Autowired
	@Qualifier("banListCardsCache")
	private Map<String, Map<String, Map<String, List<Card>>>> BAN_LIST_CARDS_CACHE;

	/**
	 * Cache for dates of ban lists
	 */
	@Autowired
	@Qualifier("banListStartDatesCache")
	private Map<String, List<BanLists>> BAN_LISTS_START_DATES_CACHE;

	/**
	 * Cache for the information of specific cards.
	 */
	@Autowired
	@Qualifier("cardsCache")
	private Map<String, Card> CARD_CACHE;

	/**
	 * Cache for cards that are new to the ban list - either newly added in general or if a card switched statuses.
	 */
	@Autowired
	@Qualifier("banListNewCardsCache")
	private Map<String, Map<String, Object>> BAN_LISTS_NEW_CARDS_CACHE;

	/**
	 * Logging object.
	 */
	private static final Logger LOG = LogManager.getLogger();



	/**
	 * Clears caches on interval or when called.
	 */
	@Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 7)
	public void invalidateCache()
	{
		LOG.info("Purging caches");
		BAN_LIST_CARDS_CACHE.clear();
		BAN_LISTS_START_DATES_CACHE.clear();
		CARD_CACHE.clear();
		BAN_LISTS_NEW_CARDS_CACHE.clear();
	}
}