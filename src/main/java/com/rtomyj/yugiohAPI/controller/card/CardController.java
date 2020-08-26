package com.rtomyj.yugiohAPI.controller.card;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.helper.constants.RegexExpressions;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.service.CardService;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Configures endpoint(s) that can be used to get card data for cards stored in database.
 */
@RestController
@RequestMapping(path = "/api/v1/card", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(description = "Request information about card data stored in database.", tags = "Card")
public class CardController
{
	/**
	 * Service object used to interface with DB DAO.
	 */
	private final CardService cardService;

	/**
	 * Object with information about http request.
	 */
	private final HttpServletRequest httpRequest;

	/**
	 * Base url for this endpoint.
	 */
	private static final String endPoint = "/api/v1/card";



	@Autowired
	public CardController(final CardService cardService, final HttpServletRequest httpRequest)
	{
		this.cardService = cardService;
		this.httpRequest = httpRequest;
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
	@ApiOperation(value = "Get information about a specific card"
		, response = Card.class
		, responseContainer = "Object"
		, tags = "Card")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK")
		, @ApiResponse(code = 400, message = "Malformed request, make sure cardId is valid")
		, @ApiResponse(code = 404, message = "No resource for requested card ID")
	})
	public ResponseEntity<Card> getCard(
			@PathVariable("cardId") @Pattern(regexp = RegexExpressions.CARD_ID_PATTERN, message = "Card ID doesn't have correct format.") final String cardId
			, @RequestParam(value = "allInfo", defaultValue = "false") final boolean fetchAllInfo)
		throws YgoException
	{
		MDC.put("reqIp", httpRequest.getRemoteHost());
		MDC.put("reqRes", endPoint);

		final Card foundCard = cardService.getCardInfo(cardId, fetchAllInfo);
		log.info("Successfully retrieved resource: ( {}, fetching all info {}.", cardId, fetchAllInfo);

		MDC.clear();
		return ResponseEntity.ok(foundCard);
	}
}