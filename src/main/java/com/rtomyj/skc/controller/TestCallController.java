package com.rtomyj.skc.controller;

import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.model.TestCallResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Configures endpoint(s) for testing the health of the API.
 */
@RestController
@RequestMapping(path="/testcall", produces = "application/json; charset=UTF-8")
@Slf4j
@Api(tags = {SwaggerConstants.TEST_CALL_TAG_NAME})
public class TestCallController extends YgoApiBaseController {
	/**
	 * Used to see if endpoint is up.
	 * @return String confirming API is up.2
	 */
	@GetMapping()
	@ApiOperation(value = "Simple test operation.", response = ResponseEntity.class, tags = SwaggerConstants.TEST_CALL_TAG_NAME)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
	})
	public ResponseEntity<TestCallResponse> testCall()
	{
		log.info("User requested API status");
		return ResponseEntity.ok(new TestCallResponse("API is online and functional."));
	}
}