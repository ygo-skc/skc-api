package com.rtomyj.skc.find

import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.model.BanListDate
import com.rtomyj.skc.model.BanListDates
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [BanListDatesService::class, DateConfig::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Re-creates DiffService which is needed since cache will have the ban list info after one of the tests executes, ruining other tests
class BanListDatesServiceTest {
  @MockBean(name = "ban-list-hibernate")
  private lateinit var banListDao: BanListDao

  @Autowired
  private lateinit var banService: BanListDatesService

  private val banListDatesInstance: BanListDates


  init {
    val testBanListDate = LocalDate.parse("2020-01-20", DateConfig().dbDateTimeFormatter())
    val banListDates = listOf(
      BanListDate(testBanListDate)
    )

    banListDatesInstance = BanListDates(banListDates)
  }


  @Nested
  inner class HappyPath {
    @Test
    fun `Test Fetching All Ban List Dates`() {
      // setup mocks
      Mockito
          .`when`(banListDao.getBanListDates("TCG"))
          .thenReturn(banListDatesInstance)


      // call code to test
      StepVerifier
          .create(
            banService.retrieveBanListStartDates("TCG")
          )
          .assertNext { banListDates ->

            Assertions.assertNotNull(banListDates)
            Assertions.assertNotNull(banListDates.dates)

            val dates = banListDates.dates

            // assertions on returned value
            Assertions.assertEquals(1, dates.size)
          }
          .verifyComplete()


      // verify mocks are called the correct number of times
      Mockito
          .verify(banListDao, Mockito.times(1))
          .getBanListDates("TCG")
    }
  }
}