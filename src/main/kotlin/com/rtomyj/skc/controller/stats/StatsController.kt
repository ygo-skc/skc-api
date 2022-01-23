package com.rtomyj.skc.controller.stats

import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.model.stats.DatabaseStats
import com.rtomyj.skc.model.stats.MonsterTypeStats
import com.rtomyj.skc.service.stats.StatsService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/stats"])
@Api(tags = [SwaggerConstants.TAG_STATS_NAME])
class StatsController @Autowired constructor(private val statsService: StatsService) : YgoApiBaseController() {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	@ApiOperation(
		value = "Retrieve sum of all unique monster types for a given color of a card.",
		response = MonsterTypeStats::class,
		responseContainer = "Object"
	)
	@ApiResponses(
		value = [
			ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE),
			ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE),
			ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
		]
	)
	@GetMapping("/card/monster_type/{cardColor}")
	fun monsterTypesForgivenCardColor(
		@ApiParam(
			value = SwaggerConstants.CARD_COLOR_DESCRIPTION,
			defaultValue = "fusion"
		) @PathVariable("cardColor") cardColor: String
	): ResponseEntity<MonsterTypeStats> {
		log.info("Retrieving monster types for cards with color: {}", cardColor)
		return ResponseEntity.ok(statsService.getMonsterTypeStats(cardColor))
	}

	@ApiOperation(
		value = "Retrieve overview of the data currently in Database.",
		response = DatabaseStats::class,
		responseContainer = "Object"
	)
	@ApiResponses(
		value = [
			ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE),
			ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE),
			ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
		]
	)
	@GetMapping
	fun databaseStats(): ResponseEntity<DatabaseStats> {
		log.info("Retrieving high level overview of info stored in DB.")
		return ResponseEntity.ok(statsService.databaseStats())
	}
}