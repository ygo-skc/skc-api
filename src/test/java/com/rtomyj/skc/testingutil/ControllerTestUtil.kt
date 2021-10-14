package com.rtomyj.skc.testingutil

import com.rtomyj.skc.exception.ErrorType
import org.hamcrest.Matchers
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class ControllerTestUtil {
	companion object {
		/**
		 * Helper method that will validate message and code from controller body for when a DB doesn't have expected table.
		 */
		@JvmStatic
		fun validateSTableNotCreatedHelper(resultActions: ResultActions) {
			resultActions
				.andExpect(MockMvcResultMatchers.status().isInternalServerError)
				.andExpect(
					MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`(ErrorType.D002.error))
				)
				.andExpect(
					MockMvcResultMatchers.jsonPath("$.code", Matchers.`is`(ErrorType.D002.name))
				)
		}
	}
}