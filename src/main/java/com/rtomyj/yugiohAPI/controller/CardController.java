package com.rtomyj.yugiohAPI.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.service.CardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping(path="${ygo.endpoints.v1.card}", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(description = "Request information about card data stored in database.", tags = "Card")
public class CardController
{
	/**
	 * Service object used to interface with DB DAO.
	 */
	@Autowired
	private CardService cardService;

	/**
	 * Object with information about http request.
	 */
	@Autowired
	private HttpServletRequest httpRequest;

	/**
	 * Base url for this endpoint.
	 */
	@Value("${ygo.endpoints.v1.card}")
	private String endPoint;

	/**
	 * Cache used to store card data to prevent querying DB.
	 */
	@Autowired
	@Qualifier("cardsCache")
	private Map<String, Card> CARD_CACHE;



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
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure cardId is valid")
	})
	public ResponseEntity<Card> getCard(
		@PathVariable("cardId") @Pattern(regexp = "[0-9]{8}", message = "Card ID doesn't have correct format.") String cardId)
		throws YgoException
	{
		String requestIP = httpRequest.getRemoteHost();	// IP address of the client accessing endpoint

		Card requestedCard = CARD_CACHE.get(cardId);
		/* If requested card was not found in cache - use DB */
		if (requestedCard == null)
		{
			requestedCard = cardService.getCardInfo(cardId);
			CARD_CACHE.put(cardId, requestedCard);	// puts card into cache
		}


		HttpStatus status = HttpStatus.OK;
		log.info(LogHelper.requestStatusLogString(requestIP, cardId, endPoint, status, false, true));
		return new ResponseEntity<>(requestedCard, status);
	}
}