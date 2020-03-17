package com.rtomyj.yugiohAPI.service.banlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;
import com.rtomyj.yugiohAPI.model.BanList;
import com.rtomyj.yugiohAPI.model.BanListStartDates;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BanServiceTest
{
	@InjectMocks
	private BanService banService;

	@Mock
	private Dao dao;

	private static BanListStartDates banListStartDates;
	private static Date testBanListDate;


	@BeforeAll
	public static void before() throws Exception
	{
		final SimpleDateFormat banListSimpleDate = new SimpleDateFormat("yyyy-mm-dd");
		testBanListDate = banListSimpleDate.parse("2020-01-20");

		final BanList banList = BanList
			.builder()
			.banListDate(testBanListDate)
			.build();

		final List<BanList> banLists = new ArrayList<>();
		banLists.add(banList);


		banListStartDates = BanListStartDates
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

    
		assertEquals(HttpStatus.OK, serviceLayerHelper.getStatus());
		assertEquals(1, banListStartDates.getBanListStartDates().size());
	}
}