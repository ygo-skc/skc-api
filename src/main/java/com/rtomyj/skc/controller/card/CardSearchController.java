package com.rtomyj.skc.controller.card;

import com.rtomyj.skc.controller.YgoApiBaseController;
import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.service.card.CardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/card/search", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = {"*"})
@Api(tags = {SwaggerConstants.TAG_CAR_TAG_NAMED})
@Slf4j
public class CardSearchController extends YgoApiBaseController
{

	private final CardService cardService;


	@Autowired
	public CardSearchController(final CardService cardService)
	{

		this.cardService = cardService;

	}


	@GetMapping
	@ApiOperation(value = "Search for a specific set of cards using certain properties. Props don't have to be complete. When partial props are passed, API will return Cards that contain the partial value of given prop. See below for example of partial prop (card name, card ID, monsterType)"
			, response = Card.class
			, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.http200)
	})
	public ResponseEntity<List<Card>> searchByCriteria(
			@ApiParam(value = SwaggerConstants.CARD_ID_DESCRIPTION
					, example = "5"
			) @RequestParam(name = "cId", required = false, defaultValue="") final String cardId
			, @ApiParam(value = SwaggerConstants.CARD_NAME_DESCRIPTION
					, example = "hero"
			) @RequestParam(name = "cName", required = false, defaultValue="") final String cardName
			, @ApiParam(value = SwaggerConstants.CARD_ATTRIBUTE_DESCRIPTION
					, example = "water"
			) @RequestParam(name = "cAttribute", required = false, defaultValue="") final String cardAttribute
			, @ApiParam(value = SwaggerConstants.CARD_COLOR_DESCRIPTION
					, example = "effect"
			) @RequestParam(name = "cColor", required = false, defaultValue="") final String cardColor
			, @ApiParam(value = SwaggerConstants.MONSTER_TYPE_DESCRIPTION
					, example = "war"
			) @RequestParam(name = "mType", required = false, defaultValue="") final String monsterType
			, @ApiParam(value = SwaggerConstants.RESULT_LIMIT_DESCRIPTION
					, example = "5"
					, defaultValue = "-1"
			) @RequestParam(name = "limit", required = false, defaultValue = "-1") final int limit
			, @ApiParam(value = SwaggerConstants.SAVE_BANDWIDTH_DESCRIPTION
					, example = "false"
					, defaultValue = "true"
			) @RequestParam(name = "saveBandwidth", required = false, defaultValue = "true") final boolean saveBandwidth)
			throws YgoException
	{

		log.info("User is searching for card.");
		final List<Card> searchResult = cardService.getCardSearchResults(cardId, cardName, cardAttribute, cardColor, monsterType, limit, saveBandwidth);
		log.info("Successfully retrieved search results using the following criteria: [ cardId={}, cardName={}, cardAttribute={}, cardColor={}, monsterType={}, limit={}, saveBandwidth={} ]. Found {} matching results."
				, cardId, cardName, cardAttribute, cardColor, monsterType, limit, saveBandwidth, searchResult.size());

		return new ResponseEntity<>(searchResult, HttpStatus.OK);

	}

}