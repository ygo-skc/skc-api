package com.rtomyj.yugiohAPI.controller.banlist;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.constants.RegexExpressions;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.banlist.BanListRemovedContent;
import com.rtomyj.yugiohAPI.service.banlist.DiffService;

import io.swagger.annotations.ApiParam;
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
@RequestMapping(path="/ban/removed", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(tags = {SwaggerConstants.BAN_LIST_TAG_NAME})
public class BanListRemovedContentController extends YgoApiBaseController {

	/**
	 * The base path/endpoint being used by controller.
	 */
	private static final String endPoint = YgoApiBaseController.BASE_ENDPOINT + "/ban/removed";

	/**
	 * Service used to interface with dao.
	 */
	private final DiffService banListDiffService;


	/**
	 * Create object instance.
	 * @param request Object containing info about the client and their request.
	 * @param banListDiffService Service object to use to accomplish functionality needed by this endpoint.
	 */
	@Autowired
	public BanListRemovedContentController(final HttpServletRequest request, final DiffService banListDiffService)
	{

		this.request = request;
		this.banListDiffService = banListDiffService;

	}


	@GetMapping(path = "/{banListStartDate}")
	@ApiOperation(value = "Retrieve cards removed from the ban list relative to the previous ban list (use /api/v1/ban/dates to see a valid list of start dates)"
		, response = BanListRemovedContent.class
		, responseContainer = "Object"
		, tags = SwaggerConstants.BAN_LIST_TAG_NAME)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = SwaggerConstants.http200)
		, @ApiResponse(code = 400, message = SwaggerConstants.http400)
		, @ApiResponse(code = 404, message = SwaggerConstants.http404)
	})
	public ResponseEntity<BanListRemovedContent> getNewlyRemovedContentForBanList(
			@ApiParam(
					value = "Valid start date of a ban list stored in database. Must conform to yyyy-mm-dd format (use /api/v1/ban/dates to see a valid list of start dates)."
					, example = "2020-04-01"
					, required = true
			)
			@Pattern(regexp = RegexExpressions.DB_DATE_PATTERN, message = "Date doesn't have correct format.") @PathVariable(name = "banListStartDate") final String banListStartDate)
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