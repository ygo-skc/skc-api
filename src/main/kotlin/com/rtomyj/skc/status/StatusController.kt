package com.rtomyj.skc.status

import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.status.dao.StatusDao
import com.rtomyj.skc.status.model.StatusResponse
import com.rtomyj.skc.util.YgoApiBaseController
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
	@Qualifier("jdbc") val dao: StatusDao
) : YgoApiBaseController() {

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}


	/**
	 * Retrieve status of the API.
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

		// get status of downstream services
		val downstreamStatus = listOf(dao.dbConnection())

		return ResponseEntity.ok(
			StatusResponse("API is online and functional.", AppConstants.APP_VERSION, downstreamStatus)
		)
	}
}