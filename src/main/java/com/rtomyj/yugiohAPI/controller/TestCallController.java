package com.rtomyj.yugiohAPI.controller;

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

@RequestMapping(path = "${ygo.endpoints.test-call-v1}")
@RestController
@CrossOrigin(origins = "*")
@Api(description = "Endpoint to check if API is online.", tags = "Testcall")
public class TestCallController
{
	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	@Value("${ygo.endpoints.test-call-v1}")
	private String endPoint;



	/**
	 * @return item
	 */
	@GetMapping()
	@ApiOperation(value = "Testcall", response = ResponseEntity.class, tags = "Testcall")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK")
	})
	public ResponseEntity<String> testCall()
	{
		LOG.info(String.format("%s hit", endPoint));
		return new ResponseEntity<>("API is online.", HttpStatus.OK);
	}
}