package com.rtomyj.skc.find

import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListDate
import com.rtomyj.skc.model.BanListDates
import com.rtomyj.skc.testingutil.ControllerTestUtil
import com.rtomyj.skc.util.constant.ErrConstants
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@WebFluxTest(BanListDatesController::class, DateConfig::class)
@Tag("Controller")
class BanListDatesControllerTest {
  @MockitoBean
  private lateinit var banListDatesService: BanListDatesService

  @Autowired
  private lateinit var mockMvc: WebTestClient

  @Autowired
  private lateinit var banListDateFormat: DateTimeFormatter


  companion object {
    const val BAN_LIST_DATES_ENDPOINT = "/ban_list/dates"
  }


  @Nested
  inner class HappyPath {
    @Test
    fun `Getting Ban List Dates Using Controller, DB Has Databases - Success`() {
      `when`(banListDatesService.retrieveBanListStartDates("TCG")).thenReturn(Mono.just(BanListDates(getMockBanListDates())))


      // call endpoint to retrieve ban list dates
      mockMvc
          .get()
          .uri(BAN_LIST_DATES_ENDPOINT)
          .exchange()
          .expectStatus()
          .isOk()
          .expectBody()
          .jsonPath("$.banListDates.length()")
          .isEqualTo(2)
          .jsonPath("$.banListDates[0].effectiveDate")
          .isEqualTo("2021-07-01")
          .jsonPath("$.banListDates[1].effectiveDate")
          .isEqualTo("2021-04-14")


      // ensure methods are called correct number of times
      verify(banListDatesService).retrieveBanListStartDates("TCG")
    }


    @Test
    fun `Getting Ban List Dates Using Controller, But There Are No Ban List Dates To Return - Success`() {
      // mock retrieval of ban list dates - return an empty array (na dates found in DB)
      `when`(banListDatesService.retrieveBanListStartDates("TCG")).thenReturn(
        Mono.just(BanListDates(emptyList()))
      )


      // call endpoint to retrieve ban list dates
      mockMvc
          .get()
          .uri(BAN_LIST_DATES_ENDPOINT)
          .exchange()
          .expectStatus().isOk


      // ensure methods are called correct number of times
      verify(banListDatesService).retrieveBanListStartDates("TCG")
    }
  }


  @Nested
  inner class UnhappyPath {
    @Test
    fun `Getting Ban List Dates Using Controller, But The Ban List Table Isn't Setup - Server Error`() {
      // mock retrieval of ban list dates - error occurred - table ban list table DNE
      `when`(banListDatesService.retrieveBanListStartDates("TCG")).thenThrow(
        SKCException(ErrConstants.DB_MISSING_TABLE, ErrorType.DB002)
      )


      // call endpoint to retrieve ban list dates
      ControllerTestUtil.validateErrorByErrorType(
        mockMvc
            .get()
            .uri(BAN_LIST_DATES_ENDPOINT)
            .exchange(), 500, ErrorType.DB002
      )


      // ensure methods are called correct number of times
      verify(banListDatesService).retrieveBanListStartDates("TCG")
    }
  }


  /**
   * Helper method used to create a list of BanListDate objects.
   */
  private fun getMockBanListDates(): List<BanListDate> = listOf(
    BanListDate(LocalDate.parse("2021-07-01", banListDateFormat)),
    BanListDate(LocalDate.parse("2021-04-14", banListDateFormat))
  )
}