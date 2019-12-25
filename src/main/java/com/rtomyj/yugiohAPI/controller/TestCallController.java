package com.rtomyj.yugiohAPI.controller;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.helper.LogHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * Configures endpoint(s) for testing the health of the API.
 */
@RequestMapping(path = "${ygo.endpoints.v1.test-call}")
@RestController
@CrossOrigin(origins = "*")
@Api(description = "Endpoint to check if API is online.", tags = "Testcall")
public class TestCallController
{
	/**
	 * Object containing info about the user who initiates a request
	 */
	@Autowired
	private HttpServletRequest httpRequest;

	/**
	 * Logging object.
	 */
	private static final Logger LOG = LogManager.getLogger();

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
		LOG.info(LogHelper.requestStatusLogString(httpRequest.getRemoteHost(), "status", endPoint, HttpStatus.OK));
		return new ResponseEntity<>("API is online.", HttpStatus.OK);
	}
}