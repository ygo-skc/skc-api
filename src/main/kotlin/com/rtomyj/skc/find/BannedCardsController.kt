package com.rtomyj.skc.find

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.config.SwaggerConfig
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListInstance
import com.rtomyj.skc.util.constant.SKCRegex
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * Configures endpoint(s) that can be used to obtain information about cards for a particular ban list.
 */
@RestController
@RequestMapping(path = ["/ban_list"], produces = ["application/json; charset=UTF-8"])
@Validated
@Tag(name = SwaggerConstants.BAN_LIST_TAG_NAME)
class BannedCardsController
/**
 * Create object instance.
 * @param bannedCardsService Service object to use to accomplish functionality needed by this endpoint.
 */ @Autowired constructor(
  /**
   * Service object used to get information about banned cards from the database.
   */
  val bannedCardsService: BannedCardsService
) {

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }

  /**
   * User can get the contents of a ban list by the start date of the ban list.
   * User data will be validated. A regular expression is used to ensure that the
   * user passed a valid date. The ban list cache will be utilized to speed up
   * delivery. If desired ban list contents are not in query , the ban list
   * contents will be fetched from DB.
   *
   * @param banListStartDate The date the desired ban list took effect.
   * @return ban list for specified ban list start date.
   */
  @ResponseBody
  @GetMapping(path = ["{banListStartDate}/cards"])
  @Operation(
    summary = "Retrieves information about a ban list using a valid effective ban list start date (use /api/v1/ban/dates to see a valid list of start dates).",
    tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
  )
  @ApiResponse(responseCode = "200", description = SwaggerConfig.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "400", ref = "badRequest")
  @ApiResponse(responseCode = "404", ref = "notFound")
  @ApiResponse(responseCode = "500", ref = "internalServerError")
  @Throws(
    SKCException::class
  )
  fun getBannedCards(
    @Parameter(
      description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
      example = "2020-04-01",
      required = true,
      schema = Schema(implementation = String::class)
    ) @Pattern(
      regexp = SKCRegex.DB_DATE, message = "Date doesn't have correct format."
    ) @PathVariable banListStartDate: String, @Parameter(
      description = SwaggerConstants.SAVE_BANDWIDTH_DESCRIPTION,
      required = false,
      schema = Schema(implementation = Boolean::class)
    ) @RequestParam(
      name = "saveBandwidth", required = false, defaultValue = "true"
    ) saveBandwidth: Boolean = true, @RequestParam(
      name = "format", required = true, defaultValue = "TCG"
    ) format: String = "TCG", @Parameter(
      description = SwaggerConstants.BAN_LIST_FETCH_ALL_DESCRIPTION,
      required = false,
      schema = Schema(implementation = Boolean::class)
    ) @RequestParam(
      name = "allInfo", required = false, defaultValue = "false"
    ) fetchAllInfo: Boolean = false
  ): Mono<ResponseEntity<BanListInstance>> = ReactiveMDC.deferMDC(Mono
      .fromCallable { bannedCardsService.getBanListByDate(banListStartDate, saveBandwidth, format, fetchAllInfo) }
      .map { banListInstance ->
        log.info(
          "Successfully retrieved ban list contents for ban list w/ start date {} with saveBandwidth as {} for format {}.",
          banListStartDate,
          saveBandwidth,
          format
        )
        ResponseEntity.ok(banListInstance)
      })
}