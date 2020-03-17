package com.rtomyj.yugiohAPI.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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


@ExtendWith(SpringExtension.class)
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

		assertEquals(TestConstants.WRONG_CARD_ID_MESSAGE, testCardId, card.getCardID());
		assertEquals(TestConstants.WRONG_CARD_NAME_MESSAGE, testCardName, card.getCardName());


		verify(dao, times(1)).getCardInfo(eq(testCardId));
	}



	@Test
	public void testFetchingCardFromDB_Failure() throws YgoException
	{
		when(dao.getCardInfo(eq(testCardId)))
			.thenThrow(new YgoException());


		assertThrows(YgoException.class, () -> cardService.getCardInfo(testCardId));


		verify(dao, times(1)).getCardInfo(eq(testCardId));
	}
}