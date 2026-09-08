package com.rtomyj.skc.find

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.config.SwaggerConfig
import com.rtomyj.skc.config.blockingJDBCMono
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListNewContent
import com.rtomyj.skc.model.BanListRemovedContent
import com.rtomyj.skc.util.constant.SKCRegex
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * Class used as a REST controller for retrieving cards added to a particular ban list compared to previous ban list
 * or cards that switched statuses (Forbidden -&gt; limited, limited -&gt; semi-limited, etc.) compared with the previous ban list.
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
) {

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
  @ApiResponse(responseCode = "200", description = SwaggerConfig.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "400", ref = "badRequest")
  @ApiResponse(responseCode = "404", ref = "notFound")
  @ApiResponse(responseCode = "422", ref = "unprocessableEntity")
  @ApiResponse(responseCode = "500", ref = "internalServerError")
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
  ): Mono<BanListNewContent> = ReactiveMDC.deferMDC(
    blockingJDBCMono {
      banListDiffService.getNewContentForGivenBanList(
        banListStartDate, format
      )
    }
        .doOnNext { banListNewContent ->
          if (format == "DL") {
            log.info(
              "Retrieved new content for ban list w/ start date {} & format {}, using previous ban list ({}) for comparison. Newly; forbidden ({}), limited 1 ({}), limited 2 ({}), limited 3 ({})",
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
              "Retrieved new content for ban list {} & format {}, using previous ban list ({}) for comparison. Newly; forbidden ({}), limited ({}), semi-limited ({})",
              banListNewContent.listRequested,
              format,
              banListNewContent.comparedTo,
              banListNewContent.numNewForbidden,
              banListNewContent.numNewLimited,
              banListNewContent.numNewSemiLimited
            )
          }

        }
        .doOnSubscribe {
          log.info(
            "Retrieving new ban list content for ban list w/ start date {} & format {}", banListStartDate, format
          )
        })

  @GetMapping(path = ["/{banListStartDate}/removed"])
  @Operation(
    summary = "Retrieve cards removed from the desired ban list compared to the previous logical ban list (use /api/v1/ban/dates to see a valid list of start dates).",
    tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
  )
  @ApiResponse(responseCode = "200", description = SwaggerConfig.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "400", ref = "badRequest")
  @ApiResponse(responseCode = "404", ref = "notFound")
  @ApiResponse(responseCode = "422", ref = "unprocessableEntity")
  @ApiResponse(responseCode = "500", ref = "internalServerError")
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
  ): Mono<BanListRemovedContent> = ReactiveMDC.deferMDC(
    blockingJDBCMono { banListDiffService.getRemovedContentForGivenBanList(banListStartDate, format) }
        .doOnNext {
          log.info(
            "Retrieved removed content for ban list w/ start date {} for format {}. Newly removed ({})",
            banListStartDate,
            format,
            it.numRemoved
          )
        }
        .doOnSubscribe {
          log.info("Retrieving removed content for ban list w/ start date {} & format {}", banListStartDate, format)
        })

}