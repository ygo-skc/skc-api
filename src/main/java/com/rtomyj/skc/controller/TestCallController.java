package com.rtomyj.skc.controller;

import com.rtomyj.skc.helper.constants.SwaggerConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Configures endpoint(s) for testing the health of the API.
 */
@RestController
@RequestMapping(path="/testcall", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = {SwaggerConstants.TEST_CALL_TAG_NAME})
public class TestCallController extends YgoApiBaseController
{
	// static inner classes
	@Getter
	@AllArgsConstructor
	@ApiModel(description = "Return object for test call endpoint.")
	private static class ApiTestCall
	{

		@ApiModelProperty(
				value = "The current status of the API."
				, accessMode = ApiModelProperty.AccessMode.READ_ONLY
		)
		private final String status;

	}


	// fields
	/**
	 * Object containing info about the user who initiates a request
	 */
	private final HttpServletRequest httpServletRequest;

	/**
	 * Base endpoint for this class.
	 */
	private final String END_POINT = BASE_ENDPOINT + "/testcall";


	@Autowired
	public TestCallController(final HttpServletRequest httpServletRequest)
	{

		this.httpServletRequest = httpServletRequest;

	}


	/**
	 * Used to see if endpoint is up.
	 * @return String confirming API is up.
	 */
	@GetMapping()
	@ApiOperation(value = "Simple test operation.", response = ResponseEntity.class, tags = SwaggerConstants.TEST_CALL_TAG_NAME)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.http200)
	})
	public ResponseEntity<ApiTestCall> testCall()
	{

		log.info("User requested API status");
		return ResponseEntity.ok(new ApiTestCall("API is online and functional."));

	}

}