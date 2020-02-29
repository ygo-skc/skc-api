package com.rtomyj.yugiohAPI.service.banlist;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.rtomyj.yugiohAPI.model.BanListComparisonResults;
import com.rtomyj.yugiohAPI.model.BanListInstance;
import com.rtomyj.yugiohAPI.model.BanListNewContent;
import com.rtomyj.yugiohAPI.model.NewCards;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffServiceTest {
	@InjectMocks
	private DiffService diffService;

	@Mock
	private Dao dao;

	@Mock
	private Map<String, BanListNewContent> NEW_CARDS_CACHE;

	@Mock
	private Map<String, BanListInstance> REMOVED_CARDS_CACHE;

	private final static String BAN_LIST_START_DATE = "2018-12-03";
	private final static String PREVIOUS_BAN_LIST_START_DATE = "2018-09-17";

	private BanListNewContent banListNewContent;



	@Before
	public void before() throws JsonParseException, JsonMappingException, IOException
	{
		final ObjectMapper mapper = new ObjectMapper();
		this.banListNewContent = mapper.readValue(new File(TestConstants.BAN_LIST_NEW_CONTENT), BanListNewContent.class);
	}



	@Test
	public void testFetchingBanListNewContent_FromDB_Successfully() throws YgoException
	{
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN)))
			.thenReturn(this.banListNewContent.getNewCards().getForbidden());
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED)))
			.thenReturn(this.banListNewContent.getNewCards().getLimited());
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED)))
			.thenReturn(this.banListNewContent.getNewCards().getSemiLimited());
		when(this.dao.getPreviousBanListDate(eq(BAN_LIST_START_DATE)))
			.thenReturn(PREVIOUS_BAN_LIST_START_DATE);
		when(this.NEW_CARDS_CACHE.get(eq(BAN_LIST_START_DATE)))
			.thenReturn(null);


		final ServiceLayerHelper serviceLayerHelper = this.diffService.getNewContentOfBanList(BAN_LIST_START_DATE);
		final BanListNewContent banListNewContentInstance = (BanListNewContent) serviceLayerHelper.getRequestedResource();
		final NewCards newCards = banListNewContentInstance.getNewCards();
		final List<BanListComparisonResults> newForbiddenCards = newCards.getForbidden();
		final List<BanListComparisonResults> newLimitedCards = newCards.getLimited();
		final List<BanListComparisonResults> newSemiLimitedCards = newCards.getSemiLimited();

		assertNotNull(serviceLayerHelper);
		assertNotNull(banListNewContentInstance);
		assertNotNull(newCards);

		assertEquals(BAN_LIST_START_DATE, banListNewContentInstance.getListRequested());
		assertEquals(PREVIOUS_BAN_LIST_START_DATE, banListNewContentInstance.getComparedTo());

		assertNotNull(newForbiddenCards);
		assertNotNull(newLimitedCards);
		assertNotNull(newSemiLimitedCards);
		assertEquals(1, newForbiddenCards.size());
		assertEquals(1, newLimitedCards.size());
		assertEquals(1, newSemiLimitedCards.size());

		assertEquals(TestConstants.STRATOS_ID, newForbiddenCards.get(0).getId());
		assertEquals("Limited", newForbiddenCards.get(0).getPreviousState());

		assertEquals(TestConstants.A_HERO_LIVES_ID, newLimitedCards.get(0).getId());
		assertEquals("Unlimited", newLimitedCards.get(0).getPreviousState());

		assertEquals(TestConstants.D_MALICIOUS_ID, newSemiLimitedCards.get(0).getId());
		assertEquals("Forbidden", newSemiLimitedCards.get(0).getPreviousState());


		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN));
		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED));
		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED));
		verify(this.dao, times(1))
			.getPreviousBanListDate(eq(BAN_LIST_START_DATE));
		verify(this.NEW_CARDS_CACHE, times(1))
			.get(eq(BAN_LIST_START_DATE));
	}



	@Test
	public void testFetchingBanListNewContent_FromCache_Successfully() throws YgoException
	{
		when(this.NEW_CARDS_CACHE.get(eq(BAN_LIST_START_DATE)))
			.thenReturn(this.banListNewContent);


		final ServiceLayerHelper serviceLayerHelper = this.diffService.getNewContentOfBanList(BAN_LIST_START_DATE);
		final BanListNewContent banListNewContentInstance = (BanListNewContent) serviceLayerHelper.getRequestedResource();
		final NewCards newCards = banListNewContentInstance.getNewCards();
		final List<BanListComparisonResults> newForbiddenCards = newCards.getForbidden();
		final List<BanListComparisonResults> newLimitedCards = newCards.getLimited();
		final List<BanListComparisonResults> newSemiLimitedCards = newCards.getSemiLimited();

		assertNotNull(serviceLayerHelper);
		assertNotNull(banListNewContentInstance);
		assertNotNull(newCards);

		assertEquals(BAN_LIST_START_DATE, banListNewContentInstance.getListRequested());
		assertEquals(PREVIOUS_BAN_LIST_START_DATE, banListNewContentInstance.getComparedTo());

		assertNotNull(newForbiddenCards);
		assertNotNull(newLimitedCards);
		assertNotNull(newSemiLimitedCards);
		assertEquals(1, newForbiddenCards.size());
		assertEquals(1, newLimitedCards.size());
		assertEquals(1, newSemiLimitedCards.size());

		assertEquals(TestConstants.STRATOS_ID, newForbiddenCards.get(0).getId());
		assertEquals("Limited", newForbiddenCards.get(0).getPreviousState());

		assertEquals(TestConstants.A_HERO_LIVES_ID, newLimitedCards.get(0).getId());
		assertEquals("Unlimited", newLimitedCards.get(0).getPreviousState());

		assertEquals(TestConstants.D_MALICIOUS_ID, newSemiLimitedCards.get(0).getId());
		assertEquals("Forbidden", newSemiLimitedCards.get(0).getPreviousState());


		verify(this.dao, times(0))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN));
		verify(this.dao, times(0))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED));
		verify(this.dao, times(0))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED));
		verify(this.dao, times(0))
			.getPreviousBanListDate(eq(BAN_LIST_START_DATE));
		verify(this.NEW_CARDS_CACHE, times(1))
			.get(eq(BAN_LIST_START_DATE));
	}



	@Test(expected = YgoException.class)
	public void testFetchingBanListNewContent_FromDB_Failure() throws YgoException
	{
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN)))
			.thenReturn(new ArrayList<>());
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED)))
			.thenReturn(new ArrayList<>());
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED)))
			.thenReturn(new ArrayList<>());
		when(this.NEW_CARDS_CACHE.get(eq(BAN_LIST_START_DATE)))
			.thenReturn(null);


		final ServiceLayerHelper serviceLayerHelper = this.diffService.getNewContentOfBanList(BAN_LIST_START_DATE);
		final BanListNewContent banListNewContentInstance = (BanListNewContent) serviceLayerHelper.getRequestedResource();
		final NewCards newCards = banListNewContentInstance.getNewCards();
		final List<BanListComparisonResults> newForbiddenCards = newCards.getForbidden();
		final List<BanListComparisonResults> newLimitedCards = newCards.getLimited();
		final List<BanListComparisonResults> newSemiLimitedCards = newCards.getSemiLimited();

		assertNotNull(serviceLayerHelper);
		assertNotNull(banListNewContentInstance);
		assertNotNull(newCards);

		assertEquals(BAN_LIST_START_DATE, banListNewContentInstance.getListRequested());
		assertEquals(PREVIOUS_BAN_LIST_START_DATE, banListNewContentInstance.getComparedTo());

		assertNotNull(newForbiddenCards);
		assertNotNull(newLimitedCards);
		assertNotNull(newSemiLimitedCards);
		assertEquals(1, newForbiddenCards.size());
		assertEquals(1, newLimitedCards.size());
		assertEquals(1, newSemiLimitedCards.size());

		assertEquals(TestConstants.STRATOS_ID, newForbiddenCards.get(0).getId());
		assertEquals("Limited", newForbiddenCards.get(0).getPreviousState());

		assertEquals(TestConstants.A_HERO_LIVES_ID, newLimitedCards.get(0).getId());
		assertEquals("Unlimited", newLimitedCards.get(0).getPreviousState());

		assertEquals(TestConstants.D_MALICIOUS_ID, newSemiLimitedCards.get(0).getId());
		assertEquals("Forbidden", newSemiLimitedCards.get(0).getPreviousState());


		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN));
		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED));
		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED));
		verify(this.dao, times(0))
			.getPreviousBanListDate(eq(BAN_LIST_START_DATE));
		verify(this.NEW_CARDS_CACHE, times(1))
			.get(eq(BAN_LIST_START_DATE));
	}
}