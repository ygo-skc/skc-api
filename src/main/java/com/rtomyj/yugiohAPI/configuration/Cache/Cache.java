package com.rtomyj.yugiohAPI.configuration.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rtomyj.yugiohAPI.model.BanListInstance;
import com.rtomyj.yugiohAPI.model.BanListNewContent;
import com.rtomyj.yugiohAPI.model.BanListRemovedContent;
import com.rtomyj.yugiohAPI.model.BanListStartDates;
import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class for creating beans for the purpose of caching data produced by requests.
 *
 * For example, if a user requested ban list X then the cache will remember the request and save the data for the ban list. Then, when other users
 * also request the same ban list, it is retrieved from the cache for faster delivery.
 */
@Configuration
public class Cache
{
	/**
	 * Creates a bean for caching cards in ban lists.
	 * @return The cache.
	 */
	@Bean(name = "banListCardsCache")
	public Map<String, BanListInstance>  geBanListCardsCache()	{ return new HashMap<String, BanListInstance> (); }

	/**
	 * Creates a bean for caching cards in ban lists with some trimmed text (in card effect for example) to prevent using a lot of bandwidth
	 * @return The cache.
	 */
	@Bean(name = "banListCardsCacheLowBandwidth")
	public Map<String, BanListInstance>  geBanListCardsLowBandwidthCache()	{ return new HashMap<String, BanListInstance> (); }

	/**
	 *	Creates a bean for caching dates of ban lists.
	 * @return The cache.
	 */
	@Bean(name = "banListStartDatesCache")
	public BanListStartDates getBanListStartDatesCache()
	{
		BanListStartDates banListStartDates = new BanListStartDates();
		banListStartDates.setBanListStartDates(new ArrayList());
		return banListStartDates;
	}
	//private static final Map<String, Card> CARD_CACHE = new HashMap<>();

	/**
	 * Creates a bean for caching card information.
	 * @return The cache.
	 */
	@Bean(name = "cardsCache")
	public Map<String, Card> getCardsCache()	{ return new HashMap<String, Card>(); }

	/**
	 * Creates a bean for caching new cards added to a ban list.
	 * @return The cache.
	 */
	@Bean(name = "banListNewCardsCache")
	public Map<String, BanListNewContent> getBanListNewCardsCache()	{ return new HashMap<String, BanListNewContent>(); }

	/**
	 * Creates a bean for caching new cards added to a ban list.
	 * @return The cache.
	 */
	@Bean(name = "banListRemovedCardsCache")
	public Map<String, BanListRemovedContent> getBanListRemovedCardsCache()	{ return new HashMap<String,BanListRemovedContent>(); }

}