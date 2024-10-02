package com.rtomyj.skc.stats

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.config.SwaggerConfig
import com.rtomyj.skc.model.DatabaseStats
import com.rtomyj.skc.model.MonsterTypeStats
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/stats"], produces = ["application/json; charset=UTF-8"])
@Tag(name = SwaggerConstants.TAG_STATS_NAME)
class StatsController @Autowired constructor(private val statsService: StatsService) {
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }

  @Operation(summary = "Retrieve sum of all unique monster types for a given color of a card.")
  @GetMapping("/card/monster_type/{cardColor}")
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "400", ref = "badRequest")
  @ApiResponse(responseCode = "404", ref = "notFound")
  @ApiResponse(responseCode = "422", ref = "unprocessableEntity")
  @ApiResponse(responseCode = "500", ref = "internalServerError")
  fun monsterTypeStats(
    @Parameter(description = SwaggerConfig.CARD_COLOR_DESCRIPTION,
      `in` = ParameterIn.PATH,
      required = true,
      examples = [ExampleObject(ref = "fusion", name = "fusion"), ExampleObject(ref = "effect", name = "effect"),
        ExampleObject(ref = "synchro", name = "synchro")])
    @PathVariable("cardColor") cardColor: String
  ): Mono<MonsterTypeStats> = ReactiveMDC.deferMDC(
    Mono
        .fromCallable { statsService.getMonsterTypeStats(cardColor) }
        .doOnSubscribe {
          log.info("Retrieving monster types for cards with color: {}", cardColor)
        })

  @Operation(summary = "Retrieve overview of the data currently in Database.")
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "400", ref = "badRequest")
  @ApiResponse(responseCode = "404", ref = "notFound")
  @GetMapping
  fun databaseStats(): Mono<DatabaseStats> = ReactiveMDC.deferMDC(
    Mono
        .fromCallable { statsService.databaseStats() }
        .doOnSuccess {
          log.info("Successfully retrieved database stats: {}", it.toString())
        }
        .doOnSubscribe { log.info("Retrieving high level overview of info stored in DB.") })
}