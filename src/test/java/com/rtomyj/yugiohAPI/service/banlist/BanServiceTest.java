package com.rtomyj.yugiohAPI.service.banlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.banlist.CardBanListStatus;
import com.rtomyj.yugiohAPI.model.banlist.BanListDates;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BanServiceTest
{
	@InjectMocks
	private BanService banService;

	@Mock
	private Dao dao;

	private static BanListDates banListDates;
	private static Date testBanListDate;


	@BeforeAll
	public static void before() throws Exception
	{
		final SimpleDateFormat banListSimpleDate = new SimpleDateFormat("yyyy-mm-dd");
		testBanListDate = banListSimpleDate.parse("2020-01-20");

		final CardBanListStatus cardBanListStatus = CardBanListStatus
			.builder()
			.banListDate(testBanListDate)
			.build();

		final List<CardBanListStatus> cardBanListStatuses = new ArrayList<>();
		cardBanListStatuses.add(cardBanListStatus);


		banListDates = BanListDates
			.builder()
			.cardBanListStatusStartDates(cardBanListStatuses)
			.build();
	}



	@Test
	public void testFetchingAllBanListDates()
	{
		when(dao.getBanListDates())
			.thenReturn(BanServiceTest.banListDates);

		final BanListDates banListDates = banService.getBanListStartDates();

		assertEquals(1, banListDates.getCardBanListStatusStartDates().size());
	}
}