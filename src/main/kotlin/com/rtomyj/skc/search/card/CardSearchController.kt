package com.rtomyj.skc.search.card

import com.rtomyj.skc.browse.card.model.Card
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.util.YgoApiBaseController
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = SwaggerConstants.TAG_CARD_TAG_NAMED)
class CardSearchController @Autowired constructor(
	private val cardSearchService: CardSearchService
) : YgoApiBaseController() {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	@GetMapping
	@Operation(
		summary = "Search for a specific set of cards using certain properties. Props don't have to be complete. When partial props are passed, API will return Cards that contain the partial value of given prop. See below for example of partial prop (card name, card ID, monsterType)",
	)
	@ApiResponse(
		responseCode = "200",
		description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
	)
	@Throws(
		YgoException::class
	)
	fun searchByCriteria(
		@Parameter(
			description = SwaggerConstants.CARD_ID_DESCRIPTION,
			example = "5",
			schema = Schema(implementation = Int::class)
		)
		@RequestParam(name = "cId", required = false, defaultValue = "")
		cardId: String = "",
		@Parameter(
			description = SwaggerConstants.CARD_NAME_DESCRIPTION,
			example = "hero",
			schema = Schema(implementation = String::class)
		)
		@RequestParam(name = "cName", required = false, defaultValue = "")
		cardName: String = "",
		@Parameter(
			description = SwaggerConstants.CARD_ATTRIBUTE_DESCRIPTION,
			example = "water",
			schema = Schema(implementation = String::class)
		)
		@RequestParam(name = "cAttribute", required = false, defaultValue = "")
		cardAttribute: String = "",
		@Parameter(
			description = SwaggerConstants.CARD_COLOR_DESCRIPTION,
			example = "effect",
			schema = Schema(implementation = String::class)
		)
		@RequestParam(name = "cColor", required = false, defaultValue = "")
		cardColor: String = "",
		@Parameter(
			description = SwaggerConstants.MONSTER_TYPE_DESCRIPTION,
			example = "war",
			schema = Schema(implementation = String::class)
		)
		@RequestParam(name = "mType", required = false, defaultValue = "")
		monsterType: String = "",
		@Parameter(
			description = SwaggerConstants.RESULT_LIMIT_DESCRIPTION,
			example = "10",
			schema = Schema(implementation = Int::class, defaultValue = "5")
		)
		@RequestParam(name = "limit", required = false, defaultValue = "5")
		limit: Int = 5,
		@Parameter(
			description = SwaggerConstants.SAVE_BANDWIDTH_DESCRIPTION,
			example = "false",
			schema = Schema(implementation = Boolean::class, defaultValue = "false")
		)
		@RequestParam(name = "saveBandwidth", required = false, defaultValue = "true")
		saveBandwidth: Boolean = true
	): ResponseEntity<List<Card>> {
		log.info("User is searching for card.")
		val searchResult = cardSearchService.getCardSearchResults(
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