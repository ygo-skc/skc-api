package com.rtomyj.yugiohAPI.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping(path="${ygo.endpoints.v1.test-call}", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Api(description = "Endpoint to check if API is online.", tags = "Testcall")
public class TestCallController
{
	/**
	 * Object containing info about the user who initiates a request
	 */
	@Autowired
	private HttpServletRequest httpRequest;

	/**
	 * Base endpoint for this class.
	 */
	@Value("${ygo.endpoints.v1.test-call}")
	private String endPoint;



	/**
	 * Used to see if endpoint is up.
	 * @return String confirming API is up.
	 */
	@GetMapping()
	@ApiOperation(value = "Testcall", response = ResponseEntity.class, tags = "Testcall")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK")
	})
	public ResponseEntity<String> testCall()
	{
		MDC.put("reqIp", httpRequest.getRemoteHost());
		MDC.put("reqRes", endPoint);

		log.info("User requested API status");

		MDC.clear();
		return new ResponseEntity<>("API is online.", HttpStatus.OK);
	}
}