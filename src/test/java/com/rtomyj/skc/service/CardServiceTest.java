package com.rtomyj.skc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rtomyj.skc.dao.database.Dao;
import com.rtomyj.skc.helper.exceptions.YgoException;
import com.rtomyj.skc.model.card.Card;

import com.rtomyj.skc.service.card.CardService;
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


		final Card card = cardService.getCardInfo(testCardId, false);

		assertEquals(testCardId, card.getCardID());
		assertEquals(testCardName, card.getCardName());


		verify(dao, times(1)).getCardInfo(eq(testCardId));
	}



	@Test
	public void testFetchingCardFromDB_Failure() throws YgoException
	{
		when(dao.getCardInfo(eq(testCardId)))
			.thenThrow(new YgoException());


		assertThrows(CacheLoaderException.class, () -> cardService.getCardInfo(testCardId, false));


		verify(dao, times(1)).getCardInfo(eq(testCardId));
	}
}