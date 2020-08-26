package com.rtomyj.yugiohAPI.controller;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Configures endpoint(s) for testing the health of the API.
 */
@RestController
@RequestMapping(path=YgoApiBaseController.BASE_ENDPOINT + "/testcall", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = {SwaggerConstants.SWAGGER_TAG_TEST_CALL})
public class TestCallController extends YgoApiBaseController
{

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
	@ApiOperation(value = "Simple test operation.", response = ResponseEntity.class, tags = SwaggerConstants.SWAGGER_TAG_TEST_CALL)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.http200)
	})
	public ResponseEntity<String> testCall()
	{

		MDC.put("reqIp", httpServletRequest.getRemoteHost());
		MDC.put("reqRes", END_POINT);

		log.info("User requested API status");

		MDC.clear();
		return new ResponseEntity<>("API is online.", HttpStatus.OK);

	}

}