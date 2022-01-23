package com.rtomyj.skc.controller.card

import com.rtomyj.skc.constant.SKCRegex
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.service.card.CardService
import io.swagger.annotations.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * Configures endpoint(s) that can be used to get card data for cards stored in database.
 */
@RestController
@RequestMapping(path = ["/card"], produces = ["application/json; charset=UTF-8"])
@Validated
@Api(tags = [SwaggerConstants.TAG_CAR_TAG_NAMED])
class CardController @Autowired constructor(
    /**
     * Service object used to interface with DB DAO.
     */
    private val cardService: CardService
) : YgoApiBaseController() {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    /**
     * Accepts a cardId which is used to query the DB/Cache to get information about the card.
     * cardId must be in proper format. A regular expression is used to validate the format. If the format isn't correct, card cannot be looked up.
     * If Card cannot be looked up, an appropriate HTTP response is sent.
     *
     * If the cardId is in proper format, the DB/Cache will be queried. If cardId is found in DB/Cache, a card object will be returned
     * , else only an appropriate HTTP response is sent.
     * @param cardId The unique identification of the card desired.
     * @param fetchAllInfo Whether all info for specified card should be fetched.
     * @return Card object as a response.
     */
    @GetMapping("/{cardId}")
    @ApiOperation(
        value = "Get information about a specific card.",
        response = Card::class,
        responseContainer = "Object"
    )

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE),
            ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE),
            ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE),
            ApiResponse(code = 500, message = SwaggerConstants.HTTP_500_SWAGGER_MESSAGE)
        ]
    )
    @Throws(YgoException::class)
    fun getCard(
        @ApiParam(
            value = SwaggerConstants.CARD_ID_DESCRIPTION,
            example = "40044918",
            required = true
        )
        @NotNull
        @Pattern(
            regexp = SKCRegex.CARD_ID,
            message = "Card ID doesn't have correct format."
        )
        @PathVariable("cardId") cardId: String,
        @ApiParam(
            value = SwaggerConstants.CARD_FETCH_ALL_DESCRIPTION,
            example = "true"
        )
        @RequestParam(value = "allInfo", defaultValue = "false") fetchAllInfo: Boolean = false
    ): ResponseEntity<Card> {
        log.info("Retrieving card info for using ID: {}.", cardId)
        val foundCard = cardService.getCardInfo(cardId, fetchAllInfo)
        log.info(
            "Successfully retrieved card info for: {}, w/ all info: {}.",
            cardId,
            fetchAllInfo
        )

        return ResponseEntity.ok(foundCard)
    }
}