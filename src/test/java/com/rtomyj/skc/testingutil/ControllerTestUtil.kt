package com.rtomyj.skc.testingutil

import com.rtomyj.skc.exception.ErrorType
import org.hamcrest.Matchers
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class ControllerTestUtil {
	companion object {
		/**
		 * Helper method that will validate message and code from controller body for when a resource is not in DB.
		 */
		@JvmStatic
		fun validateBadRequestHelper(resultActions: ResultActions) {
			resultActions
				.andExpect(MockMvcResultMatchers.status().isBadRequest)
				.andExpect(
					MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`(ErrorType.G001.error))
				)
				.andExpect(
					MockMvcResultMatchers.jsonPath("$.code", Matchers.`is`(ErrorType.G001.name))
				)
		}


		/**
		 * Helper method that will validate message and code from controller body for when a resource is not in DB.
		 */
		@JvmStatic
		fun validateNotFoundHelper(resultActions: ResultActions) {
			resultActions
				.andExpect(MockMvcResultMatchers.status().isNotFound)
				.andExpect(
					MockMvcResultMatchers.jsonPath(
						"$.message", Matchers.`is`(ErrorType.D001.error)
					)
				)
				.andExpect(
					MockMvcResultMatchers.jsonPath("$.code", Matchers.`is`(ErrorType.D001.name))
				)
		}


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