package com.rtomyj.skc.controller.stats

import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.model.stats.DatabaseStats
import com.rtomyj.skc.model.stats.MonsterTypeStats
import com.rtomyj.skc.service.stats.StatsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/stats"])
@Tag(name = SwaggerConstants.TAG_STATS_NAME)
class StatsController @Autowired constructor(private val statsService: StatsService) : YgoApiBaseController() {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	@Operation(
		summary = "Retrieve sum of all unique monster types for a given color of a card."
	)
	@GetMapping("/card/monster_type/{cardColor}")
	@ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
	@ApiResponse(responseCode = "400", description = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE)
	@ApiResponse(responseCode = "404", description = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
	fun monsterTypesForgivenCardColor(
		@Parameter(
			name = SwaggerConstants.CARD_COLOR_DESCRIPTION,
			schema = Schema(
				implementation = String::class,
				defaultValue = "fusion"
			)
		) @PathVariable("cardColor") cardColor: String
	): ResponseEntity<MonsterTypeStats> {
		log.info("Retrieving monster types for cards with color: {}", cardColor)
		return ResponseEntity.ok(statsService.getMonsterTypeStats(cardColor))
	}

	@Operation(
		summary = "Retrieve overview of the data currently in Database."
	)
	@ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
	@ApiResponse(responseCode = "400", description = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE)
	@ApiResponse(responseCode = "404", description = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
	@GetMapping
	fun databaseStats(): ResponseEntity<DatabaseStats> {
		log.info("Retrieving high level overview of info stored in DB.")
		return ResponseEntity.ok(statsService.databaseStats())
	}
}