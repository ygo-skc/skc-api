package com.rtomyj.skc.controller.banlist

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListNewContent
import com.rtomyj.skc.model.banlist.BanListRemovedContent
import com.rtomyj.skc.service.banlist.DiffService
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BanListDiffController::class)
@Tag("Controller")
class BanListDiffControllerTest {
    @MockBean
    private lateinit var banListDiffService: DiffService

    @Autowired
    private lateinit var mockMvc: MockMvc


    companion object {
        private const val NEW_CONTENT_ENDPOINT = "/ban_list/2018-12-03/new"
        private const val REMOVED_CONTENT_ENDPOINT = "/ban_list/2018-12-03/removed"

        private val mapper = ObjectMapper()

        private val banListNewContent = mapper
            .readValue(ClassPathResource(TestConstants.BAN_LIST_NEW_CONTENT).file, BanListNewContent::class.java)

        private val banListRemovedContent = mapper
            .readValue(ClassPathResource(TestConstants.BAN_LIST_REMOVED_CONTENT).file, BanListRemovedContent::class.java)
    }


    @Nested
    inner class HappyPath {
        @Test
        fun `Getting Newly Added Cards For A Ban List  - Success`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
            )
                .thenReturn(banListNewContent)


            // call controller
            mockMvc
                .perform(get(NEW_CONTENT_ENDPOINT))
                .andExpect(status().isOk)
                .andExpect(
                    jsonPath("$.listRequested", `is`(TestConstants.BAN_LIST_START_DATE)
                    )
                )
                .andExpect(
                    jsonPath("$.comparedTo", `is`(TestConstants.PREVIOUS_BAN_LIST_START_DATE))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
        }


        @Test
        fun `Getting Removed Cards For A Ban List  - Success`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getRemovedContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
            )
                .thenReturn(banListRemovedContent)


            // call controller
            mockMvc
                .perform(get(REMOVED_CONTENT_ENDPOINT))
                .andExpect(status().isOk)
                .andExpect(
                    jsonPath("$.listRequested", `is`(TestConstants.BAN_LIST_START_DATE)
                    )
                )
                .andExpect(
                    jsonPath("$.comparedTo", `is`(TestConstants.PREVIOUS_BAN_LIST_START_DATE))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getRemovedContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
        }
    }


    @Nested
    inner class UnhappyPath {
        @Test
        fun `Getting Newly Added Cards For A Ban List - Date Used Isn't In Correct Format - 400 HTTP Exception`() {
            mockMvc
                .perform(get("/ban_list/incorrect-date-format/new"))
                .andExpect(status().isBadRequest)
                .andExpect(
                    jsonPath("$.message", `is`(ErrorType.G001.error))
                )
                .andExpect(
                    jsonPath("$.code", `is`(ErrorType.G001.name))
                )
        }


        @Test
        fun `Getting Newly Added Cards For A Ban List - No Ban List Info For Given Date - 404 HTTP Exception`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
            )
                .thenThrow(
                    YgoException(
                        String.format(
                            ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE,
                            TestConstants.BAN_LIST_START_DATE
                        ), ErrorType.D001
                    )
                )


            // call controller
            mockMvc
                .perform(get(NEW_CONTENT_ENDPOINT))
                .andExpect(status().isNotFound)
                .andExpect(
                    jsonPath(
                        "$.message", `is`(ErrorType.D001.error)
                    )
                )
                .andExpect(
                    jsonPath("$.code", `is`(ErrorType.D001.name))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
        }


        @Test
        fun `Getting Newly Added Cards For A Ban List - Ban List Table Is Missing - 500 HTTP Exception`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
            )
                .thenThrow(
                    YgoException(ErrConstants.DB_MISSING_TABLE, ErrorType.D002)
                )


            // call controller
            mockMvc
                .perform(get(NEW_CONTENT_ENDPOINT))
                .andExpect(status().isInternalServerError)
                .andExpect(
                    jsonPath("$.message", `is`(ErrorType.D002.error))
                )
                .andExpect(
                    jsonPath("$.code", `is`(ErrorType.D002.name))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE)
        }
    }
}