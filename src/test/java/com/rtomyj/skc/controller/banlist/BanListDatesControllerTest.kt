package com.rtomyj.skc.controller.banlist

import com.rtomyj.skc.config.DateConfig
import com.rtomyj.skc.model.banlist.BanListDate
import com.rtomyj.skc.model.banlist.BanListDates
import com.rtomyj.skc.service.banlist.BanListDatesService
import org.hamcrest.Matchers
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat

@ExtendWith(SpringExtension::class)
@WebMvcTest(BanListDatesController::class, DateConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BanListDatesControllerTest {
    @MockBean
    private lateinit var banListDatesService: BanListDatesService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var banListDateFormat: SimpleDateFormat


    @Nested
    inner class HappyPath {
        @Test
        fun `Getting Ban List Dates Using Controller, DB Has Databases - Success`() {
            `when`(banListDatesService.retrieveBanListStartDates())
                .thenReturn(
                    BanListDates()
                        .apply {
                            this.dates = getMockBanListDates()
                        }
                )


            // call endpoint to retrieve ban list dates
            mockMvc
                .perform(get("/ban_list/dates"))
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.banListDates.length()", Matchers.`is`(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banListDates[0].effectiveDate", Matchers.`is`("2021-07-01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banListDates[1].effectiveDate", Matchers.`is`("2021-04-14")))


            // ensure methods are called correct number of times
            verify(banListDatesService)
                .retrieveBanListStartDates()
        }


        @Test
        fun `Getting Ban List Dates Using Controller, But There Are No Ban List Dates To Return - Success`() {
            // mock retrieval of ban list dates - return an empty array (na dates found in DB)
            `when`(banListDatesService.retrieveBanListStartDates())
                .thenReturn(
                    BanListDates()
                        .apply {
                        this.dates = emptyList()
                    }
                )


            // call endpoint to retrieve ban list dates
            mockMvc
                .perform(get("/ban_list/dates"))
                .andExpect(status().isOk)


            // ensure methods are called correct number of times
            verify(banListDatesService)
                .retrieveBanListStartDates()
        }
    }


    /**
     * Helper method used to create a list of BanListDate objects.
     */
    private fun getMockBanListDates(): List<BanListDate> = listOf(
        BanListDate().apply {
            this.effectiveDate = banListDateFormat.parse("2021-07-01")
        }
        , BanListDate().apply {
            this.effectiveDate = banListDateFormat.parse("2021-04-14")
        }
    )
}