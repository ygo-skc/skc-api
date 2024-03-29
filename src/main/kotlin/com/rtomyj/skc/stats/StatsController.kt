package com.rtomyj.skc.stats

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.exception.SKCError
import com.rtomyj.skc.model.DatabaseStats
import com.rtomyj.skc.model.MonsterTypeStats
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.reactive.WebFluxLinkBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/stats"], produces = ["application/json; charset=UTF-8"])
@Tag(name = SwaggerConstants.TAG_STATS_NAME)
class StatsController @Autowired constructor(private val statsService: StatsService) : YgoApiBaseController() {

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }

  @Operation(
    summary = "Retrieve sum of all unique monster types for a given color of a card."
  )
  @GetMapping("/card/monster_type/{cardColor}")
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(
    responseCode = "400",
    description = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE,
    content = [Content(schema = Schema(implementation = SKCError::class))]
  )
  @ApiResponse(
    responseCode = "404",
    description = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE,
    content = [Content(schema = Schema(implementation = SKCError::class))]
  )
  fun monsterTypesForgivenCardColor(
    @Parameter(
      description = SwaggerConstants.CARD_COLOR_DESCRIPTION, schema = Schema(
        implementation = String::class, defaultValue = "fusion"
      )
    ) @PathVariable("cardColor") cardColor: String
  ): Mono<EntityModel<MonsterTypeStats>> = ReactiveMDC.deferMDC(
    Mono
        .zip(Mono.fromCallable {
          statsService.getMonsterTypeStats(cardColor)
        }, monsterTypesForgivenCardColorLinks(cardColor))
        .map {
          EntityModel.of(it.t1, listOf(it.t2))
        }
        .doOnSubscribe {
          log.info("Retrieving monster types for cards with color: {}", cardColor)
        })

  private fun monsterTypesForgivenCardColorLinks(cardColor: String): Mono<Link> = WebFluxLinkBuilder
      .linkTo(
        WebFluxLinkBuilder
            .methodOn(this::class.java)
            .monsterTypesForgivenCardColor(
              cardColor
            )
      )
      .withSelfRel()
      .toMono()

  @Operation(
    summary = "Retrieve overview of the data currently in Database."
  )
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(
    responseCode = "400",
    description = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE,
    content = [Content(schema = Schema(implementation = SKCError::class))]
  )
  @ApiResponse(
    responseCode = "404",
    description = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE,
    content = [Content(schema = Schema(implementation = SKCError::class))]
  )
  @GetMapping
  fun databaseStats(): Mono<EntityModel<DatabaseStats>> = ReactiveMDC.deferMDC(Mono
      .zip(Mono.fromCallable { statsService.databaseStats() }, dbStatsLinks())
      .doOnSuccess {
        log.info("Successfully retrieved database stats: {}", it.t1.toString())
      }
      .map {
        EntityModel.of(it.t1, listOf(it.t2))
      }
      .doOnSubscribe { log.info("Retrieving high level overview of info stored in DB.") })


  private fun dbStatsLinks(): Mono<Link> = WebFluxLinkBuilder
      .linkTo(
        WebFluxLinkBuilder
            .methodOn(this::class.java)
            .databaseStats()
      )
      .withSelfRel()
      .toMono()
}