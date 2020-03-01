package com.rtomyj.yugiohAPI.configuration;

import java.util.concurrent.TimeUnit;

import com.rtomyj.yugiohAPI.model.BanListInstance;
import com.rtomyj.yugiohAPI.model.BanListNewContent;
import com.rtomyj.yugiohAPI.model.BanListRemovedContent;
import com.rtomyj.yugiohAPI.model.Card;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class for creating beans for the purpose of caching data produced by requests.
 *
 * For example, if a user requested ban list X then the cache will remember the request and save the data for the ban list. Then, when other users
 * also request the same ban list, it is retrieved from the cache for faster delivery.
 */
@Configuration
public class CacheConfig
{
	/**
	 * Creates a bean for caching cards in ban lists.
	 * @return The cache.
	 */
	@Bean(name = "banListCardsCache")
	public Cache<String, BanListInstance> geBanListCardsCache()
	{
		return new Cache2kBuilder<String, BanListInstance>() {}
			.expireAfterWrite(1, TimeUnit.DAYS)
			.entryCapacity(10)
			.build();
	}

	/**
	 * Creates a bean for caching cards in ban lists with some trimmed text (in card effect for example) to prevent using a lot of bandwidth
	 * @return The cache.
	 */
	@Bean(name = "banListCardsCacheLowBandwidth")
	public Cache<String, BanListInstance> geBanListCardsLowBandwidthCache()
	{
		return new Cache2kBuilder<String, BanListInstance>() {}
			.expireAfterWrite(1, TimeUnit.DAYS)
			.entryCapacity(10)
			.build();
	}

	/**
	 * Creates a bean for caching card information.
	 * @return The cache.
	 */
	@Bean(name = "cardsCache")
	public Cache<String, Card> getCardsCache()
	{
		return new Cache2kBuilder<String, Card>() {}
			.expireAfterWrite(7, TimeUnit.DAYS)
			.entryCapacity(1000)
			.build();
	}

	/**
	 * Creates a bean for caching new cards added to a ban list.
	 * @return The cache.
	 */
	@Bean(name = "banListNewCardsCache")
	public Cache<String, BanListNewContent> getBanListNewCardsCache()
	{
		return new Cache2kBuilder<String, BanListNewContent>() {}
			.expireAfterWrite(1, TimeUnit.DAYS)
			.entryCapacity(10)
			.build();
	}

	/**
	 * Creates a bean for caching new cards added to a ban list.
	 * @return The cache.
	 */
	@Bean(name = "banListRemovedCardsCache")
	public Cache<String, BanListRemovedContent> getBanListRemovedCardsCache()
	{
		return new Cache2kBuilder<String, BanListRemovedContent>() {}
			.expireAfterWrite(1, TimeUnit.DAYS)
			.entryCapacity(10)
			.build();
	}

}