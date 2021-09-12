package com.rtomyj.skc.service.banlist

import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.dao.database.Dao
import com.rtomyj.skc.model.banlist.BanListDate
import com.rtomyj.skc.model.banlist.BanListDates
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [BanListDatesService::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
class BanListDatesServiceTest {
    @MockBean(name = "hibernate")
    private lateinit var dao: Dao

    @Autowired
    private lateinit var banService: BanListDatesService


    companion object {
        private lateinit var banListDatesInstance: BanListDates


        @BeforeAll
        @JvmStatic
        fun before() {
            val banListSimpleDate = DateConfig.getDBSimpleDateFormat()
            val testBanListDate = banListSimpleDate.parse("2020-01-20")
            val banListDates = listOf(
                BanListDate
                    .builder()
                    .effectiveDate(testBanListDate)
                    .build()
            )

            banListDatesInstance = BanListDates
                .builder()
                .banListDates(banListDates)
                .build()
        }
    }


    @Test
    fun testFetchingAllBanListDates() {
        // setup mocks
        Mockito.`when`(dao.banListDates)
            .thenReturn(banListDatesInstance)
        

        // call code to test
        val banListDates = banService.banListStartDates


        // assertions on returned value
        Assertions.assertEquals(1, banListDates.banListDates.size)

        Assertions.assertNotNull(banListDates.banListDates[0].links)

        Assertions.assertNotNull(banListDates.banListDates[0].links.getLink("Ban List Content"))
        Assertions.assertEquals("/ban_list/2020-01-20/cards?saveBandwidth=false&allInfo=true"
            , banListDates.banListDates[0].links.getLink("Ban List Content").get().href)

        Assertions.assertNotNull(banListDates.banListDates[0].links.getLink("Ban List New Content"))
        Assertions.assertEquals("/ban_list/2020-01-20/new"
            , banListDates.banListDates[0].links.getLink("Ban List New Content").get().href)

        Assertions.assertNotNull(banListDates.banListDates[0].links.getLink("Ban List Removed Content"))
        Assertions.assertEquals("/ban_list/2020-01-20/removed"
            , banListDates.banListDates[0].links.getLink("Ban List Removed Content").get().href)


        // verify mocks are called the correct number of times
        Mockito.verify(dao, Mockito.times(1))
            .banListDates
    }
}