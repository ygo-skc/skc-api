package com.rtomyj.yugiohAPI.service.banlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;
import com.rtomyj.yugiohAPI.helper.constants.TestConstants;
import com.rtomyj.yugiohAPI.model.BanListInstance;
import com.rtomyj.yugiohAPI.model.Card;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CardsServiceTest
{
	@InjectMocks
	private CardsService cardsService;

	@Mock
	private Dao dao;

	@Mock
	private Map<String, BanListInstance>  BAN_LIST_CARDS_CACHE;

	@Mock
	private Map<String, BanListInstance>  BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;


	private BanListInstance cachedInstanceWithFullCardEffect;
	private BanListInstance cachedInstanceWithTrimmedCardEffect;

	private final String banListStartDate = "2019-07-15";

	private final List<Card> forbiddenFullEffect = new ArrayList<>();
	private final List<Card> limitedFullEffect = new ArrayList<>();
	private final List<Card> semiLimitedFullEffect = new ArrayList<>();

	private final List<Card> forbiddenTrimmedEffect = new ArrayList<>();
	private final List<Card> limitedTrimmedEffect = new ArrayList<>();
	private final List<Card> semiLimitedTrimmedEffect = new ArrayList<>();

	private Card forbiddenCardWithFullEffect;
	private final String forbiddenCardId = TestConstants.A_HERO_LIVES_ID;
	private final String forbiddenCardName = TestConstants.A_HERO_LIVES_NAME;
	private final String forbiddenCardFullEffect = TestConstants.A_HERO_LIVES_FULL_EFFECT;
	private final String forbiddenCardTrimmedEffect = TestConstants.A_HERO_LIVES_TRIMMED_EFFECT;

	private Card limitedCardWithFullEffect;
	private final String limitedCardId = TestConstants.STRATOS_ID;
	private final String limitedCardName = TestConstants.STRATOS_NAME;
	private final String limitedCardFullEffect = TestConstants.STRATOS_FULL_EFFECT;
	private final String limitedCardTrimmedEffect = TestConstants.A_HERO_LIVES_NAME;

	private Card semiLimitedCardWithFullEffect;
	private final String semiLimitedCardId = TestConstants.D_MALICIOUS_ID;
	private final String semiLimitedCardName = TestConstants.D_MALICIOUS_NAME;
	private final String semiLimitedCardFullEffect = TestConstants.D_MALICIOUS_FULL_EFFECT;
	private final String semiLimitedCardTrimmedEffect = TestConstants.D_MALICIOUS_TRIMMED_EFFECT;


	@Before
	public void before()
	{
		this.forbiddenCardWithFullEffect = Card
			.builder()
			.cardID(forbiddenCardId)
			.cardName(forbiddenCardName)
			.cardEffect(forbiddenCardFullEffect)
			.build();

		this.limitedCardWithFullEffect = Card
			.builder()
			.cardID(limitedCardId)
			.cardName(limitedCardName)
			.cardEffect(limitedCardFullEffect)
			.build();

		this.semiLimitedCardWithFullEffect = Card
			.builder()
			.cardID(semiLimitedCardId)
			.cardName(semiLimitedCardName)
			.cardEffect(semiLimitedCardFullEffect)
		.build();

		this.forbiddenFullEffect.add(forbiddenCardWithFullEffect);
		this.limitedFullEffect.add(limitedCardWithFullEffect);
		this.semiLimitedFullEffect.add(semiLimitedCardWithFullEffect);

		/*
			Create forbidden, limited, and semi-limited card lists with trimmed card effects using previously defined
			lists that contain full card effects as a base. The only field being changed will be the card effect as the trimmed
			effect is needed. All other fields can stay the same.
		*/
		this.forbiddenTrimmedEffect.add(forbiddenCardWithFullEffect.withCardEffect(forbiddenCardTrimmedEffect));
		this.limitedTrimmedEffect.add(limitedCardWithFullEffect.withCardEffect(limitedCardTrimmedEffect));
		this.semiLimitedTrimmedEffect.add(semiLimitedCardWithFullEffect.withCardEffect(semiLimitedCardTrimmedEffect));

		this.cachedInstanceWithFullCardEffect = BanListInstance
			.builder()
			.forbidden(forbiddenFullEffect)
			.limited(limitedFullEffect)
			.semiLimited(semiLimitedFullEffect)
			.build();

		this.cachedInstanceWithTrimmedCardEffect = BanListInstance
			.builder()
			.forbidden(forbiddenTrimmedEffect)
			.limited(limitedTrimmedEffect)
			.semiLimited(semiLimitedTrimmedEffect)
			.build();
	}



	@Test
	public void testFetchingBanListInstance_FromDB_WithFullText_Successfully() throws YgoException
	{
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN)))
			.thenReturn(forbiddenFullEffect);
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED)))
			.thenReturn(limitedFullEffect);
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED)))
			.thenReturn(semiLimitedFullEffect);
		when(BAN_LIST_CARDS_CACHE.get(eq(banListStartDate)))
			.thenReturn(null);


		final ServiceLayerHelper serviceLayerHelper = cardsService.getBanListByBanStatus(banListStartDate, false);
		final BanListInstance banListInstance = (BanListInstance) serviceLayerHelper.getRequestedResource();
		final List<Card> forbidden = banListInstance.getForbidden();
		final List<Card> limited = banListInstance.getLimited();
		final List<Card> semiLimited = banListInstance.getSemiLimited();

		assertNotEquals(null, serviceLayerHelper);
		assertNotEquals(null, banListInstance);
		assertNotEquals(null, forbidden);
		assertNotEquals(null, limited);
		assertNotEquals(null, semiLimited);

		assertEquals(1, forbidden.size());
		assertEquals(1, limited.size());
		assertEquals(1, semiLimited.size());

		assertEquals(forbiddenCardId, forbidden.get(0).getCardID());
		assertEquals(forbiddenCardName, forbidden.get(0).getCardName());
		assertEquals(forbiddenCardFullEffect, forbidden.get(0).getCardEffect());

		assertEquals(limitedCardId, limited.get(0).getCardID());
		assertEquals(limitedCardName, limited.get(0).getCardName());
		assertEquals(limitedCardFullEffect, limited.get(0).getCardEffect());

		assertEquals(semiLimitedCardId, semiLimited.get(0).getCardID());
		assertEquals(semiLimitedCardName, semiLimited.get(0).getCardName());
		assertEquals(semiLimitedCardFullEffect, semiLimited.get(0).getCardEffect());

		assertEquals(HttpStatus.OK, serviceLayerHelper.getStatus());
		assertFalse(serviceLayerHelper.getInCache());
		assertTrue(serviceLayerHelper.getIsContentReturned());


		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
		verify(BAN_LIST_CARDS_CACHE, times(1)).get(eq(banListStartDate));
		verify(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE, times(0)).get(any(String.class));
	}



	@Test
	public void testFetchingBanListInstance_FromCache_WithFullText_Successfully() throws YgoException
	{
		when(BAN_LIST_CARDS_CACHE.get(eq(banListStartDate)))
			.thenReturn(cachedInstanceWithFullCardEffect);


		final ServiceLayerHelper serviceLayerHelper = cardsService.getBanListByBanStatus(banListStartDate, false);
		final BanListInstance banListInstance = (BanListInstance) serviceLayerHelper.getRequestedResource();
		final List<Card> forbidden = banListInstance.getForbidden();
		final List<Card> limited = banListInstance.getLimited();
		final List<Card> semiLimited = banListInstance.getSemiLimited();

		assertNotEquals(null, serviceLayerHelper);
		assertNotEquals(null, banListInstance);
		assertNotEquals(null, forbidden);
		assertNotEquals(null, limited);
		assertNotEquals(null, semiLimited);

		assertEquals(1, forbidden.size());
		assertEquals(1, limited.size());
		assertEquals(1, semiLimited.size());

		assertEquals(forbiddenCardId, forbidden.get(0).getCardID());
		assertEquals(forbiddenCardName, forbidden.get(0).getCardName());
		assertEquals(forbiddenCardFullEffect, forbidden.get(0).getCardEffect());

		assertEquals(limitedCardId, limited.get(0).getCardID());
		assertEquals(limitedCardName, limited.get(0).getCardName());
		assertEquals(limitedCardFullEffect, limited.get(0).getCardEffect());

		assertEquals(semiLimitedCardId, semiLimited.get(0).getCardID());
		assertEquals(semiLimitedCardName, semiLimited.get(0).getCardName());
		assertEquals(semiLimitedCardFullEffect, semiLimited.get(0).getCardEffect());

		assertEquals(HttpStatus.OK, serviceLayerHelper.getStatus());
		assertTrue(serviceLayerHelper.getInCache());
		assertTrue(serviceLayerHelper.getIsContentReturned());


		verify(dao, times(0)).getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN));
		verify(dao, times(0)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(0)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
		verify(BAN_LIST_CARDS_CACHE, times(1)).get(eq(banListStartDate));
		verify(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE, times(0)).get(any(String.class));
	}



	@Test
	public void testFetchingBanListInstance_FromDB_WithTrimmedText_Successfully() throws YgoException
	{
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN)))
			.thenReturn(forbiddenTrimmedEffect);
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED)))
			.thenReturn(limitedTrimmedEffect);
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED)))
			.thenReturn(semiLimitedTrimmedEffect);
		when(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE.get(eq(banListStartDate)))
			.thenReturn(null);


		final ServiceLayerHelper serviceLayerHelper = cardsService.getBanListByBanStatus(banListStartDate, true);
		final BanListInstance banListInstance = (BanListInstance) serviceLayerHelper.getRequestedResource();
		final List<Card> forbidden = banListInstance.getForbidden();
		final List<Card> limited = banListInstance.getLimited();
		final List<Card> semiLimited = banListInstance.getSemiLimited();

		assertNotEquals(null, serviceLayerHelper);
		assertNotEquals(null, banListInstance);
		assertNotEquals(null, forbidden);
		assertNotEquals(null, limited);
		assertNotEquals(null, semiLimited);

		assertEquals(1, forbidden.size());
		assertEquals(1, limited.size());
		assertEquals(1, semiLimited.size());

		assertEquals(forbiddenCardId, forbidden.get(0).getCardID());
		assertEquals(forbiddenCardName, forbidden.get(0).getCardName());
		assertEquals(forbiddenCardTrimmedEffect, forbidden.get(0).getCardEffect());

		assertEquals(limitedCardId, limited.get(0).getCardID());
		assertEquals(limitedCardName, limited.get(0).getCardName());
		assertEquals(limitedCardTrimmedEffect, limited.get(0).getCardEffect());

		assertEquals(semiLimitedCardId, semiLimited.get(0).getCardID());
		assertEquals(semiLimitedCardName, semiLimited.get(0).getCardName());
		assertEquals(semiLimitedCardTrimmedEffect, semiLimited.get(0).getCardEffect());

		assertEquals(HttpStatus.OK, serviceLayerHelper.getStatus());
		assertFalse(serviceLayerHelper.getInCache());
		assertTrue(serviceLayerHelper.getIsContentReturned());


		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
		verify(BAN_LIST_CARDS_CACHE, times(0)).get(eq(banListStartDate));
		verify(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE, times(1)).get(any(String.class));
	}



	@Test
	public void testFetchingBanListInstance_FromCache_WithTrimmedText_Successfully() throws YgoException
	{
		when(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE.get(eq(banListStartDate)))
			.thenReturn(cachedInstanceWithTrimmedCardEffect);


		final ServiceLayerHelper serviceLayerHelper = cardsService.getBanListByBanStatus(banListStartDate, true);
		final BanListInstance banListInstance = (BanListInstance) serviceLayerHelper.getRequestedResource();
		final List<Card> forbidden = banListInstance.getForbidden();
		final List<Card> limited = banListInstance.getLimited();
		final List<Card> semiLimited = banListInstance.getSemiLimited();

		assertNotEquals(null, serviceLayerHelper);
		assertNotEquals(null, banListInstance);
		assertNotEquals(null, forbidden);
		assertNotEquals(null, limited);
		assertNotEquals(null, semiLimited);

		assertEquals(1, forbidden.size());
		assertEquals(1, limited.size());
		assertEquals(1, semiLimited.size());

		assertEquals(forbiddenCardId, forbidden.get(0).getCardID());
		assertEquals(forbiddenCardName, forbidden.get(0).getCardName());
		assertEquals(forbiddenCardTrimmedEffect, forbidden.get(0).getCardEffect());

		assertEquals(limitedCardId, limited.get(0).getCardID());
		assertEquals(limitedCardName, limited.get(0).getCardName());
		assertEquals(limitedCardTrimmedEffect, limited.get(0).getCardEffect());

		assertEquals(semiLimitedCardId, semiLimited.get(0).getCardID());
		assertEquals(semiLimitedCardName, semiLimited.get(0).getCardName());
		assertEquals(semiLimitedCardTrimmedEffect, semiLimited.get(0).getCardEffect());

		assertEquals(HttpStatus.OK, serviceLayerHelper.getStatus());
		assertTrue(serviceLayerHelper.getInCache());
		assertTrue(serviceLayerHelper.getIsContentReturned());


		verify(dao, times(0)).getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN));
		verify(dao, times(0)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(0)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
		verify(BAN_LIST_CARDS_CACHE, times(0)).get(eq(banListStartDate));
		verify(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE, times(1)).get(any(String.class));
	}



	@Test(expected = YgoException.class)
	public void testFetchingBanListInstance_FromDB_WithFullText_Failure() throws YgoException
	{
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN)))
			.thenReturn(new ArrayList<>());
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED)))
			.thenReturn(new ArrayList<>());
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED)))
			.thenReturn(new ArrayList<>());
		when(BAN_LIST_CARDS_CACHE.get(eq(banListStartDate)))
			.thenReturn(null);


		final ServiceLayerHelper serviceLayerHelper = cardsService.getBanListByBanStatus(banListStartDate, false);

		assertEquals(null, serviceLayerHelper);


		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN));
		verify(dao, times(0)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(0)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
		verify(BAN_LIST_CARDS_CACHE, times(1)).get(eq(banListStartDate));
		verify(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE, times(0)).get(any(String.class));
	}
}