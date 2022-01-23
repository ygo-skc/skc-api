package com.rtomyj.skc.controller

import com.rtomyj.skc.constant.AppConstants
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.model.StatusResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
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
@Api(tags = [SwaggerConstants.TEST_CALL_TAG_NAME])
class StatusController @Autowired constructor(
	@Qualifier("jdbc") val dao: Dao
) : YgoApiBaseController() {

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}


	/**
	 * Retrieve status of the API.
	 * @return Status info.
	 */
	@GetMapping
	@ApiOperation(
		value = "Checking status of the API.",
		response = ResponseEntity::class,
		tags = [SwaggerConstants.TEST_CALL_TAG_NAME]
	)
	@ApiResponses(value = [ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)])
	fun status(): ResponseEntity<StatusResponse> {
		log.info("Status of API was requested")

		// get status of downstream services
		val downstreamStatus = listOf(dao.dbConnection())

		return ResponseEntity.ok(
			StatusResponse("API is online and functional.", AppConstants.APP_VERSION, downstreamStatus)
		)
	}
}