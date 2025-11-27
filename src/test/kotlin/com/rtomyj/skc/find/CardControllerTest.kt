package com.rtomyj.skc.find

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.net.HttpHeaders.X_FORWARDED_FOR
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.Card
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
import reactor.core.publisher.Mono

@WebFluxTest(CardController::class)
@Tag("Controller")
class CardControllerTest {
  @MockitoBean
  private lateinit var cardService: CardService

  @Autowired
  private lateinit var mockMvc: WebTestClient


  @Nested
  inner class HappyPath {
    @Test
    fun `Fetching Card Information Using Card ID - Success`() {
      // setup mocks - throw NOT FOUND (card not in DB) exception when particular card is requested
      val mapper = jacksonObjectMapper()
      val mockCardData: Card =
        mapper.readValue(ClassPathResource(TestConstants.CARD_INSTANCE_STRATOS).file, Card::class.java)

      `when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)).thenReturn(
        Mono.just(
          mockCardData
        )
      )


      // call controller and verify correct status, code and message are returned
      mockMvc
          .get()
          .uri {
            it
                .path("/card/${TestConstants.STRATOS_ID}")
                .queryParam("allInfo", "true")
                .build()
          }
          .header(X_FORWARDED_FOR, TestConstants.MOCK_IP)
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .jsonPath("$.cardID")
          .isEqualTo(TestConstants.STRATOS_ID)
          .jsonPath("$.cardName")
          .isEqualTo(TestConstants.STRATOS_NAME)
          .jsonPath("$.cardColor")
          .isEqualTo(TestConstants.STRATOS_COLOR)
          .jsonPath("$.cardAttribute")
          .isEqualTo(TestConstants.STRATOS_ATTRIBUTE)


      // verify mocks are called
      verify(cardService).getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)
    }
  }


  @Nested
  inner class UnhappyPath {
    @Test
    fun `Fetching Card Information Using Card ID - Card ID Is Not Formatted Correctly - HTTP 400 Error`() {
      // call controller with incorrectly formatted card ID
      ControllerTestUtil.validateErrorByErrorType(mockMvc
          .get()
          .uri {
            it
                .path("/card/123")
                .queryParam("allInfo", "true")
                .build()
          }
          .header(X_FORWARDED_FOR, TestConstants.MOCK_IP)
          .exchange(), 422, ErrorType.G001)
    }


    @Test
    fun `Fetching Card Information Using Card ID - Card Requested Is Not In DB - HTTP 404 Error`() {
      // setup mocks - throw NOT FOUND (card not in DB) exception when particular card is requested
      `when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)).thenThrow(
        SKCException(
          String.format(ErrConstants.CARD_ID_REQUESTED_NOT_FOUND_IN_DB, TestConstants.STRATOS_ID), ErrorType.DB001
        )
      )


      // call controller and verify correct status, code and message are returned
      ControllerTestUtil.validateErrorByErrorType(mockMvc
          .get()
          .uri {
            it
                .path("/card/${TestConstants.STRATOS_ID}")
                .queryParam("allInfo", "true")
                .build()
          }
          .header(X_FORWARDED_FOR, TestConstants.MOCK_IP)
          .exchange(), 404, ErrorType.DB001)


      // verify mocks are called
      verify(cardService).getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)
    }


    @Test
    fun `Fetching Card Information Using Card ID - Required Database Tables Are Missing - HTTP 500 Error`() {
      // setup mocks - throw DB table missing exception when particular card is requested
      `when`(cardService.getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)).thenThrow(
        SKCException(
          ErrConstants.DB_MISSING_TABLE, ErrorType.DB002
        )
      )


      // call controller and verify correct status, code and message are returned
      ControllerTestUtil.validateErrorByErrorType(mockMvc
          .get()
          .uri {
            it
                .path("/card/${TestConstants.STRATOS_ID}")
                .queryParam("allInfo", "true")
                .build()
          }
          .header(X_FORWARDED_FOR, TestConstants.MOCK_IP)
          .exchange(), 500, ErrorType.DB002)

      // verify mocks are called
      verify(cardService).getCardInfo(TestConstants.STRATOS_ID, true, TestConstants.MOCK_IP)
    }
  }
}