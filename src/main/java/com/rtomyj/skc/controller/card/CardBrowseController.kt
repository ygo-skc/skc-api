package com.rtomyj.skc.controller.card

import com.google.common.base.Suppliers
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.model.card.CardBrowseCriteria
import com.rtomyj.skc.model.card.CardBrowseResults
import com.rtomyj.skc.service.card.CardBrowseService
import io.swagger.annotations.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping(path = ["/card/browse"], produces = ["application/json; charset=UTF-8"])
@Api(tags = [SwaggerConstants.TAG_CAR_TAG_NAMED])
class CardBrowseController @Autowired constructor(
    private val cardBrowseService: CardBrowseService
    ) : YgoApiBaseController() {

    private val cardBrowseCriteriaSupplier = Suppliers.memoizeWithExpiration({ cardBrowseService.browseCriteria }, 10, TimeUnit.MINUTES)

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    @GetMapping
    @ApiOperation(
        value = "Fetches cards given a set of criteria (use /api/v1/browse/criteria for valid criteria).",
        response = CardBrowseResults::class,
        responseContainer = "Object"
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE),
            ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE),
            ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
        ]
    )
    fun browse(
        @ApiParam(
            value = "Desired set of card types to include in browse results.",
            example = "effect,fusion"
        ) @RequestParam(value = "cardColors", defaultValue = "") cardColors: String = "",
        @ApiParam(
            value = "Desired set of attributes to include in browse results.",
            example = "wind,dark,light"
        ) @RequestParam(value = "attributes", defaultValue = "") attributes: String = "",
        @ApiParam(
            value = "Desired set of monster types to include in browse results.",
            example = "spellcaster,wyrm,warrior"
        ) @RequestParam(value = "monsterTypes", defaultValue = "") monsterTypes: String = "",
        @ApiParam(
            value = "Desired set of monster sub types to include in browse results.",
            example = "flip,gemini,toon"
        ) @RequestParam(value = "monsterSubTypes", defaultValue = "") monsterSubTypes: String = "",
        @ApiParam(
            value = "Desired set of monster levels to include in browse results.",
            example = "4,5,6,7,8"
        ) @RequestParam(value = "levels", defaultValue = "") monsterLevels: String = "",
        @ApiParam(
            value = "Desired set of monster ranks to include in browse results.",
            example = "4,7,8"
        ) @RequestParam(value = "ranks", defaultValue = "") monsterRanks: String = "",
        @ApiParam(
            value = "Desired set of monster ranks to include in browse results.",
            example = "4,7,8"
        ) @RequestParam(value = "linkRatings", defaultValue = "") monsterLinkRatings: String = ""
    ): CardBrowseResults {
        log.info("Retrieving browse results.")
        val cardBrowseResults = cardBrowseService.getBrowseResults(
            cardColors,
            attributes,
            monsterTypes,
            monsterSubTypes,
            monsterLevels,
            monsterRanks,
            monsterLinkRatings
        )
        log.info(
            "Successfully retrieved card browse results using criteria: [ cardColors={}, attributes={}, monsterTypes={}, monsterLevels={}, monsterRanks={}, monsterLinkRatings={} ]. Found {} matching results.",
            cardColors,
            attributes,
            monsterTypes,
            monsterLevels,
            monsterRanks,
            monsterLinkRatings,
            cardBrowseResults
        )

        return cardBrowseResults
    }

    @GetMapping("/criteria")
    @ApiOperation(
        value = "Fetches valid criteria and valid values for each criteria that can be used in browse endpoint.",
        response = CardBrowseCriteria::class,
        responseContainer = "Object"
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE),
            ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE),
            ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
        ]
    )
    fun browseCriteria(): CardBrowseCriteria {
        log.info("Retrieving browse criteria.")
        val cardBrowseCriteria = cardBrowseCriteriaSupplier.get()
        log.info("Successfully retrieved browse criteria for cards.")

        return cardBrowseCriteria
    }
}