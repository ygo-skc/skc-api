package com.rtomyj.skc.service.banlist;

import com.rtomyj.skc.dao.database.Dao;
import com.rtomyj.skc.model.banlist.BanListDate;
import com.rtomyj.skc.model.banlist.BanListDates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BanService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)	// Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
public class BanServiceTest
{
	@MockBean(name = "hibernate")
	private Dao dao;

	@Autowired
	private BanService banService;

	private static BanListDates banListDatesInstance;


	@BeforeAll
	public static void before() throws Exception
	{
		final SimpleDateFormat banListSimpleDate = new SimpleDateFormat("yyyy-MM-dd");
		final Date testBanListDate = banListSimpleDate.parse("2020-01-20");

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