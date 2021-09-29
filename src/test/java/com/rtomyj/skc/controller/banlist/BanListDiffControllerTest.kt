package com.rtomyj.skc.controller.banlist

import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.enums.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListNewContent
import com.rtomyj.skc.model.banlist.BanListRemovedContent
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus
import com.rtomyj.skc.service.banlist.DiffService
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BanListDiffController::class)
class BanListDiffControllerTest {
    @MockBean
    private lateinit var banListDiffService: DiffService

    @Autowired
    private lateinit var mockMvc: MockMvc


    companion object {
        const val REQUESTED_BAN_LIST_MOCK_DATE = "2021-10-01"
        const val REQUESTED_PREVIOUS_BAN_LIST_MOCK_DATE = "2021-07-01"

        const val NEW_CONTENT_ENDPOINT = "/ban_list/2021-10-01/new"
        const val REMOVED_CONTENT_ENDPOINT = "/ban_list/2021-10-01/removed"

        val NEW_FORBIDDEN = listOf(
            CardsPreviousBanListStatus().apply {
                this.cardId = TestConstants.A_HERO_LIVES_ID
                this.cardName = TestConstants.A_HERO_LIVES_NAME
                this.previousBanStatus = "Limited"
            }
            , CardsPreviousBanListStatus().apply {
                this.cardId = TestConstants.STRATOS_ID
                this.cardName = TestConstants.STRATOS_NAME
                this.previousBanStatus = "Unlimited"
            }
        )

        val REMOVED_CARDS = listOf(
            CardsPreviousBanListStatus().apply {
                this.cardId = TestConstants.A_HERO_LIVES_ID
                this.cardName = TestConstants.A_HERO_LIVES_NAME
                this.previousBanStatus = "Limited"
            }
            , CardsPreviousBanListStatus().apply {
                this.cardId = TestConstants.STRATOS_ID
                this.cardName = TestConstants.STRATOS_NAME
                this.previousBanStatus = "Forbidden"
            }
        )
    }


    @Nested
    inner class HappyPath {
        @Test
        fun `Getting Newly Added Cards For A Ban List  - Success`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getNewContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
            )
                .thenReturn(
                    BanListNewContent().apply {
                        this.listRequested = REQUESTED_BAN_LIST_MOCK_DATE
                        this.comparedTo = REQUESTED_PREVIOUS_BAN_LIST_MOCK_DATE

                        this.numNewForbidden = NEW_FORBIDDEN.size
                        this.numNewLimited = 0
                        this.numNewSemiLimited = 0

                        this.newForbidden = NEW_FORBIDDEN
                        this.newLimited = emptyList()
                        this.newSemiLimited = emptyList()
                    }
                )


            // call controller
            mockMvc
                .perform(get(NEW_CONTENT_ENDPOINT))
                .andExpect(status().isOk)
                .andExpect(
                    jsonPath("$.listRequested", `is`(REQUESTED_BAN_LIST_MOCK_DATE)
                    )
                )
                .andExpect(
                    jsonPath("$.comparedTo", `is`(REQUESTED_PREVIOUS_BAN_LIST_MOCK_DATE))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getNewContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
        }


        @Test
        fun `Getting Removed Cards For A Ban List  - Success`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getRemovedContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
            )
                .thenReturn(
                    BanListRemovedContent().apply {
                        this.listRequested = REQUESTED_BAN_LIST_MOCK_DATE
                        this.comparedTo = REQUESTED_PREVIOUS_BAN_LIST_MOCK_DATE

                        this.numRemoved = REMOVED_CARDS.size
                        this.removedCards = REMOVED_CARDS
                    }
                )


            // call controller
            mockMvc
                .perform(get(REMOVED_CONTENT_ENDPOINT))
                .andExpect(status().isOk)
                .andExpect(
                    jsonPath("$.listRequested", `is`(REQUESTED_BAN_LIST_MOCK_DATE)
                    )
                )
                .andExpect(
                    jsonPath("$.comparedTo", `is`(REQUESTED_PREVIOUS_BAN_LIST_MOCK_DATE))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getRemovedContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
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
                    jsonPath("$.message", `is`("URL requested doesn't have proper syntax"))
                )
                .andExpect(
                    jsonPath("$.code", `is`("G001"))
                )
        }


        @Test
        fun `Getting Newly Added Cards For A Ban List - No Ban List Info For Given Date - 404 HTTP Exception`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getNewContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
            )
                .thenThrow(
                    YgoException(String.format(ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE, REQUESTED_BAN_LIST_MOCK_DATE)
                        , HttpStatus.NOT_FOUND, ErrorType.D001)
                )


            // call controller
            mockMvc
                .perform(get(NEW_CONTENT_ENDPOINT))
                .andExpect(status().isNotFound)
                .andExpect(
                    jsonPath("$.message"
                        , `is`(ErrorType.D001.toString())
                    )
                )
                .andExpect(
                    jsonPath("$.code", `is`(ErrorType.D001.name))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getNewContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
        }


        @Test
        fun `Getting Newly Added Cards For A Ban List - Ban List Table Is Missing - 500 HTTP Exception`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getNewContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
            )
                .thenThrow(
                    YgoException(ErrConstants.DB_MISSING_TABLE, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.D002)
                )


            // call controller
            mockMvc
                .perform(get(NEW_CONTENT_ENDPOINT))
                .andExpect(status().isInternalServerError)
                .andExpect(
                    jsonPath("$.message", `is`(ErrorType.D002.toString()))
                )
                .andExpect(
                    jsonPath("$.code", `is`(ErrorType.D002.name))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getNewContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
        }


        @Test
        fun `Getting Cards Removed From A Ban List - Date Used Isn't In Correct Format - 400 HTTP Exception`() {
            mockMvc
                .perform(get("/ban_list/incorrect-date-format/removed"))
                .andExpect(status().isBadRequest)
                .andExpect(
                    jsonPath("$.message", `is`("URL requested doesn't have proper syntax"))
                )
                .andExpect(
                    jsonPath("$.code", `is`("G001"))
                )
        }


        @Test
        fun `Getting Cards Removed From A Ban List - No Ban List Info For Given Date - 404 HTTP Exception`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getRemovedContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
            )
                .thenThrow(
                    YgoException(String.format(ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE, REQUESTED_BAN_LIST_MOCK_DATE)
                        , HttpStatus.NOT_FOUND, ErrorType.D001)
                )


            // call controller
            mockMvc
                .perform(get(REMOVED_CONTENT_ENDPOINT))
                .andExpect(status().isNotFound)
                .andExpect(
                    jsonPath("$.message"
                        , `is`(ErrorType.D001.toString())
                    )
                )
                .andExpect(
                    jsonPath("$.code", `is`(ErrorType.D001.name))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getRemovedContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
        }


        @Test
        fun `Getting Cards Removed From A Ban List - Ban List Table Is Missing - 500 HTTP Exception`() {
            // mock exception when Service is called
            `when`(
                banListDiffService.getNewContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
            )
                .thenThrow(
                    YgoException(ErrConstants.DB_MISSING_TABLE, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.D002)
                )


            // call controller
            mockMvc
                .perform(get(NEW_CONTENT_ENDPOINT))
                .andExpect(status().isInternalServerError)
                .andExpect(
                    jsonPath("$.message", `is`(ErrorType.D002.toString()))
                )
                .andExpect(
                    jsonPath("$.code", `is`(ErrorType.D002.name))
                )


            // ensure mocks are called the correct number of times
            verify(banListDiffService)
                .getNewContentForGivenBanList(REQUESTED_BAN_LIST_MOCK_DATE)
        }
    }
}