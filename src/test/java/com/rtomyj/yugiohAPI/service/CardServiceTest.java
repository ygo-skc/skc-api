package com.rtomyj.yugiohAPI.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
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
public class CardServiceTest
{
	@InjectMocks
	private CardService cardService;

	@Mock
	private Dao dao;

	private static Card successfulCardReceived;
	private static final String testCardId = "12345678";
	private static final String testCardName = "E-HERO Stratos";

	@BeforeAll
	public static void before() {
		successfulCardReceived = Card
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

		assertEquals(testCardId, card.getCardID());
		assertEquals(testCardName, card.getCardName());


		verify(dao, times(1)).getCardInfo(eq(testCardId));
	}



	@Test
	public void testFetchingCardFromDB_Failure() throws YgoException
	{
		when(dao.getCardInfo(eq(testCardId)))
			.thenThrow(new YgoException());


		assertThrows(CacheLoaderException.class, () -> cardService.getCardInfo(testCardId));


		verify(dao, times(1)).getCardInfo(eq(testCardId));
	}
}