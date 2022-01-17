package com.rtomyj.skc.controller.banlist

import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.model.banlist.BanListDates
import com.rtomyj.skc.service.banlist.BanListDatesService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Configures endpoint(s) for returning user the dates of the ban lists in the database.
 */
@RestController
@RequestMapping(path = ["/ban_list/dates"], produces = ["application/json; charset=UTF-8"])
@Api(tags = [SwaggerConstants.BAN_LIST_TAG_NAME])
class BanListDatesController
/**
 * Create object instance.
 * @param banListDatesService Service object to use to accomplish functionality needed by this endpoint.
 */ @Autowired constructor(
    /**
     * Service object used to interface the database DAO
     */
    val banListDatesService: BanListDatesService
) : YgoApiBaseController() {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    /**
     * Looks in the database for the start dates of all ban lists stored in database.
     * @return Map that contains a list of all dates of the ban lists in database.
     */
    @ApiResponses(
        value = [ApiResponse(
            code = 200,
            message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
        ), ApiResponse(code = 500, message = SwaggerConstants.HTTP_500_SWAGGER_MESSAGE)]
    )
    @ApiOperation(
        value = "Retrieve start (effective) dates of all ban lists stored in database in logical order. These dates are \"valid\" start dates that can be used by other endpoints ban list endpoints.",
        response = BanListDates::class,
        tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
    )
    @GetMapping
    fun banListStartDates(): ResponseEntity<BanListDates> {
        log.info("User is retrieving all effective start dates for ban lists.")

        val banListDates = banListDatesService.retrieveBanListStartDates()
        log.info(
            "Successfully retrieved all effective start dates for ban list. Currently there are {} ban lists",
            banListDates.dates.size
        )

        return ResponseEntity.ok(banListDates)
    }
}