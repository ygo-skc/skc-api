package com.rtomyj.skc.search

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.config.SwaggerConfig
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.CardSearchParameters
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/card/search"], produces = ["application/json; charset=UTF-8"])
@Tag(name = SwaggerConstants.TAG_CARD_TAG_NAMED)
class CardSearchController @Autowired constructor(
  private val cardSearchService: CardSearchService
) {

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }

  @GetMapping
  @Operation(
    summary = "Search for a specific set of cards using certain properties. Props don't have to be complete. When partial props are passed, API will return Cards that contain the partial value of given prop. See below for example of partial prop (card name, card ID, monsterType)",
  )
  @ApiResponse(responseCode = "200", description = SwaggerConfig.HTTP_200_SWAGGER_MESSAGE)
  @Throws(
    SKCException::class
  )
  fun searchByCriteria(@Valid cardSearchParameters: CardSearchParameters): Mono<ResponseEntity<List<Card>>> = ReactiveMDC.deferMDC(Mono
      .fromCallable {
        cardSearchService.getCardSearchResults(cardSearchParameters)
      }
      .map { searchResult ->
        log.info(
          "Successfully retrieved search results using the following criteria: {}. Found {} matching results.",
          cardSearchParameters,
          searchResult.size
        )
        ResponseEntity(searchResult, HttpStatus.OK)
      }
      .doOnSubscribe {
        log.info("User is searching for card")
      })
}