package com.rtomyj.skc.find

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.exception.SKCError
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListNewContent
import com.rtomyj.skc.model.BanListRemovedContent
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.util.constant.SKCRegex
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Links
import org.springframework.hateoas.server.reactive.WebFluxLinkBuilder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Class used as a REST controller for retrieving cards added to a particular ban list compared to previous ban list
 * or cards that switched statuses (Forbidden -&gt; limited, limited -&gt; semi-limited, etc) compared with the previous ban list.
 */
@RestController
@RequestMapping(path = ["/ban_list"], produces = ["application/json; charset=UTF-8"])
@Validated
@Tag(name = SwaggerConstants.BAN_LIST_TAG_NAME)
class BanListDiffController
/**
 * Create object instance.
 * @param banListDiffService Service object to use to accomplish functionality needed by this endpoint.
 */ @Autowired constructor(
  /**
   * Service used to interface with dao.
   */
  val banListDiffService: BanListDiffService
) : YgoApiBaseController() {

  companion object {
    @JvmStatic
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }


  /**
   *
   * @param banListStartDate The date of a ban list user wants to see new card information about.
   * @return Information about the new cards for the specified ban list date.
   */
  @GetMapping(path = ["/{banListStartDate}/new"])
  @Operation(
    summary = "Retrieve cards that are either newly added to desired ban list or cards that have switched statuses (ie: from forbidden to limited) relative to desired ban list " + "using a valid start/effective date of a ban list (use /api/v1/ban/dates to see a valid list of start dates).",
    tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
  )
  @ApiResponse(
    responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
  )
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
  @ApiResponse(
    responseCode = "500",
    description = SwaggerConstants.HTTP_500_SWAGGER_MESSAGE,
    content = [Content(schema = Schema(implementation = SKCError::class))]
  )
  @Throws(
    SKCException::class
  )
  fun getNewlyAddedContentForBanList(
    @Parameter(
      description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
      example = "2020-04-01",
      required = true,
      schema = Schema(implementation = String::class)
    ) @NotNull @Pattern(
      regexp = SKCRegex.DB_DATE, message = "Date doesn't have correct format."
    ) @PathVariable banListStartDate: String, @RequestParam(
      name = "format", required = true, defaultValue = "TCG"
    ) format: String = "TCG"
  ): Mono<EntityModel<BanListNewContent>> = ReactiveMDC.deferMDC(Mono
      .zip(Mono
          .fromCallable {
            banListDiffService.getNewContentForGivenBanList(
              banListStartDate, format
            )
          }
          .doOnSuccess { banListNewContent ->
            if (format == "DL") {
              log.info(
                "Successfully retrieved new content for ban list w/ start date {} for format {}, using previous ban list ({}) for comparison. Newly... forbidden ({}), limited 1 ({}), limited 2 ({}), limited 3 ({})",
                banListNewContent.listRequested,
                format,
                banListNewContent.comparedTo,
                banListNewContent.numNewForbidden,
                banListNewContent.numNewLimitedOne,
                banListNewContent.numNewLimitedTwo,
                banListNewContent.numNewLimitedThree
              )
            } else {
              log.info(
                "Successfully retrieved new content for ban list {} for format {}, using previous ban list ({}) for comparison. Newly... forbidden ({}), limited ({}), semi-limited ({})",
                banListNewContent.listRequested,
                format,
                banListNewContent.comparedTo,
                banListNewContent.numNewForbidden,
                banListNewContent.numNewLimited,
                banListNewContent.numNewSemiLimited
              )
            }

          }, newlyAddedContentLinks(banListStartDate, format)
      )
      .map {
        EntityModel.of(it.t1, it.t2)
      }
      .doOnSubscribe {
        log.info(
          "Retrieving new ban list content for ban list w/ start date {} using format {}", banListStartDate, format
        )
      })


  private fun newlyAddedContentLinks(banListStartDate: String, format: String): Mono<Links> = Flux
      .merge(
        WebFluxLinkBuilder
            .linkTo(
              WebFluxLinkBuilder
                  .methodOn(this::class.java)
                  .getNewlyAddedContentForBanList(banListStartDate, format)
            )
            .withSelfRel()
            .toMono(),
        WebFluxLinkBuilder
            .linkTo(
              WebFluxLinkBuilder
                  .methodOn(this::class.java)
                  .getNewlyRemovedContentForBanList(banListStartDate, format)
            )
            .withRel("Ban List Removed Content")
            .toMono(),
        WebFluxLinkBuilder
            .linkTo(
              WebFluxLinkBuilder
                  .methodOn(BannedCardsController::class.java)
                  .getBannedCards(banListStartDate, false, format, true)
            )
            .withRel("Ban List Content")
            .toMono()
      )
      .collectList()
      .map {
        Links.of(it)
      }

  @GetMapping(path = ["/{banListStartDate}/removed"])
  @Operation(
    summary = "Retrieve cards removed from the desired ban list compared to the previous logical ban list (use /api/v1/ban/dates to see a valid list of start dates).",
    tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
  )
  @ApiResponse(
    responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
  )
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
  @ApiResponse(
    responseCode = "500",
    description = SwaggerConstants.HTTP_500_SWAGGER_MESSAGE,
    content = [Content(schema = Schema(implementation = SKCError::class))]
  )
  @Throws(
    SKCException::class
  )
  fun getNewlyRemovedContentForBanList(
    @Parameter(
      description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
      example = "2020-04-01",
      required = true,
      schema = Schema(implementation = String::class)
    ) @NotNull @Pattern(
      regexp = SKCRegex.DB_DATE, message = "Date doesn't have correct format."
    ) @PathVariable(name = "banListStartDate") banListStartDate: String, @RequestParam(
      name = "format", required = true, defaultValue = "TCG"
    ) format: String = "TCG"
  ): Mono<EntityModel<BanListRemovedContent>> = ReactiveMDC.deferMDC(Mono
      .zip(Mono
          .fromCallable { banListDiffService.getRemovedContentForGivenBanList(banListStartDate, format) }
          .doOnSuccess { banListRemovedContent ->
            log.info(
              "Successfully retrieved removed content for ban list w/ start date {} for format {}. Newly removed ({})",
              banListStartDate,
              format,
              banListRemovedContent.numRemoved
            )
          }, removedContentLinks(banListStartDate, format)
      )
      .map {
        EntityModel.of(it.t1, it.t2)
      }
      .doOnSubscribe {
        log.info(
          "Retrieving removed ban list content for ban list w/ start date {} using format {}",
          banListStartDate,
          format
        )
      })


  private fun removedContentLinks(banListStartDate: String, format: String): Mono<Links> = Flux
      .merge(
        WebFluxLinkBuilder
            .linkTo(
              WebFluxLinkBuilder
                  .methodOn(this::class.java)
                  .getNewlyRemovedContentForBanList(banListStartDate, format)
            )
            .withSelfRel()
            .toMono(),
        WebFluxLinkBuilder
            .linkTo(
              WebFluxLinkBuilder
                  .methodOn(BannedCardsController::class.java)
                  .getBannedCards(banListStartDate, false, format, true)
            )
            .withRel("Ban List Content")
            .toMono(),
        WebFluxLinkBuilder
            .linkTo(
              WebFluxLinkBuilder
                  .methodOn(this::class.java)
                  .getNewlyAddedContentForBanList(banListStartDate, format)
            )
            .withRel("Ban List New Content")
            .toMono()
      )
      .collectList()
      .map {
        Links.of(it)
      }
}