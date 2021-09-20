package com.rtomyj.skc.service;

import com.rtomyj.skc.dao.database.Dao;
import com.rtomyj.skc.helper.exceptions.YgoException;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.service.card.CardService;
import org.cache2k.io.CacheLoaderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.rtomyj.skc.helper.constants.TestConstants.ID_THAT_CAUSES_FAILURE;
import static com.rtomyj.skc.helper.constants.TestConstants.STRATOS_ID;
import static com.rtomyj.skc.helper.constants.TestConstants.STRATOS_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CardService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)	// Re-creates DiffService which is needed since cache will have the card info after one of the tests executes, ruining other tests
public class CardServiceTest
{
	@MockBean(name = "jdbc")
	private Dao dao;

	@Autowired
	private CardService cardService;

	private static Card successfulCardReceived;

	@BeforeAll
	public static void before() {
		successfulCardReceived = Card
			.builder()
			.cardID(STRATOS_ID)
			.cardName(STRATOS_NAME)
			.build();
	}


	@Test
	public void testFetchingCard_FromDB_Success()
	{
		when(dao.getCardInfo(eq(STRATOS_ID)))
			.thenReturn(successfulCardReceived);


		final Card card = cardService.getCardInfo(STRATOS_ID, false);

		assertEquals(STRATOS_ID, card.getCardID());
		assertEquals(STRATOS_NAME, card.getCardName());


		verify(dao, times(1)).getCardInfo(eq(STRATOS_ID));
	}


	@Test
	public void testFetchingCardFromDB_Failure()
	{
		when(dao.getCardInfo(eq(ID_THAT_CAUSES_FAILURE)))
			.thenThrow(new YgoException());

		assertThrows(CacheLoaderException.class, () -> cardService.getCardInfo(ID_THAT_CAUSES_FAILURE, false));

		verify(dao, times(1)).getCardInfo(eq(ID_THAT_CAUSES_FAILURE));
	}
}