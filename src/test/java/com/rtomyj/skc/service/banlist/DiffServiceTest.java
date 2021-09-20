package com.rtomyj.skc.service.banlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.skc.dao.database.Dao;
import com.rtomyj.skc.dao.database.Dao.Status;
import com.rtomyj.skc.helper.constants.TestConstants;
import com.rtomyj.skc.helper.exceptions.YgoException;
import com.rtomyj.skc.model.banlist.BanListNewContent;
import com.rtomyj.skc.model.banlist.BanListRemovedContent;
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus;
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
import static com.rtomyj.skc.helper.constants.TestConstants.PREVIOUS_BAN_LIST_START_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DiffService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)	// Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
public class DiffServiceTest {
	@MockBean(name = "jdbc")
	private Dao dao;

	@Autowired
	private DiffService diffService;

	private static BanListNewContent banListNewContent;
	private static BanListRemovedContent banListRemovedContent;

	@BeforeAll
	public static void before() throws IOException {
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