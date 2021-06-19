package com.rtomyj.skc.service.banlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rtomyj.skc.dao.database.Dao;
import com.rtomyj.skc.model.banlist.BanListDate;
import com.rtomyj.skc.model.banlist.BanListDates;

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

	private static BanListDates banListDatesInstance;
	private static Date testBanListDate;


	@BeforeAll
	public static void before() throws Exception
	{
		final SimpleDateFormat banListSimpleDate = new SimpleDateFormat("yyyy-mm-dd");
		testBanListDate = banListSimpleDate.parse("2020-01-20");

		final List<BanListDate> banListDates = new ArrayList<>();
		banListDates.add(BanListDate
				.builder()
				.effectiveDate(testBanListDate)
				.build());


		banListDatesInstance = BanListDates
			.builder()
			.banListDates(banListDates)
			.build();
	}



	@Test
	public void testFetchingAllBanListDates()
	{
		when(dao.getBanListDates())
			.thenReturn(BanServiceTest.banListDatesInstance);

		final BanListDates banListDates = banService.getBanListStartDates();

		assertEquals(1, banListDates.getBanListDates().size());
	}
}