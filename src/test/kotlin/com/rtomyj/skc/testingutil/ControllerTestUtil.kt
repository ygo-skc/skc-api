package com.rtomyj.skc.testingutil

import com.rtomyj.skc.exception.ErrorType
import org.springframework.test.web.reactive.server.WebTestClient

class ControllerTestUtil {
  companion object {
    /**
     * Helper method that will validate message and code from controller body for when a DB doesn't have expected table.
     */
    @JvmStatic
    fun validateErrorByErrorType(resultActions: WebTestClient.ResponseSpec, error: ErrorType) {
      resultActions
          .expectStatus()
          .isEqualTo(error.httpStatus)
          .expectBody()
          .jsonPath("$.message")
          .isEqualTo(error.error)
          .jsonPath("$.code")
          .isEqualTo(error.name)
    }
  }
}