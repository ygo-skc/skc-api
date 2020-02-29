package com.rtomyj.yugiohAPI.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;
import com.rtomyj.yugiohAPI.helper.constants.TestConstants;
import com.rtomyj.yugiohAPI.model.Card;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CardServiceTest
{
	@InjectMocks
	private CardService cardService;

	@Mock
	private Dao dao;

	@Mock
	private Map<String, Card> cardCache;

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

		assertEquals(TestConstants.WRONG_CARD_ID_MESSAGE, testCardId, card.getCardID());
		assertEquals(TestConstants.WRONG_CARD_NAME_MESSAGE, testCardName, card.getCardName());
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

		assertEquals(TestConstants.WRONG_CARD_ID_MESSAGE, testCardId, card.getCardID());
		assertEquals(TestConstants.WRONG_CARD_NAME_MESSAGE, testCardName, card.getCardName());
		assertEquals(TestConstants.WRONG_HTTP_CODE_MESSAGE, HttpStatus.OK.toString(), serviceLayerHelper.getStatus().toString());
		assertTrue(serviceLayerHelper.getInCache());
		assertTrue(serviceLayerHelper.getIsContentReturned());


		verify(dao, times(0)).getCardInfo(eq(testCardId));
		verify(cardCache, times(1)).get(eq(testCardId));
	}



	@Test(expected = YgoException.class)
	public void testFetchingCardFromDB_Failure() throws YgoException
	{
		when(dao.getCardInfo(eq(testCardId)))
			.thenThrow(new YgoException());
		when(cardCache.get(eq(testCardId)))
			.thenReturn(null);


		final ServiceLayerHelper serviceLayerHelper = cardService.getCardInfo(testCardId);

		assertEquals(null, serviceLayerHelper);


		verify(dao, times(1)).getCardInfo(eq(testCardId));
		verify(cardCache, times(1)).get(eq(testCardId));
	}
}