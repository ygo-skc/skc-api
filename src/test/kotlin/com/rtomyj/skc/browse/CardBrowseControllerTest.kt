package com.rtomyj.skc.browse

import com.google.common.net.HttpHeaders
import com.rtomyj.skc.constant.TestConstants
import com.rtomyj.skc.model.CardBrowseCriteria
import com.rtomyj.skc.model.CardBrowseResults
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(CardBrowseController::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)    // since we cache Browse Criteria using Guava cache, we need to clean up after each test execution
@Tag("Controller")
class CardBrowseControllerTest {
  @MockitoBean
  private lateinit var cardBrowseService: CardBrowseService

  @Autowired
  private lateinit var mockMvc: WebTestClient

  @Nested
  @Tag("CardBrowse")
  inner class HappyPath {
    @Test
    fun `'Retrieving Browse Results With No Errors'`() {
      // mock setup
      val cardBrowseCriteria = CardBrowseCriteria(
        emptySet(), emptySet(), emptySet(), emptySet(), setOf(4), emptySet(), emptySet()
      )

      Mockito
          .`when`(cardBrowseService.browseResults(cardBrowseCriteria))
          .thenReturn(CardBrowseResults(listOf(CardBrowseTestUtil.stratos), 0))

      // perform call
      mockMvc
          .get()
          .uri() {
            it
                .path("/card/browse")
                .queryParam("levels", "4")
                .build()
          }
          .header(HttpHeaders.X_FORWARDED_FOR, TestConstants.MOCK_IP)
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .jsonPath("$.results.length()")
          .isEqualTo(1)
          .jsonPath("$.results[0].cardID")
          .isEqualTo("40044918")
          .jsonPath("$.results[0].cardName")
          .isEqualTo("Elemental HERO Stratos")
          .jsonPath("$.results[0].cardColor")
          .isEqualTo("Effect")
          .jsonPath("$.results[0].cardAttribute")
          .isEqualTo("Wind")
          .jsonPath("$.results[0].monsterType")
          .isEqualTo("Warrior/Effect")
          .jsonPath("$.results[0].monsterAssociation.level")
          .isEqualTo(4)

      // verify mocks are called with correct values
      Mockito
          .verify(cardBrowseService)
          .browseResults(cardBrowseCriteria)
    }


    @Test
    fun `'Retrieving Browse Criteria With No Errors'`() {
      // mock setup
      val cardBrowseCriteria = CardBrowseTestUtil.cardBrowseCriteria

      Mockito
          .`when`(cardBrowseService.browseCriteria())
          .thenReturn(cardBrowseCriteria)

      // perform call
      mockMvc
          .get()
          .uri("/card/browse/criteria")
          .header(HttpHeaders.X_FORWARDED_FOR, TestConstants.MOCK_IP)
          .exchange()
          .expectStatus().isOk
          .expectBody()
          .jsonPath("$.cardColors.length()")
          .isEqualTo(3)
          .jsonPath("$.attributes.length()")
          .isEqualTo(7)
          .jsonPath("$.monsterTypes.length()")
          .isEqualTo(5)
          .jsonPath("$.monsterSubTypes.length()")
          .isEqualTo(6)
          .jsonPath("$.levels.length()")
          .isEqualTo(3)
          .jsonPath("$.ranks.length()")
          .isEqualTo(3)
          .jsonPath("$.linkRatings.length()")
          .isEqualTo(3)

      // verify mocks are called with correct values
      Mockito
          .verify(cardBrowseService)
          .browseCriteria()
    }
  }
}