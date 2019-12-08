package com.rtomyj.yugiohAPI.configuration.Cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Cache
{
	/**
	 *
	 */
	@Bean(name = "banListCardsCache")
	public Map<String, Map<String, Map<String, List<Card>>>> geBanListCardsCache()
	{
		Map<String, Map<String, Map<String, List<Card>>>> BAN_LIST_CARDS_CACHE = new HashMap<>();
		return BAN_LIST_CARDS_CACHE;
	}



	/**
	 *
	 * @return
	 */
	@Bean(name = "banListStartDatesCache")
	public Map<String, List<BanLists>> getBanListStartDatesCache()
	{
		Map<String, List<BanLists>> BAN_LISTS_START_DATES_CACHE = new HashMap<>();
		return BAN_LISTS_START_DATES_CACHE;
	}
	//private static final Map<String, Card> CARD_CACHE = new HashMap<>();



	/**
	 *
	 * @return
	 */
	@Bean(name = "cardsCache")
	public Map<String, Card> getCardsCache()
	{
		Map<String, Card> CARD_CACHE = new HashMap<>();
		return CARD_CACHE;
	}
}