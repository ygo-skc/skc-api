package com.rtomyj.yugiohAPI.service.banlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.constants.TestConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.BanListInstance;
import com.rtomyj.yugiohAPI.model.Card;

import org.cache2k.integration.CacheLoaderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CardsServiceTest {
	@InjectMocks
	private CardsService cardsService;

	@Mock
	private Dao dao;

	private static BanListInstance banListInstanceFullText;
	//private static BanListInstance banListInstanceTrimmedText;

	private final String banListStartDate = "2019-07-15";



	@BeforeAll
	public static void before() throws JsonParseException, JsonMappingException, IOException
	{
		final ObjectMapper mapper = new ObjectMapper();
		banListInstanceFullText = mapper.readValue(
			new File(TestConstants.BAN_LIST_INSTANCE_JSON_FILE), BanListInstance.class);
		// banListInstanceTrimmedText = mapper.readValue(
		// 	new File(TestConstants.BAN_LIST_INSTANCE_LOW_BANDWIDTH_JSON_FILE), BanListInstance.class);
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


		final BanListInstance banListInstance = cardsService.getBanListByBanStatus(banListStartDate, false);

		final List<Card> forbidden = banListInstance.getForbidden();
		final List<Card> limited = banListInstance.getLimited();
		final List<Card> semiLimited = banListInstance.getSemiLimited();


		assertNotNull(banListInstance);
		assertNotNull(forbidden);
		assertNotNull(limited);
		assertNotNull(semiLimited);

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


		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
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


		final BanListInstance banListInstance = cardsService.getBanListByBanStatus(banListStartDate, true);

		final List<Card> forbiddenTrimmed = banListInstance.getForbidden();
		final List<Card> limitedTrimmed = banListInstance.getLimited();
		final List<Card> semiLimitedTrimmed = banListInstance.getSemiLimited();

		assertNotNull(banListInstance);
		assertNotNull(forbiddenTrimmed);
		assertNotNull(limitedTrimmed);
		assertNotNull(semiLimitedTrimmed);

		assertEquals(1, forbiddenTrimmed.size());
		assertEquals(1, limitedTrimmed.size());
		assertEquals(1, semiLimitedTrimmed.size());

		assertEquals(TestConstants.STRATOS_ID, forbiddenTrimmed.get(0).getCardID());
		assertEquals(TestConstants.STRATOS_NAME, forbiddenTrimmed.get(0).getCardName());
		assertEquals(Card.trimEffect(TestConstants.STRATOS_FULL_EFFECT), forbiddenTrimmed.get(0).getCardEffect());

		assertEquals(TestConstants.A_HERO_LIVES_ID, limitedTrimmed.get(0).getCardID());
		assertEquals(TestConstants.A_HERO_LIVES_NAME, limitedTrimmed.get(0).getCardName());
		assertEquals(Card.trimEffect(TestConstants.A_HERO_LIVES_FULL_EFFECT), limitedTrimmed.get(0).getCardEffect());

		assertEquals(TestConstants.D_MALICIOUS_ID, semiLimitedTrimmed.get(0).getCardID());
		assertEquals(TestConstants.D_MALICIOUS_NAME, semiLimitedTrimmed.get(0).getCardName());
		assertEquals(Card.trimEffect(TestConstants.D_MALICIOUS_FULL_EFFECT), semiLimitedTrimmed.get(0).getCardEffect());


		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
	}



	@Test
	public void testFetchingBanListInstance_FromDB_WithFullText_Failure() throws YgoException
	{
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN)))
			.thenReturn(new ArrayList<>());
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED)))
			.thenReturn(new ArrayList<>());
		when(dao.getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED)))
			.thenReturn(new ArrayList<>());


		assertThrows(CacheLoaderException.class, () -> cardsService.getBanListByBanStatus(banListStartDate, false));


		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.FORBIDDEN));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(banListStartDate), eq(Status.SEMI_LIMITED));
	}
}