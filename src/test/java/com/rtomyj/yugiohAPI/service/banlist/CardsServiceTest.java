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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CardsServiceTest {
	@InjectMocks
	private CardsService cardsService;

	@Mock
	private Dao dao;

	@Mock
	private Map<String, BanListInstance> BAN_LIST_CARDS_CACHE;

	@Mock
	private Map<String, BanListInstance> BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;

	private BanListInstance banListInstanceFullText;
	private BanListInstance banListInstanceTrimmedText;

	private final String banListStartDate = "2019-07-15";



	@Before
	public void before() throws JsonParseException, JsonMappingException, IOException
	{
		final ObjectMapper mapper = new ObjectMapper();
		this.banListInstanceFullText = mapper.readValue(
			new File(TestConstants.BAN_LIST_INSTANCE_JSON_FILE), BanListInstance.class);
		this.banListInstanceTrimmedText = mapper.readValue(
			new File(TestConstants.BAN_LIST_INSTANCE_LOW_BANDWIDTH_JSON_FILE), BanListInstance.class);
	}



	@Test
	public void testFetchingBanListInstance_FromDB_WithFullText_Successfully() throws YgoException
	{
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN)))
			.thenReturn(banListInstanceFullText.getForbidden());
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED)))
			.thenReturn(banListInstanceFullText.getLimited());
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED)))
			.thenReturn(banListInstanceFullText.getSemiLimited());
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

		assertEquals(TestConstants.STRATOS_ID, forbidden.get(0).getCardID());
		assertEquals(TestConstants.STRATOS_NAME, forbidden.get(0).getCardName());
		assertEquals(TestConstants.STRATOS_FULL_EFFECT, forbidden.get(0).getCardEffect());

		assertEquals(TestConstants.A_HERO_LIVES_ID, limited.get(0).getCardID());
		assertEquals(TestConstants.A_HERO_LIVES_NAME, limited.get(0).getCardName());
		assertEquals(TestConstants.A_HERO_LIVES_FULL_EFFECT, limited.get(0).getCardEffect());

		assertEquals(TestConstants.D_MALICIOUS_ID, semiLimited.get(0).getCardID());
		assertEquals(TestConstants.D_MALICIOUS_NAME, semiLimited.get(0).getCardName());
		assertEquals(TestConstants.D_MALICIOUS_FULL_EFFECT, semiLimited.get(0).getCardEffect());

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
			.thenReturn(banListInstanceFullText);


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

		assertEquals(TestConstants.STRATOS_ID, forbidden.get(0).getCardID());
		assertEquals(TestConstants.STRATOS_NAME, forbidden.get(0).getCardName());
		assertEquals(TestConstants.STRATOS_FULL_EFFECT, forbidden.get(0).getCardEffect());

		assertEquals(TestConstants.A_HERO_LIVES_ID, limited.get(0).getCardID());
		assertEquals(TestConstants.A_HERO_LIVES_NAME, limited.get(0).getCardName());
		assertEquals(TestConstants.A_HERO_LIVES_FULL_EFFECT, limited.get(0).getCardEffect());

		assertEquals(TestConstants.D_MALICIOUS_ID, semiLimited.get(0).getCardID());
		assertEquals(TestConstants.D_MALICIOUS_NAME, semiLimited.get(0).getCardName());
		assertEquals(TestConstants.D_MALICIOUS_FULL_EFFECT, semiLimited.get(0).getCardEffect());

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
			.thenReturn(banListInstanceFullText.getForbidden());
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED)))
			.thenReturn(banListInstanceFullText.getLimited());
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED)))
			.thenReturn(banListInstanceFullText.getSemiLimited());
		when(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE.get(eq(banListStartDate)))
			.thenReturn(null);


		final ServiceLayerHelper serviceLayerHelper = cardsService.getBanListByBanStatus(banListStartDate, true);
		final BanListInstance banListInstance = (BanListInstance) serviceLayerHelper.getRequestedResource();

		final List<Card> forbiddenTrimmed = banListInstance.getForbidden();
		final List<Card> limitedTrimmed = banListInstance.getLimited();
		final List<Card> semiLimitedTrimmed = banListInstance.getSemiLimited();

		assertNotEquals(null, serviceLayerHelper);
		assertNotEquals(null, banListInstance);
		assertNotEquals(null, forbiddenTrimmed);
		assertNotEquals(null, limitedTrimmed);
		assertNotEquals(null, semiLimitedTrimmed);

		assertEquals(1, forbiddenTrimmed.size());
		assertEquals(1, limitedTrimmed.size());
		assertEquals(1, semiLimitedTrimmed.size());

		assertEquals(TestConstants.STRATOS_ID, forbiddenTrimmed.get(0).getCardID());
		assertEquals(TestConstants.STRATOS_NAME, forbiddenTrimmed.get(0).getCardName());
		assertEquals(TestConstants.STRATOS_TRIMMED_EFFECT, forbiddenTrimmed.get(0).getCardEffect());

		assertEquals(TestConstants.A_HERO_LIVES_ID, limitedTrimmed.get(0).getCardID());
		assertEquals(TestConstants.A_HERO_LIVES_NAME, limitedTrimmed.get(0).getCardName());
		assertEquals(TestConstants.A_HERO_LIVES_TRIMMED_EFFECT, limitedTrimmed.get(0).getCardEffect());

		assertEquals(TestConstants.D_MALICIOUS_ID, semiLimitedTrimmed.get(0).getCardID());
		assertEquals(TestConstants.D_MALICIOUS_NAME, semiLimitedTrimmed.get(0).getCardName());
		assertEquals(TestConstants.D_MALICIOUS_TRIMMED_EFFECT, semiLimitedTrimmed.get(0).getCardEffect());

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
			.thenReturn(banListInstanceTrimmedText);


		final ServiceLayerHelper serviceLayerHelper = cardsService.getBanListByBanStatus(banListStartDate, true);
		final BanListInstance banListInstance = (BanListInstance) serviceLayerHelper.getRequestedResource();

		final List<Card> forbiddenTrimmed = banListInstance.getForbidden();
		final List<Card> limitedTrimmed = banListInstance.getLimited();
		final List<Card> semiLimitedTrimmed = banListInstance.getSemiLimited();

		assertNotEquals(null, serviceLayerHelper);
		assertNotEquals(null, banListInstance);
		assertNotEquals(null, forbiddenTrimmed);
		assertNotEquals(null, limitedTrimmed);
		assertNotEquals(null, semiLimitedTrimmed);

		assertEquals(1, forbiddenTrimmed.size());
		assertEquals(1, limitedTrimmed.size());
		assertEquals(1, semiLimitedTrimmed.size());

		assertEquals(TestConstants.STRATOS_ID, forbiddenTrimmed.get(0).getCardID());
		assertEquals(TestConstants.STRATOS_NAME, forbiddenTrimmed.get(0).getCardName());
		assertEquals(TestConstants.STRATOS_TRIMMED_EFFECT, forbiddenTrimmed.get(0).getCardEffect());

		assertEquals(TestConstants.A_HERO_LIVES_ID, limitedTrimmed.get(0).getCardID());
		assertEquals(TestConstants.A_HERO_LIVES_NAME, limitedTrimmed.get(0).getCardName());
		assertEquals(TestConstants.A_HERO_LIVES_TRIMMED_EFFECT, limitedTrimmed.get(0).getCardEffect());

		assertEquals(TestConstants.D_MALICIOUS_ID, semiLimitedTrimmed.get(0).getCardID());
		assertEquals(TestConstants.D_MALICIOUS_NAME, semiLimitedTrimmed.get(0).getCardName());
		assertEquals(TestConstants.D_MALICIOUS_TRIMMED_EFFECT, semiLimitedTrimmed.get(0).getCardEffect());

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
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
		verify(BAN_LIST_CARDS_CACHE, times(1)).get(eq(banListStartDate));
		verify(BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE, times(0)).get(any(String.class));
	}
}