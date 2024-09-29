package com.rtomyj.skc.browse

import com.google.common.base.Suppliers
import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.model.CardBrowseCriteria
import com.rtomyj.skc.model.CardBrowseResults
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.util.function.Tuples
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping(path = ["/card/browse"], produces = ["application/json; charset=UTF-8"])
@Tag(name = SwaggerConstants.TAG_CARD_TAG_NAMED)
class CardBrowseController @Autowired constructor(
  private val cardBrowseService: CardBrowseService
) {

  private val cardBrowseCriteriaSupplier =
    Suppliers.memoizeWithExpiration({ cardBrowseService.browseCriteria() }, 10, TimeUnit.MINUTES)

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }


  @GetMapping
  @Operation(
    summary = "Fetches cards given a set of criteria (use /api/v1/browse/criteria for valid criteria)."
  )
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "400", ref = "badRequest")
  @ApiResponse(responseCode = "404", ref = "notFound")
  @ApiResponse(responseCode = "422", ref = "unprocessableEntity")
  fun browse(
    @Parameter(
      description = "Desired set of card types to include in browse results.",
      example = "effect,fusion",
      schema = Schema(implementation = String::class)
    ) @RequestParam(value = "cardColors", defaultValue = "") cardColors: String = "", @Parameter(
      description = "Desired set of attributes to include in browse results.",
      example = "wind,dark,light",
      schema = Schema(implementation = String::class)
    ) @RequestParam(value = "attributes", defaultValue = "") attributes: String = "", @Parameter(
      description = "Desired set of monster types to include in browse results.",
      example = "spellcaster,wyrm,warrior",
      schema = Schema(implementation = String::class)
    ) @RequestParam(value = "monsterTypes", defaultValue = "") monsterTypes: String = "", @Parameter(
      description = "Desired set of monster sub types to include in browse results.",
      example = "flip,gemini,toon",
      schema = Schema(implementation = String::class)
    ) @RequestParam(value = "monsterSubTypes", defaultValue = "") monsterSubTypes: String = "", @Parameter(
      description = "Desired set of monster levels to include in browse results.",
      example = "4,5,6,7,8",
      schema = Schema(implementation = Int::class)
    ) @RequestParam(value = "levels", defaultValue = "") monsterLevels: String = "", @Parameter(
      description = "Desired set of monster ranks to include in browse results.",
      example = "4,7,8",
      schema = Schema(implementation = Int::class)
    ) @RequestParam(value = "ranks", defaultValue = "") monsterRanks: String = "", @Parameter(
      description = "Desired set of monster ranks to include in browse results.",
      example = "4,7,8",
      schema = Schema(implementation = Int::class)
    ) @RequestParam(value = "linkRatings", defaultValue = "") monsterLinkRatings: String = ""
  ): Mono<CardBrowseResults> = ReactiveMDC.deferMDC(Mono
      .fromCallable {
        val cardColorsSet: Set<String> = CardBrowseService.criteriaStringToSet(cardColors)
        val attributeSet: Set<String> = CardBrowseService.criteriaStringToSet(attributes)
        val monsterTypeSet: Set<String> = CardBrowseService.criteriaStringToSet(monsterTypes)
        val monsterSubTypeSet = CardBrowseService.criteriaStringToSet(monsterSubTypes)

        val levelSet = CardBrowseService.stringSetToIntSet(CardBrowseService.criteriaStringToSet(monsterLevels))
        val rankSet = CardBrowseService.stringSetToIntSet(CardBrowseService.criteriaStringToSet(monsterRanks))
        val linkRatingSet =
          CardBrowseService.stringSetToIntSet(CardBrowseService.criteriaStringToSet(monsterLinkRatings))

        val criteria = CardBrowseCriteria(
          cardColorsSet, attributeSet, monsterTypeSet, monsterSubTypeSet, levelSet, rankSet, linkRatingSet
        )

        Tuples.of(criteria, cardBrowseService.browseResults(criteria))
      }
      .doOnSuccess {
        log.info(
          "Successfully retrieved card browse results using criteria: [ {} ]. Found {} matching results.",
          it.t1.toString(),
          it.t2
        )
      }
      .map {
        it.t2
      }
      .doOnSubscribe {
        log.info("Retrieving browse results.")
      })

  @GetMapping("/criteria")
  @Operation(
    summary = "Fetches valid criteria and valid values for each criteria that can be used in browse endpoint.",
  )
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "400", ref = "badRequest")
  @ApiResponse(responseCode = "404", ref = "notFound")
  @ApiResponse(responseCode = "422", ref = "unprocessableEntity")
  fun browseCriteria(): Mono<CardBrowseCriteria> = ReactiveMDC.deferMDC(
    Mono
        .fromCallable { cardBrowseCriteriaSupplier.get() }
        .doOnSuccess {
          log.info("Successfully retrieved browse criteria for cards.")
        }
        .doOnSubscribe { log.info("Retrieving browse criteria.") })
}