package com.rtomyj.skc.find.banlist.controller

import com.rtomyj.skc.util.constant.SKCRegex
import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.exception.SKCError
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.find.banlist.model.BanListNewContent
import com.rtomyj.skc.find.banlist.model.BanListRemovedContent
import com.rtomyj.skc.find.banlist.service.DiffService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

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
    val banListDiffService: DiffService
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
        summary = "Retrieve cards that are either newly added to desired ban list or cards that have switched statuses (ie: from forbidden to limited) relative to desired ban list " +
                "using a valid start/effective date of a ban list (use /api/v1/ban/dates to see a valid list of start dates).",
        tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
    )
    @ApiResponse(
        responseCode = "200",
        description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
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
        )
        @NotNull
        @Pattern(
            regexp = SKCRegex.DB_DATE,
            message = "Date doesn't have correct format."
        )
        @PathVariable banListStartDate: String
    ): ResponseEntity<BanListNewContent> {
        log.info("User is requesting new content for ban list: {}", banListStartDate)

        val banListNewContent = banListDiffService.getNewContentForGivenBanList(banListStartDate)
        log.info(
            "Successfully retrieved new content for ban list ({}) using previous ban list ({}) for comparison. Newly... forbidden ({}), limited ({}), semi-limited ({})",
            banListNewContent.listRequested,
            banListNewContent.comparedTo,
            banListNewContent.numNewForbidden,
            banListNewContent.numNewLimited,
            banListNewContent.numNewSemiLimited
        )

        return ResponseEntity.ok(banListNewContent)
    }

    @GetMapping(path = ["/{banListStartDate}/removed"])
    @Operation(
        summary = "Retrieve cards removed from the desired ban list compared to the previous logical ban list (use /api/v1/ban/dates to see a valid list of start dates).",
        tags = [SwaggerConstants.BAN_LIST_TAG_NAME]
    )
    @ApiResponse(
        responseCode = "200",
        description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
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
        )
        @NotNull
        @Pattern(
            regexp = SKCRegex.DB_DATE,
            message = "Date doesn't have correct format."
        )
        @PathVariable(name = "banListStartDate") banListStartDate: String
    ): ResponseEntity<BanListRemovedContent> {
        val banListRemovedContent = banListDiffService.getRemovedContentForGivenBanList(banListStartDate)
        log.info("Successfully retrieved removed content for banlist: ( {} ).", banListStartDate)

        return ResponseEntity.ok(banListRemovedContent)
    }
}