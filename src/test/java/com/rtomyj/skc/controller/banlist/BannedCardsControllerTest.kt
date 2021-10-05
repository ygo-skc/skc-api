package com.rtomyj.skc.controller.banlist

import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.enums.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.service.banlist.BannedCardsService
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

@WebMvcTest(BannedCardsController::class)
class BannedCardsControllerTest {
	@MockBean
	private lateinit var bannedCardsServiceMock: BannedCardsService

	@Autowired
	private lateinit var mockMvc: MockMvc


	companion object {
		const val REQUESTED_BAN_LIST_MOCK_DATE = "2021-10-01"
		const val REQUESTED_PREVIOUS_BAN_LIST_MOCK_DATE = "2021-07-01"

		const val BAN_LIST_CONTENT_ENDPOINT = "/ban_list/2021-10-01/cards"
	}


	@Nested
	inner class HappyPath {

	}


	@Nested
	inner class UnhappyPath {
		@Test
		fun `Getting All Banned Cards For A Ban List - Date Used Isn't In Correct Format - 400 HTTP Exception`() {
			// ensure calling endpoint fails with expected status and body
			mockMvc
				.perform(get("/ban_list/2020-04-0/cards"))
				.andExpect(status().isBadRequest)
				.andExpect(
					jsonPath("$.message", `is`(ErrorType.G001.toString()))
				)
				.andExpect(
					jsonPath("$.code", `is`(ErrorType.G001.name))
				)
		}


		@Test
		fun `Getting All Banned Cards For A Ban List - Date Requested Is Not A Valid Ban List - 404 HTTP Exception`() {
			// mock call and return exception - ban list requested not in DB
			`when`(
				bannedCardsServiceMock.getBanListByDate(REQUESTED_BAN_LIST_MOCK_DATE, false, false)
			)
				.thenThrow(
					YgoException(String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, REQUESTED_BAN_LIST_MOCK_DATE)
					, HttpStatus.NOT_FOUND, ErrorType.D001)
				)



			// ensure calling endpoint fails with expected status and body
			mockMvc
				.perform(get("${BAN_LIST_CONTENT_ENDPOINT}?saveBandwidth=false&allInfo=false"))
				.andExpect(status().isNotFound)
				.andExpect(
					jsonPath("$.message", `is`(ErrorType.D001.toString()))
				)
				.andExpect(
					jsonPath("$.code", `is`(ErrorType.D001.name))
				)


			// ensure mocks are called the correct number of times
			verify(bannedCardsServiceMock)
				.getBanListByDate(REQUESTED_BAN_LIST_MOCK_DATE, false, false)
		}
	}
}