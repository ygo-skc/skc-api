package com.rtomyj.yugiohAPI.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;
import com.rtomyj.yugiohAPI.model.Card;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CardServiceTest
{
	@InjectMocks
	private CardService cardService;

	@Mock
	private Dao dao;

	@Mock
	private Map<String, Card> cardCache;

	private static Card successfulCardReceived;
	private final static String testCardId = "12345678";
	private final static String testCardName = "E-HERO Stratos";

	@BeforeAll
	public static void before() {
		successfulCardReceived = Card
			.builder()
			.cardID(testCardId)
			.cardName(testCardName)
			.build();
	}



	@Test
	public void testFetchingCard_FromDB_Success() throws YgoException
	{
		when(dao.getCardInfo(eq(testCardId)))
			.thenReturn(successfulCardReceived);
		when(cardCache.get(eq(testCardId)))
			.thenReturn(null);


		final ServiceLayerHelper serviceLayerHelper = cardService.getCardInfo(testCardId);
		final Card card = (Card) serviceLayerHelper.getRequestedResource();

		assertEquals(false, serviceLayerHelper.getInCache());
		assertEquals(true, serviceLayerHelper.getIsContentReturned());
		assertEquals(HttpStatus.OK, serviceLayerHelper.getStatus());

		assertEquals(testCardId, card.getCardID());
		assertEquals(testCardName, card.getCardName());
		assertFalse(serviceLayerHelper.getInCache());
		assertTrue(serviceLayerHelper.getIsContentReturned());


		verify(dao, times(1)).getCardInfo(eq(testCardId));
		verify(cardCache, times(1)).get(eq(testCardId));
	}



	@Test
	public void testFetchingCard_FromCache_Success() throws YgoException
	{
		when(cardCache.get(eq(testCardId)))
			.thenReturn(successfulCardReceived);


		final ServiceLayerHelper serviceLayerHelper = cardService.getCardInfo(testCardId);
		final Card card = (Card) serviceLayerHelper.getRequestedResource();

		assertEquals(true, serviceLayerHelper.getInCache());
		assertEquals(true, serviceLayerHelper.getIsContentReturned());
		assertEquals(HttpStatus.OK, serviceLayerHelper.getStatus());

		assertEquals(testCardId, card.getCardID());
		assertEquals(testCardName, card.getCardName());
		assertEquals(HttpStatus.OK.toString(), serviceLayerHelper.getStatus().toString());
		assertTrue(serviceLayerHelper.getInCache());
		assertTrue(serviceLayerHelper.getIsContentReturned());


		verify(dao, times(0)).getCardInfo(eq(testCardId));
		verify(cardCache, times(1)).get(eq(testCardId));
	}



	@Test
	public void testFetchingCardFromDB_Failure() throws YgoException
	{
		when(dao.getCardInfo(eq(testCardId)))
			.thenThrow(new YgoException());
		when(cardCache.get(eq(testCardId)))
			.thenReturn(null);


		assertThrows(YgoException.class, () -> cardService.getCardInfo(testCardId));


		verify(dao, times(1)).getCardInfo(eq(testCardId));
		verify(cardCache, times(1)).get(eq(testCardId));
	}
}