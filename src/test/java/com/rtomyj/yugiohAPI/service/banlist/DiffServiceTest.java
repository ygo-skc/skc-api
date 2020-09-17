package com.rtomyj.yugiohAPI.service.banlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.rtomyj.yugiohAPI.model.banlist.CardsPreviousBanListStatus;
import com.rtomyj.yugiohAPI.model.banlist.BanListNewContent;
import com.rtomyj.yugiohAPI.model.banlist.BanListRemovedContent;

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
public class DiffServiceTest {
	@InjectMocks
	private DiffService diffService;

	@Mock
	private Dao dao;

	private final static String BAN_LIST_START_DATE = "2018-12-03";
	private final static String PREVIOUS_BAN_LIST_START_DATE = "2018-09-17";

	private static BanListNewContent banListNewContent;
	private static BanListRemovedContent banListRemovedContent;

	@BeforeAll
	public static void before() throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();

		banListNewContent = mapper.readValue(new File(TestConstants.BAN_LIST_NEW_CONTENT), BanListNewContent.class);
		banListRemovedContent = mapper.readValue(new File(TestConstants.BAN_LIST_REMOVED_CONTENT),
				BanListRemovedContent.class);
	}



	@Test
	public void testFetchingBanListNewContent_FromDB_Success()
		throws YgoException
	{
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN)))
			.thenReturn(DiffServiceTest.banListNewContent.getNewForbidden());
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED)))
			.thenReturn(DiffServiceTest.banListNewContent.getNewLimited());
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED)))
			.thenReturn(DiffServiceTest.banListNewContent.getNewSemiLimited());
		when(this.dao.isValidBanList(eq(BAN_LIST_START_DATE)))
			.thenReturn(true);
		when(this.dao.getPreviousBanListDate(eq(BAN_LIST_START_DATE)))
			.thenReturn(PREVIOUS_BAN_LIST_START_DATE);


		final BanListNewContent banListNewContentInstance = this.diffService.getNewContentOfBanList(BAN_LIST_START_DATE);
		final List<CardsPreviousBanListStatus> newForbiddenCards = banListNewContentInstance.getNewForbidden();
		final List<CardsPreviousBanListStatus> newLimitedCards = banListNewContentInstance.getNewLimited();
		final List<CardsPreviousBanListStatus> newSemiLimitedCards = banListNewContentInstance.getNewSemiLimited();

		assertEquals(BAN_LIST_START_DATE, banListNewContentInstance.getListRequested());
		assertEquals(PREVIOUS_BAN_LIST_START_DATE, banListNewContentInstance.getComparedTo());

		assertNotNull(newForbiddenCards);
		assertNotNull(newLimitedCards);
		assertNotNull(newSemiLimitedCards);
		assertEquals(1, newForbiddenCards.size());
		assertEquals(1, newLimitedCards.size());
		assertEquals(1, newSemiLimitedCards.size());

		assertEquals(TestConstants.STRATOS_ID, newForbiddenCards.get(0).getCardId());
		assertEquals("Limited", newForbiddenCards.get(0).getPreviousBanStatus());

		assertEquals(TestConstants.A_HERO_LIVES_ID, newLimitedCards.get(0).getCardId());
		assertEquals("Unlimited", newLimitedCards.get(0).getPreviousBanStatus());

		assertEquals(TestConstants.D_MALICIOUS_ID, newSemiLimitedCards.get(0).getCardId());
		assertEquals("Forbidden", newSemiLimitedCards.get(0).getPreviousBanStatus());


		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN));
		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED));
		verify(this.dao, times(1))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED));
		verify(this.dao, times(1))
			.isValidBanList(eq(BAN_LIST_START_DATE));
		verify(this.dao, times(1))
			.getPreviousBanListDate(eq(BAN_LIST_START_DATE));
	}



	@Test
	public void testFetchingBanListNewContent_FromDB_Failure()
		throws YgoException
	{
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN)))
			.thenReturn(new ArrayList<>());
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED)))
			.thenReturn(new ArrayList<>());
		when(this.dao.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED)))
			.thenReturn(new ArrayList<>());


		assertThrows(CacheLoaderException.class, () -> this.diffService.getNewContentOfBanList(BAN_LIST_START_DATE));


		verify(this.dao, times(0))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.FORBIDDEN));
		verify(this.dao, times(0))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.LIMITED));
		verify(this.dao, times(0))
			.getNewContentOfBanList(eq(BAN_LIST_START_DATE), eq(Status.SEMI_LIMITED));
		verify(this.dao, times(1))
			.isValidBanList(eq(BAN_LIST_START_DATE));
		verify(this.dao, times(0))
			.getPreviousBanListDate(any(String.class));
	}



	@Test
	public void testFetchingBanListRemovedContent_FromDB_Success()
		throws YgoException
	{
		when(this.dao.getRemovedContentOfBanList(eq(BAN_LIST_START_DATE)))
			.thenReturn(banListRemovedContent.getRemovedCards());
		when(this.dao.getPreviousBanListDate(eq(BAN_LIST_START_DATE)))
			.thenReturn(PREVIOUS_BAN_LIST_START_DATE);
		when(this.dao.isValidBanList(eq(BAN_LIST_START_DATE)))
			.thenReturn(true);


		final BanListRemovedContent banListRemovedContentInstance = this.diffService.getRemovedContentOfBanList(BAN_LIST_START_DATE);

		final List<CardsPreviousBanListStatus> removedCards = banListRemovedContentInstance.getRemovedCards();

		assertNotNull(removedCards);

		assertEquals(BAN_LIST_START_DATE, banListRemovedContentInstance.getListRequested());
		assertEquals(PREVIOUS_BAN_LIST_START_DATE, banListRemovedContentInstance.getComparedTo());

		assertEquals(3, removedCards.size());

		assertEquals(TestConstants.STRATOS_ID, removedCards.get(0).getCardId());
		assertEquals("Forbidden", removedCards.get(0).getPreviousBanStatus());

		assertEquals(TestConstants.A_HERO_LIVES_ID, removedCards.get(1).getCardId());
		assertEquals("Limited", removedCards.get(1).getPreviousBanStatus());

		assertEquals(TestConstants.D_MALICIOUS_ID, removedCards.get(2).getCardId());
		assertEquals("Semi-Limited", removedCards.get(2).getPreviousBanStatus());


		verify(this.dao, times(1))
			.getRemovedContentOfBanList(eq(BAN_LIST_START_DATE));
		verify(this.dao, times(1))
			.getPreviousBanListDate(eq(BAN_LIST_START_DATE));
		verify(this.dao, times(1))
			.isValidBanList(eq(BAN_LIST_START_DATE));
	}



	@Test
	public void testFetchingBanListRemovedContent_FromDB_Failure() throws YgoException

	{
		when(this.dao.isValidBanList(eq(BAN_LIST_START_DATE)))
			.thenReturn(false);
		when(this.dao.getRemovedContentOfBanList(eq(BAN_LIST_START_DATE)))
			.thenReturn(new ArrayList<>());


		assertThrows(CacheLoaderException.class, () -> this.diffService.getRemovedContentOfBanList(BAN_LIST_START_DATE));


		verify(this.dao, times(1))
			.isValidBanList(eq(BAN_LIST_START_DATE));
		verify(this.dao, times(0))
			.getRemovedContentOfBanList(eq(BAN_LIST_START_DATE));
		verify(this.dao, times(0))
			.getPreviousBanListDate(eq(BAN_LIST_START_DATE));
	}
}