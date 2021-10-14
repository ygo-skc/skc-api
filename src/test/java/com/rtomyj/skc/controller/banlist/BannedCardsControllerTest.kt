package com.rtomyj.skc.controller.banlist

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListInstance
import com.rtomyj.skc.model.banlist.BanListNewContent
import com.rtomyj.skc.model.banlist.BanListRemovedContent
import com.rtomyj.skc.service.banlist.BannedCardsService
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
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BannedCardsController::class)
@Tag("Controller")
class BannedCardsControllerTest {
	@MockBean
	private lateinit var bannedCardsServiceMock: BannedCardsService

	@Autowired
	private lateinit var mockMvc: MockMvc


	companion object {
		private const val REQUESTED_BAN_LIST_MOCK_DATE = "2018-12-03"

		private const val BAN_LIST_CONTENT_ENDPOINT = "/ban_list/2018-12-03/cards"

		private val mapper = ObjectMapper()

		private val banListInstanceFullText = mapper
			.readValue(ClassPathResource(TestConstants.BAN_LIST_INSTANCE_JSON_FILE).file, BanListInstance::class.java)

		private val banListNewContent = mapper
			.readValue(ClassPathResource(TestConstants.BAN_LIST_NEW_CONTENT).file, BanListNewContent::class.java)

		private val banListRemovedContent = mapper
			.readValue(ClassPathResource(TestConstants.BAN_LIST_REMOVED_CONTENT).file, BanListRemovedContent::class.java)
	}


	@Nested
	inner class HappyPath {
		@Test
		fun `Getting All Banned Cards For A Ban List - Success`() {
			// build mock response
			banListInstanceFullText.newContent = banListNewContent
			banListInstanceFullText.removedContent = banListRemovedContent

			// setup mocks
			`when`(bannedCardsServiceMock.getBanListByDate(REQUESTED_BAN_LIST_MOCK_DATE,
				saveBandwidth = false, fetchAllInfo = true
			))
				.thenReturn(banListInstanceFullText)


			// perform operation on endpoint
			mockMvc
				.perform(
					get(BAN_LIST_CONTENT_ENDPOINT)
						.param("saveBandwidth", "false")
						.param("allInfo", "true")
				)
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.banListInstance.effectiveDate", `is`(REQUESTED_BAN_LIST_MOCK_DATE)))
				.andExpect(jsonPath("$.banListInstance.numForbidden", `is`(1)))
				.andExpect(jsonPath("$.banListInstance.numLimited", `is`(1)))
				.andExpect(jsonPath("$.banListInstance.numSemiLimited", `is`(1)))


			// ensure mocks are called
			verify(bannedCardsServiceMock)
				.getBanListByDate(REQUESTED_BAN_LIST_MOCK_DATE, saveBandwidth = false, fetchAllInfo = true)
		}
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
					jsonPath("$.message", `is`(ErrorType.G001.error))
				)
				.andExpect(
					jsonPath("$.code", `is`(ErrorType.G001.name))
				)
		}


		@Test
		fun `Getting All Banned Cards For A Ban List - Date Requested Is Not A Valid Ban List - 404 HTTP Exception`() {
			// mock call and return exception - ban list requested not in DB
			`when`(
				bannedCardsServiceMock.getBanListByDate(REQUESTED_BAN_LIST_MOCK_DATE,
					saveBandwidth = false, fetchAllInfo = false
				)
			)
				.thenThrow(
					YgoException(String.format(ErrConstants.BAN_LIST_NOT_FOUND_FOR_START_DATE, REQUESTED_BAN_LIST_MOCK_DATE), ErrorType.D001)
				)


			// ensure calling endpoint fails with expected status and body
			mockMvc
				.perform(get("${BAN_LIST_CONTENT_ENDPOINT}?saveBandwidth=false&allInfo=false"))
				.andExpect(status().isNotFound)
				.andExpect(
					jsonPath("$.message", `is`(ErrorType.D001.error))
				)
				.andExpect(
					jsonPath("$.code", `is`(ErrorType.D001.name))
				)


			// ensure mocks are called the correct number of times
			verify(bannedCardsServiceMock)
				.getBanListByDate(REQUESTED_BAN_LIST_MOCK_DATE, saveBandwidth = false, fetchAllInfo = false)
		}


		@Test
		fun `Getting All Banned Cards For A Ban List - DB Is Missing Tables - 500 HTTP Exception`() {
			// mock call and return exception - ban list requested not in DB
			`when`(
				bannedCardsServiceMock.getBanListByDate(REQUESTED_BAN_LIST_MOCK_DATE, saveBandwidth = false, fetchAllInfo = false
				)
			)
				.thenThrow(
					YgoException(ErrConstants.DB_MISSING_TABLE, ErrorType.D002)
				)


			// ensure calling endpoint fails with expected status and body
			ControllerTestUtil.validateSTableNotCreatedHelper(
				mockMvc
					.perform(get("${BAN_LIST_CONTENT_ENDPOINT}?saveBandwidth=false&allInfo=false"))
			)


			// ensure mocks are called the correct number of times
			verify(bannedCardsServiceMock)
				.getBanListByDate(REQUESTED_BAN_LIST_MOCK_DATE, false, false)
		}
	}
}