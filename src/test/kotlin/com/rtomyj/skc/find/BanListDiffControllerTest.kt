package com.rtomyj.skc.find

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListNewContent
import com.rtomyj.skc.model.BanListRemovedContent
import com.rtomyj.skc.testingutil.ControllerTestUtil
import com.rtomyj.skc.util.constant.ErrConstants
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(BanListDiffController::class)
@Tag("Controller")
class BanListDiffControllerTest {
  @MockitoBean
  private lateinit var banListDiffService: BanListDiffService

  @Autowired
  private lateinit var mockMvc: WebTestClient


  companion object {
    private const val NEW_CONTENT_ENDPOINT = "/ban_list/2018-12-03/new"
    private const val REMOVED_CONTENT_ENDPOINT = "/ban_list/2018-12-03/removed"

    private val mapper = jacksonObjectMapper()

    private val banListNewContent =
      mapper.readValue(ClassPathResource(TestConstants.BAN_LIST_NEW_CONTENT).file, BanListNewContent::class.java)

    private val banListRemovedContent = mapper.readValue(
      ClassPathResource(TestConstants.BAN_LIST_REMOVED_CONTENT).file, BanListRemovedContent::class.java
    )
  }


  @Nested
  inner class HappyPath {
    @Test
    fun `Getting Newly Added Cards For A Ban List  - Success`() {
      // mock exception when Service is called
      `when`(
        banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
      ).thenReturn(banListNewContent)


      // call controller
      mockMvc
          .get()
          .uri(NEW_CONTENT_ENDPOINT)
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .jsonPath("$.listRequested")
          .isEqualTo(TestConstants.BAN_LIST_START_DATE)
          .jsonPath("$.comparedTo")
          .isEqualTo(TestConstants.PREVIOUS_BAN_LIST_START_DATE)

      // ensure mocks are called the correct number of times
      verify(banListDiffService).getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
    }


    @Test
    fun `Getting Removed Cards For A Ban List  - Success`() {
      // mock exception when Service is called
      `when`(
        banListDiffService.getRemovedContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
      ).thenReturn(banListRemovedContent)


      // call controller
      mockMvc
          .get()
          .uri(REMOVED_CONTENT_ENDPOINT)
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .jsonPath("$.listRequested")
          .isEqualTo(TestConstants.BAN_LIST_START_DATE)
          .jsonPath("$.comparedTo")
          .isEqualTo(TestConstants.PREVIOUS_BAN_LIST_START_DATE)

      // ensure mocks are called the correct number of times
      verify(banListDiffService).getRemovedContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
    }
  }


  @Nested
  inner class UnhappyPath {
    @Test
    fun `Getting Newly Added Cards For A Ban List - Date Used Isn't In Correct Format - 422 HTTP Exception`() {

      ControllerTestUtil.validateErrorByErrorType(
        mockMvc
            .get()
            .uri("/ban_list/incorrect-date-format/new")
            .exchange(), 422, ErrorType.G001
      )
    }


    @Test
    fun `Getting Newly Added Cards For A Ban List - No Ban List Info For Given Date - 404 HTTP Exception`() {
      // mock exception when Service is called
      `when`(
        banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
      ).thenThrow(
        SKCException(
          String.format(
            ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE, TestConstants.BAN_LIST_START_DATE
          ), ErrorType.DB001
        )
      )


      // call controller

      ControllerTestUtil.validateErrorByErrorType(
        mockMvc
            .get()
            .uri(NEW_CONTENT_ENDPOINT)
            .exchange(), 404, ErrorType.DB001
      )

      // ensure mocks are called the correct number of times
      verify(banListDiffService).getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
    }


    @Test
    fun `Getting Newly Added Cards For A Ban List - Ban List Table Is Missing - 500 HTTP Exception`() {
      // mock exception when Service is called
      `when`(
        banListDiffService.getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
      ).thenThrow(
        SKCException(ErrConstants.DB_MISSING_TABLE, ErrorType.DB002)
      )


      // call controller
      ControllerTestUtil.validateErrorByErrorType(
        mockMvc
            .get()
            .uri(NEW_CONTENT_ENDPOINT)
            .exchange(), 500, ErrorType.DB002
      )

      // ensure mocks are called the correct number of times
      verify(banListDiffService).getNewContentForGivenBanList(TestConstants.BAN_LIST_START_DATE, "TCG")
    }
  }
}