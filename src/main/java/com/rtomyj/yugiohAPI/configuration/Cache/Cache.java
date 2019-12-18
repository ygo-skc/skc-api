package com.rtomyj.yugiohAPI.configuration.cache;

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
	public Map<String, Map<String, Map<String, List<Card>>>> geBanListCardsCache()	{ return new HashMap<String, Map<String, Map<String, List<Card>>>>(); }

	/**
	 *
	 * @return
	 */
	@Bean(name = "banListStartDatesCache")
	public Map<String, List<BanLists>> getBanListStartDatesCache()	{ return new HashMap<String, List<BanLists>>(); }
	//private static final Map<String, Card> CARD_CACHE = new HashMap<>();

	/**
	 *
	 * @return
	 */
	@Bean(name = "cardsCache")
	public Map<String, Card> getCardsCache()	{ return new HashMap<String, Card>(); }

	@Bean(name = "banListNewCardsCache")
	public Map<String, Map<String, Object>> getBanListNewCardsCache()	{ return new HashMap<String, Map<String, Object>>(); }

}