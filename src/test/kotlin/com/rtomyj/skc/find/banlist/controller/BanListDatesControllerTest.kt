package com.rtomyj.skc.find.banlist.controller

import com.rtomyj.skc.banlist.controller.BanListDatesController
import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.util.constant.ErrConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.banlist.model.BanListDate
import com.rtomyj.skc.banlist.model.BanListDates
import com.rtomyj.skc.banlist.service.BanListDatesService
import com.rtomyj.skc.testingutil.ControllerTestUtil
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat

@WebMvcTest(BanListDatesController::class, DateConfig::class)
@Tag("Controller")
class BanListDatesControllerTest {
    @MockBean
    private lateinit var banListDatesService: BanListDatesService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var banListDateFormat: SimpleDateFormat
    
    
    companion object {
        const val BAN_LIST_DATES_ENDPOINT = "/ban_list/dates"
    }


    @Nested
    inner class HappyPath {
        @Test
        fun `Getting Ban List Dates Using Controller, DB Has Databases - Success`() {
            `when`(banListDatesService.retrieveBanListStartDates())
                .thenReturn(
                    BanListDates(getMockBanListDates())
                )


            // call endpoint to retrieve ban list dates
            mockMvc
                .perform(get(BAN_LIST_DATES_ENDPOINT))
                .andExpect(status().isOk)
                .andExpect(
                    jsonPath("$.banListDates.length()", `is`(2))
                )
                .andExpect(
                    jsonPath("$.banListDates[0].effectiveDate", `is`("2021-07-01"))
                )
                .andExpect(
                    jsonPath("$.banListDates[1].effectiveDate", `is`("2021-04-14"))
                )


            // ensure methods are called correct number of times
            verify(banListDatesService)
                .retrieveBanListStartDates()
        }


        @Test
        fun `Getting Ban List Dates Using Controller, But There Are No Ban List Dates To Return - Success`() {
            // mock retrieval of ban list dates - return an empty array (na dates found in DB)
            `when`(banListDatesService.retrieveBanListStartDates())
                .thenReturn(
                    BanListDates(emptyList())
                )


            // call endpoint to retrieve ban list dates
            mockMvc
                .perform(get(BAN_LIST_DATES_ENDPOINT))
                .andExpect(status().isOk)


            // ensure methods are called correct number of times
            verify(banListDatesService)
                .retrieveBanListStartDates()
        }
    }
    
    
    @Nested
    inner class UnhappyPath {
        @Test
        fun `Getting Ban List Dates Using Controller, But The Ban List Table Isn't Setup - Server Error`() {
            // mock retrieval of ban list dates - error occurred - table ban list table DNE
            `when`(banListDatesService.retrieveBanListStartDates())
                .thenThrow(
                    YgoException(ErrConstants.DB_MISSING_TABLE, ErrorType.D002)
                )


            // call endpoint to retrieve ban list dates
            ControllerTestUtil.validateSTableNotCreatedHelper(
                mockMvc.perform(get(BAN_LIST_DATES_ENDPOINT))
            )


            // ensure methods are called correct number of times
            verify(banListDatesService)
                .retrieveBanListStartDates()
        }
    }


    /**
     * Helper method used to create a list of BanListDate objects.
     */
    private fun getMockBanListDates(): List<BanListDate> = listOf(
        BanListDate(banListDateFormat.parse("2021-07-01"))
        , BanListDate(banListDateFormat.parse("2021-04-14"))
    )
}