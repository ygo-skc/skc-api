package com.rtomyj.skc.find

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.model.BanListDates
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * Configures endpoint(s) for returning user the dates of the ban lists in the database.
 */
@RestController
@RequestMapping(path = ["/ban_list/dates"], produces = ["application/json; charset=UTF-8"])
@Tag(name = SwaggerConstants.BAN_LIST_TAG_NAME)
class BanListDatesController
/**
 * Create object instance.
 * @param banListDatesService Service object to use to accomplish functionality needed by this endpoint.
 */ @Autowired constructor(
  /**
   * Service object used to interface the database DAO
   */
  val banListDatesService: BanListDatesService
) {

  companion object {
    @JvmStatic
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }


  /**
   * Looks in the database for the start dates of all ban lists stored in database.
   * @return Map that contains a list of all dates of the ban lists in database.
   */
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "422", ref = "Unprocessable Entity")
  @ApiResponse(responseCode = "500", ref = "Internal Server Error")
  @Operation(
    summary = "Retrieve start (effective) dates of all ban lists stored in database in logical order. These dates are \"valid\" start dates that can be used by other endpoints ban list endpoints.",
    tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
  )
  @GetMapping
  fun banListStartDates(
    @RequestParam(
      name = "format", required = true, defaultValue = "TCG"
    ) format: String = "TCG"
  ): Mono<BanListDates> = ReactiveMDC.deferMDC(banListDatesService
      .retrieveBanListStartDates(format)
      .doOnSuccess {
        log.info(
          "Successfully retrieved all effective start dates for ban list using format {}. Currently there are {} ban lists",
          format,
          it.dates.size
        )
      }
      .doOnSubscribe {
        log.info("Retrieving all effective start dates for ban lists using format {}.", format)
      })
}