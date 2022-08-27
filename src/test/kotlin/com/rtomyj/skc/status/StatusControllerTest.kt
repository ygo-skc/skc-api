package com.rtomyj.skc.status

import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.skcsuggestionengine.status.SuggestionEngineStatusService
import com.rtomyj.skc.skcsuggestionengine.status.model.SuggestionEngineDownstreamStatus
import com.rtomyj.skc.skcsuggestionengine.status.model.SuggestionEngineStatus
import com.rtomyj.skc.status.dao.StatusDao
import com.rtomyj.skc.status.model.DownstreamStatus
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.jvm.internal.Intrinsics

@WebMvcTest(StatusController::class)
@ContextConfiguration(classes = [StatusController::class])
@Tag("Controller")
class StatusControllerTest {
	@Autowired
	private lateinit var mockMvc: MockMvc

	@MockBean(name = "jdbc")
	private lateinit var dao: StatusDao

	@MockBean
	private lateinit var suggestionEngineStatusService: SuggestionEngineStatusService


	companion object {
		private const val DB_NAME = "SKC DB"
		private const val DB_VERSION = "8"
		private const val DB_STATUS = "up"

		private const val SKC_SUGGESTION_ENGINE_NAME = "SKC Suggestion Engine"
		private const val SKC_SUGGESTION_ENGINE_VERSION = "1.0.0"
		private const val SKC_SUGGESTION_ENGINE_STATUS = "All good 😛"

		private val SKC_DB_UP = DownstreamStatus(DB_NAME, DB_VERSION, DB_STATUS)
		private val SKC_SUGGESTION_ENGINE_DB_UP = SuggestionEngineDownstreamStatus("Suggestion Engine DB", "Up")
		private val SKC_SUGGESTION_ENGINE_MONGO_DB_UP = SuggestionEngineDownstreamStatus("Suggestion Engine MongoDB", "Down")
	}


	@Nested
	inner class HappyPath {
		@Test
		fun `Calling Test Call Endpoint With Get Method - Success`() {
			Mockito
				.`when`(dao.dbConnection())
				.thenReturn(SKC_DB_UP)

			Mockito
				.`when`(suggestionEngineStatusService.getStatus())
				.thenReturn(SuggestionEngineStatus(SKC_SUGGESTION_ENGINE_VERSION, listOf(SKC_SUGGESTION_ENGINE_DB_UP)))

			mockMvc
				.perform(get("/status"))
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.status", `is`("API is online and functional.")))
				.andExpect(jsonPath("$.downstream[0].name", `is`(DB_NAME)))
				.andExpect(jsonPath("$.downstream[0].version", `is`(DB_VERSION)))
				.andExpect(jsonPath("$.downstream[0].status", `is`(DB_STATUS)))
				.andExpect(jsonPath("$.downstream[1].name", `is`(SKC_SUGGESTION_ENGINE_NAME)))
				.andExpect(jsonPath("$.downstream[1].version", `is`(SKC_SUGGESTION_ENGINE_VERSION)))
				.andExpect(jsonPath("$.downstream[1].status", `is`(SKC_SUGGESTION_ENGINE_STATUS)))
		}

		@Test
		fun `Calling Test Call Endpoint With Get Method - SKC Suggestion Engine Has One Service Down`() {
			Mockito
				.`when`(dao.dbConnection())
				.thenReturn(SKC_DB_UP)

			Mockito
				.`when`(suggestionEngineStatusService.getStatus())
				.thenReturn(
					SuggestionEngineStatus(
						SKC_SUGGESTION_ENGINE_VERSION,
						listOf(SKC_SUGGESTION_ENGINE_DB_UP, SKC_SUGGESTION_ENGINE_MONGO_DB_UP)
					)
				)

			mockMvc
				.perform(get("/status"))
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.status", `is`("API is online and functional.")))
				.andExpect(jsonPath("$.downstream[0].name", `is`(DB_NAME)))
				.andExpect(jsonPath("$.downstream[0].version", `is`(DB_VERSION)))
				.andExpect(jsonPath("$.downstream[0].status", `is`(DB_STATUS)))
				.andExpect(jsonPath("$.downstream[1].name", `is`(SKC_SUGGESTION_ENGINE_NAME)))
				.andExpect(jsonPath("$.downstream[1].version", `is`(SKC_SUGGESTION_ENGINE_VERSION)))
				.andExpect(jsonPath("$.downstream[1].status", `is`("The following services are offline: Suggestion Engine MongoDB")))
		}

		@Test
		fun `Calling Test Call Endpoint With Get Method - SKC Suggestion Engine Status Results In An Error`() {
			Mockito
				.`when`(dao.dbConnection())
				.thenReturn(SKC_DB_UP)

			Mockito
				.`when`(suggestionEngineStatusService.getStatus())
				.thenThrow(SKCException("Suggestion Engine status check failed.", ErrorType.DS001))

			mockMvc
				.perform(get("/status"))
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.status", `is`("API is online and functional.")))
				.andExpect(jsonPath("$.downstream[0].name", `is`(DB_NAME)))
				.andExpect(jsonPath("$.downstream[0].version", `is`(DB_VERSION)))
				.andExpect(jsonPath("$.downstream[0].status", `is`(DB_STATUS)))
				.andExpect(jsonPath("$.downstream[1].name", `is`(SKC_SUGGESTION_ENGINE_NAME)))
				.andExpect(jsonPath("$.downstream[1].version", `is`("N/A")))
				.andExpect(jsonPath("$.downstream[1].status", `is`("API is down.")))
		}
	}
}