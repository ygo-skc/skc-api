package com.rtomyj.skc.service.banlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.skc.dao.database.Dao;
import com.rtomyj.skc.dao.database.Dao.Status;
import com.rtomyj.skc.helper.constants.TestConstants;
import com.rtomyj.skc.helper.exceptions.YgoException;
import com.rtomyj.skc.model.banlist.BanListInstance;
import com.rtomyj.skc.model.card.Card;
import org.cache2k.io.CacheLoaderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.rtomyj.skc.helper.constants.TestConstants.BAN_LIST_START_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BannedCardsService.class, DiffService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)	// Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
public class BannedCardsServiceTest {
	@MockBean(name = "jdbc")
	private Dao dao;

	@Autowired
	private DiffService diffService;	// injected in BannedCardsService

	@Autowired
	private BannedCardsService bannedCardsService;

	private static BanListInstance banListInstanceFullText;
	

	// TODO: add ban list assertion that checks if forbidden, limited, etc length matches the length/num field for each associated list
	@BeforeAll
	public static void before() throws IOException
	{
		final ObjectMapper mapper = new ObjectMapper();
		banListInstanceFullText = mapper.readValue(
			new File(TestConstants.BAN_LIST_INSTANCE_JSON_FILE), BanListInstance.class);
	}



	@Test
	public void testFetchingBanListInstance_FromDB_WithFullText_Successfully() throws YgoException
	{
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN)))
			.thenReturn(banListInstanceFullText.getForbidden());
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.LIMITED)))
			.thenReturn(banListInstanceFullText.getLimited());
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED)))
			.thenReturn(banListInstanceFullText.getSemiLimited());


		final BanListInstance banListInstance = bannedCardsService.getBanListByBanStatus(BAN_LIST_START_DATE, false, false);

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


		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN));
		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED));
	}



	@Test
	public void testFetchingBanListInstance_FromDB_WithTrimmedText_Successfully() throws YgoException
	{
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN)))
			.thenReturn(banListInstanceFullText.getForbidden());
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.LIMITED)))
			.thenReturn(banListInstanceFullText.getLimited());
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED)))
			.thenReturn(banListInstanceFullText.getSemiLimited());


		final BanListInstance banListInstance = bannedCardsService.getBanListByBanStatus(BAN_LIST_START_DATE, true, false);

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


		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN));
		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED));
	}



	@Test
	public void testFetchingBanListInstance_FromDB_WithFullText_Failure() throws YgoException
	{
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN)))
			.thenReturn(new ArrayList<>());
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.LIMITED)))
			.thenReturn(new ArrayList<>());
		when(dao.getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED)))
			.thenReturn(new ArrayList<>());

		assertThrows(CacheLoaderException.class, () -> bannedCardsService.getBanListByBanStatus(BAN_LIST_START_DATE, false, false));

		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN));
		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.LIMITED));
		verify(dao, times(1)).getBanListByBanStatus(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED));
	}
}