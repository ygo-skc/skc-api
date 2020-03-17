package com.rtomyj.yugiohAPI.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.constants.TestConstants;
import com.rtomyj.yugiohAPI.model.Card;

import org.cache2k.integration.CacheLoaderException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CardServiceTest
{
	@InjectMocks
	private CardService cardService;

	@Mock
	private Dao dao;

	private Card successfulCardReceived;
	private final String testCardId = "12345678";
	private final String testCardName = "E-HERO Stratos";



	@Before
	public void before()
	{
		this.successfulCardReceived = Card
			.builder()
			.cardID(testCardId)
			.cardName(testCardName)
			.build();
	}



	@Test
	public void testFetchingCard_FromDB_Success()
		throws YgoException
	{
		when(dao.getCardInfo(eq(testCardId)))
			.thenReturn(successfulCardReceived);


		final Card card = cardService.getCardInfo(testCardId);

		assertEquals(TestConstants.WRONG_CARD_ID_MESSAGE, testCardId, card.getCardID());
		assertEquals(TestConstants.WRONG_CARD_NAME_MESSAGE, testCardName, card.getCardName());


		verify(dao, times(1)).getCardInfo(eq(testCardId));
	}



	@Test(expected = CacheLoaderException.class)
	public void testFetchingCardFromDB_Failure()
		throws YgoException
	{
		when(dao.getCardInfo(eq(testCardId)))
			.thenThrow(new YgoException());


		final Card card = cardService.getCardInfo(testCardId);

		assertEquals(null, card);


		verify(dao, times(1)).getCardInfo(eq(testCardId));
	}
}