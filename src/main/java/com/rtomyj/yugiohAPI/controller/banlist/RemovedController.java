package com.rtomyj.yugiohAPI.controller.banlist;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.helper.constants.RegexConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.BanListRemovedContent;
import com.rtomyj.yugiohAPI.service.banlist.DiffService;

import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@RestController
@RequestMapping(path="/api/v1/ban/removed", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class RemovedController {

	@Autowired
	DiffService banListDiffService;

	private static final String endPoint = "/api/v1/ban/removed";

	@Autowired
	HttpServletRequest request;



	@GetMapping(path = "/{banListStartDate}")
	@ApiOperation(value = "Retrieve removed cards of a specific ban list given valid date a ban list started (use /api/v1/ban/dates to see a valid list)"
		, response = BanListRemovedContent.class
		, responseContainer = "Object"
		, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK")
		, @ApiResponse(code = 400, message = "Malformed request, make sure banListStartDate is valid")
		, @ApiResponse(code = 404, message = "No resource for requested ban list start date")
	})
	public ResponseEntity<BanListRemovedContent> getRemovedContent(
		@Pattern(regexp = RegexConstants.DB_DATE_PATTERN, message = "Date doesn't have correct format.") @PathVariable(name = "banListStartDate") final String banListStartDate)
		throws YgoException
	{
		MDC.put("reqIp", request.getRemoteHost());
		MDC.put("reqRes", endPoint);

		final BanListRemovedContent banListRemovedContent = banListDiffService.getRemovedContentOfBanList(banListStartDate);
		log.info("Successfully retrieved removed content for banlist: ( {} ).", banListStartDate);

		MDC.clear();
		return ResponseEntity.ok(banListRemovedContent);
	}
}