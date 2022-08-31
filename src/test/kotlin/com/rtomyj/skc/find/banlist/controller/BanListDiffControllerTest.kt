package com.rtomyj.skc.find.banlist.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rtomyj.skc.find.banlist.service.DiffService
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.find.banlist.model.BanListNewContent
import com.rtomyj.skc.find.banlist.model.BanListRemovedContent
import com.rtomyj.skc.testingutil.ControllerTestUtil
import com.rtomyj.skc.util.constant.ErrConstants
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
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

		private val mapper = jacksonObjectMapper()

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
				banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
			)
				.thenReturn(banListNewContent)


			// call controller
			mockMvc
				.perform(get(NEW_CONTENT_ENDPOINT))
				.andExpect(status().isOk)
				.andExpect(
					jsonPath(
						"$.listRequested", `is`(TestConstants.BAN_LIST_START_DATE)
					)
				)
				.andExpect(
					jsonPath("$.comparedTo", `is`(TestConstants.PREVIOUS_BAN_LIST_START_DATE))
				)


			// ensure mocks are called the correct number of times
			verify(banListDiffService)
				.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
		}


		@Test
		fun `Getting Removed Cards For A Ban List  - Success`() {
			// mock exception when Service is called
			`when`(
				banListDiffService.getRemovedContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
			)
				.thenReturn(banListRemovedContent)


			// call controller
			mockMvc
				.perform(get(REMOVED_CONTENT_ENDPOINT))
				.andExpect(status().isOk)
				.andExpect(
					jsonPath(
						"$.listRequested", `is`(TestConstants.BAN_LIST_START_DATE)
					)
				)
				.andExpect(
					jsonPath("$.comparedTo", `is`(TestConstants.PREVIOUS_BAN_LIST_START_DATE))
				)


			// ensure mocks are called the correct number of times
			verify(banListDiffService)
				.getRemovedContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
		}
	}


	@Nested
	inner class UnhappyPath {
		@Test
		fun `Getting Newly Added Cards For A Ban List - Date Used Isn't In Correct Format - 400 HTTP Exception`() {

			ControllerTestUtil.validateBadRequestHelper(
				mockMvc
					.perform(get("/ban_list/incorrect-date-format/new"))
			)
		}


		@Test
		fun `Getting Newly Added Cards For A Ban List - No Ban List Info For Given Date - 404 HTTP Exception`() {
			// mock exception when Service is called
			`when`(
				banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
			)
				.thenThrow(
					SKCException(
						String.format(
							ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE,
							TestConstants.BAN_LIST_START_DATE
						), ErrorType.DB001
					)
				)


			// call controller
			ControllerTestUtil.validateNotFoundHelper(
				mockMvc
					.perform(get(NEW_CONTENT_ENDPOINT))
			)


			// ensure mocks are called the correct number of times
			verify(banListDiffService)
				.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
		}


		@Test
		fun `Getting Newly Added Cards For A Ban List - Ban List Table Is Missing - 500 HTTP Exception`() {
			// mock exception when Service is called
			`when`(
				banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
			)
				.thenThrow(
					SKCException(ErrConstants.DB_MISSING_TABLE, ErrorType.DB002)
				)


			// call controller
			ControllerTestUtil.validateSTableNotCreatedHelper(mockMvc.perform(get(NEW_CONTENT_ENDPOINT)))


			// ensure mocks are called the correct number of times
			verify(banListDiffService)
				.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
		}
	}
}