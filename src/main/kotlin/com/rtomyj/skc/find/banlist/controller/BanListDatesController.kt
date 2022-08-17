package com.rtomyj.skc.find.banlist.controller

import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.exception.SKCError
import com.rtomyj.skc.find.banlist.model.BanListDates
import com.rtomyj.skc.find.banlist.service.BanListDatesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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
) : YgoApiBaseController() {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    /**
     * Looks in the database for the start dates of all ban lists stored in database.
     * @return Map that contains a list of all dates of the ban lists in database.
     */
    @ApiResponse(
        responseCode = "200",
        description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
    )
    @ApiResponse(
        responseCode = "500",
        description = SwaggerConstants.HTTP_500_SWAGGER_MESSAGE,
        content = [Content(schema = Schema(implementation = SKCError::class))]
    )
    @Operation(
        summary = "Retrieve start (effective) dates of all ban lists stored in database in logical order. These dates are \"valid\" start dates that can be used by other endpoints ban list endpoints.",
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