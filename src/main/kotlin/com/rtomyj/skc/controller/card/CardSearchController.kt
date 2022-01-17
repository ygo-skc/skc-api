package com.rtomyj.skc.controller.card

import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.service.card.CardService
import io.swagger.annotations.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/card/search"], produces = ["application/json; charset=UTF-8"])
@Api(tags = [SwaggerConstants.TAG_CAR_TAG_NAMED])
class CardSearchController @Autowired constructor(
    private val cardService: CardService
) : YgoApiBaseController() {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    @GetMapping
    @ApiOperation(
        value = "Search for a specific set of cards using certain properties. Props don't have to be complete. When partial props are passed, API will return Cards that contain the partial value of given prop. See below for example of partial prop (card name, card ID, monsterType)",
        response = Card::class,
        responseContainer = "List"
    )
    @ApiResponses(value = [ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)])
    @Throws(
        YgoException::class
    )
    fun searchByCriteria(
        @ApiParam(value = SwaggerConstants.CARD_ID_DESCRIPTION, example = "5")
        @RequestParam(name = "cId", required = false, defaultValue = "")
        cardId: String = "",
        @ApiParam(value = SwaggerConstants.CARD_NAME_DESCRIPTION, example = "hero")
        @RequestParam(name = "cName", required = false, defaultValue = "")
        cardName: String = "",
        @ApiParam(
            value = SwaggerConstants.CARD_ATTRIBUTE_DESCRIPTION,
            example = "water"
        )
        @RequestParam(name = "cAttribute", required = false, defaultValue = "")
        cardAttribute: String = "",
        @ApiParam(value = SwaggerConstants.CARD_COLOR_DESCRIPTION, example = "effect")
        @RequestParam(name = "cColor", required = false, defaultValue = "")
        cardColor: String = "",
        @ApiParam(value = SwaggerConstants.MONSTER_TYPE_DESCRIPTION, example = "war")
        @RequestParam(name = "mType", required = false, defaultValue = "")
        monsterType: String = "",
        @ApiParam(value = SwaggerConstants.RESULT_LIMIT_DESCRIPTION, example = "10", defaultValue = "5")
        @RequestParam(name = "limit", required = false, defaultValue = "5")
        limit: Int = 5,
        @ApiParam(
            value = SwaggerConstants.SAVE_BANDWIDTH_DESCRIPTION,
            example = "false",
            defaultValue = "true"
        )
        @RequestParam(name = "saveBandwidth", required = false, defaultValue = "true")
        saveBandwidth: Boolean = true
    ): ResponseEntity<List<Card>> {
        log.info("User is searching for card.")
        val searchResult = cardService.getCardSearchResults(
            cardId,
            cardName,
            cardAttribute,
            cardColor,
            monsterType,
            limit,
            saveBandwidth
        )
        log.info(
            "Successfully retrieved search results using the following criteria: [ cardId={}, cardName={}, cardAttribute={}, cardColor={}, monsterType={}, limit={}, saveBandwidth={} ]. Found {} matching results.",
            cardId,
            cardName,
            cardAttribute,
            cardColor,
            monsterType,
            limit,
            saveBandwidth,
            searchResult.size
        )

        return ResponseEntity(searchResult, HttpStatus.OK)
    }
}