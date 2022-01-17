package com.rtomyj.skc.controller.banlist

import com.rtomyj.skc.constant.SKCRegex
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListInstance
import com.rtomyj.skc.service.banlist.BannedCardsService
import io.swagger.annotations.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Pattern

/**
 * Configures endpoint(s) that can be used to obtain information about cards for a particular ban list.
 */
@RestController
@RequestMapping(path = ["/ban_list"], produces = ["application/hal+json; charset=UTF-8"])
@Validated
@Api(tags = [SwaggerConstants.BAN_LIST_TAG_NAME])
class BannedCardsController
/**
 * Create object instance.
 * @param bannedCardsService Service object to use to accomplish functionality needed by this endpoint.
 */ @Autowired constructor(
    /**
     * Service object used to get information about banned cards from the database.
     */
    val bannedCardsService: BannedCardsService
) : YgoApiBaseController() {

    companion object{
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
    @ApiOperation(
        value = "Retrieves information about a ban list using a valid effective ban list start date (use /api/v1/ban/dates to see a valid list of start dates).",
        response = BanListInstance::class,
        responseContainer = "Object",
        tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
    )
    @ApiResponses(
        value = [ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE), ApiResponse(
            code = 400,
            message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE
        ), ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE), ApiResponse(
            code = 500,
            message = SwaggerConstants.HTTP_500_SWAGGER_MESSAGE
        )]
    )
    @Throws(
        YgoException::class
    )
    fun getBannedCards(
        @ApiParam(
            value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
            example = "2020-04-01",
            required = true
        )
        @Pattern(
            regexp = SKCRegex.DB_DATE,
            message = "Date doesn't have correct format."
        )
        @PathVariable banListStartDate: String,
        @ApiParam(value = SwaggerConstants.SAVE_BANDWIDTH_DESCRIPTION)
        @RequestParam(
            name = "saveBandwidth",
            required = false,
            defaultValue = "true"
        ) saveBandwidth: Boolean = true,
        @ApiParam(value = SwaggerConstants.BAN_LIST_FETCH_ALL_DESCRIPTION)
        @RequestParam(
            name = "allInfo",
            required = false,
            defaultValue = "false"
        ) fetchAllInfo: Boolean = false
    ): ResponseEntity<BanListInstance> {
        val reqBanListInstance = bannedCardsService.getBanListByDate(banListStartDate, saveBandwidth, fetchAllInfo)
        log.info(
            "Successfully retrieved ban list: ( {} ) with saveBandwidth: ( {} ).",
            banListStartDate,
            saveBandwidth
        )
        return ResponseEntity.ok(reqBanListInstance)
    }
}