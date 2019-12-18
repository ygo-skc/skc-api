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



@Configuration
public class PurgeCache
{
	@Autowired
	@Qualifier("banListCardsCache")
	private Map<String, Map<String, Map<String, List<Card>>>> BAN_LIST_CARDS_CACHE;

	@Autowired
	@Qualifier("banListStartDatesCache")
	private Map<String, List<BanLists>> BAN_LISTS_START_DATES_CACHE;

	@Autowired
	@Qualifier("cardsCache")
	private Map<String, Card> CARD_CACHE;

	@Autowired
	@Qualifier("banListNewCardsCache")
	private Map<String, Map<String, Object>> BAN_LISTS_NEW_CARDS_CACHE;


	private static final Logger LOG = LogManager.getLogger();



	@Scheduled(fixedRate = 1000 * 60 * 60)
	public void invalidateCache()
	{
		LOG.info("Purging caches");
		BAN_LIST_CARDS_CACHE.clear();
		BAN_LISTS_START_DATES_CACHE.clear();
		CARD_CACHE.clear();
		BAN_LISTS_NEW_CARDS_CACHE.clear();
	}
}