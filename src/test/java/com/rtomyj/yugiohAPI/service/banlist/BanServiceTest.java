package com.rtomyj.yugiohAPI.service.banlist;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.BanList;
import com.rtomyj.yugiohAPI.model.BanListStartDates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BanServiceTest
{
	@InjectMocks
	private BanService banService;

	@Mock
	private Dao dao;

	private BanListStartDates banListStartDates;
	private Date testBanListDate;


	@Before
	public void before() throws Exception
	{
		final SimpleDateFormat banListSimpleDate = new SimpleDateFormat("yyyy-mm-dd");
		testBanListDate = banListSimpleDate.parse("2020-01-20");

		final BanList banList = BanList
			.builder()
			.banListDate(this.testBanListDate)
			.build();

		final List<BanList> banLists = new ArrayList<>();
		banLists.add(banList);


		this.banListStartDates = BanListStartDates
			.builder()
			.banListStartDates(banLists)
			.build();
	}



	@Test
	public void testFetchingAllBanListDates()
	{
		when(dao.getBanListStartDates())
			.thenReturn(banListStartDates);

		final BanListStartDates banListStartDates = banService.getBanListStartDates();

		assertEquals(1, banListStartDates.getBanListStartDates().size());
	}
}