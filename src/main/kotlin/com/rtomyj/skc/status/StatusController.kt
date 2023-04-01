package com.rtomyj.skc.status

import com.rtomyj.skc.dao.StatusDao
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.DownstreamStatus
import com.rtomyj.skc.model.StatusResponse
import com.rtomyj.skc.model.SuggestionEngineDownstreamStatus
import com.rtomyj.skc.skcsuggestionengine.status.SuggestionEngineStatusService
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Configures endpoint(s) for testing the health of the API.
 */
@RestController
@RequestMapping(path = ["/status"], produces = ["application/json; charset=UTF-8"])
@Tag(name = SwaggerConstants.TEST_CALL_TAG_NAME)
class StatusController @Autowired constructor(
	@Qualifier("jdbc") val dao: StatusDao,
	val suggestionEngineStatusService: SuggestionEngineStatusService
) : YgoApiBaseController() {

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}


	/**
	 * Retrieve basic info of the API and status on all dependant downstream services.
	 * @return Status info.
	 */
	@GetMapping
	@Operation(
		summary = "Checking status of the API.",
		tags = [SwaggerConstants.TEST_CALL_TAG_NAME]
	)
	@ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
	fun status(): ResponseEntity<StatusResponse> {
		log.info("Status of API was requested")

		// get status of SKC DB
		val downstreamStatus = mutableListOf(dao.dbConnection())

		try {
			// get status of SKC Suggestion Engine
			val suggestionEngineStatus = suggestionEngineStatusService.getStatus()

			// get a list of services used by Suggestion Engine whose status isn't "Up"
			val failedSuggestionEngineDownstreamServices =
				suggestionEngineStatus.downstream.filter { status -> status.status != "Up" }.map(
					SuggestionEngineDownstreamStatus::serviceName
				)

			// either display a happy status, or display the names of all services used by Suggestion Engine that are down.
			val suggestionEngineStatusString =
				if (failedSuggestionEngineDownstreamServices.isEmpty()) "All good 😛" else "The following services are offline: ${failedSuggestionEngineDownstreamServices.joinToString()}"

			downstreamStatus.add(DownstreamStatus("SKC Suggestion Engine", suggestionEngineStatus.version, suggestionEngineStatusString))
		} catch (e: SKCException) {
			downstreamStatus.add(DownstreamStatus("SKC Suggestion Engine", "N/A", "API is down."))
		}

		return ResponseEntity.ok(
			StatusResponse("API is online and functional.", AppConstants.APP_VERSION, downstreamStatus)
		)
	}
}