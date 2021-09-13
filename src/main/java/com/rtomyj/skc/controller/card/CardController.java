package com.rtomyj.skc.controller.card;

import com.rtomyj.skc.controller.YgoApiBaseController;
import com.rtomyj.skc.helper.constants.RegexExpressions;
import com.rtomyj.skc.helper.constants.SwaggerConstants;
import com.rtomyj.skc.helper.exceptions.YgoException;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.service.card.CardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

/**
 * Configures endpoint(s) that can be used to get card data for cards stored in database.
 */
@RestController
@RequestMapping(path = "/card", produces = "application/json; charset=UTF-8")
@Slf4j
@Validated
@Api(tags = {SwaggerConstants.TAG_CAR_TAG_NAMED})
public class CardController extends YgoApiBaseController
{

	/**
	 * Service object used to interface with DB DAO.
	 */
	private final CardService cardService;


	@Autowired
	public CardController(final CardService cardService)
	{

		this.cardService = cardService;

	}


	/**
	 * Accepts a cardId which is used to query the DB/Cache to get information about the card.
	 * cardId must be in proper format. A regular expression is used to validate the format. If the format isn't correct, card cannot be looked up.
	 * If Card cannot be looked up, an appropriate HTTP response is sent.
	 *
	 * If the cardId is in proper format, the DB/Cache will be queried. If cardId is found in DB/Cache, a card object will be returned
	 * , else only an appropriate HTTP response is sent.
	 * @param cardId The unique identification of the card desired.
	 * @return Card object as a response.
	 */
	@GetMapping("/{cardId}")
	@ApiOperation(value = "Get information about a specific card."
		, response = Card.class
		, responseContainer = "Object")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = SwaggerConstants.http200)
		, @ApiResponse(code = 400, message = SwaggerConstants.http400)
		, @ApiResponse(code = 404, message = SwaggerConstants.http404)
	})
	public ResponseEntity<Card> getCard(
			@ApiParam(value = SwaggerConstants.CARD_ID_DESCRIPTION
					, example = "40044918"
					, required = true
			) @PathVariable("cardId") @Pattern(regexp = RegexExpressions.CARD_ID_PATTERN, message = "Card ID doesn't have correct format.") final String cardId
			, @ApiParam(value = SwaggerConstants.CARD_FETCH_ALL_DESCRIPTION
					, example = "true"
			) @RequestParam(value = "allInfo", defaultValue = "false") final boolean fetchAllInfo)
		throws YgoException
	{

		log.info("Retrieving card info for : {}.", cardId);
		final Card foundCard = cardService.getCardInfo(cardId, fetchAllInfo);
		log.info("Successfully retrieved card info for: {}, fetching all info: {}.", cardId, fetchAllInfo);

		return ResponseEntity.ok(foundCard);

	}

}