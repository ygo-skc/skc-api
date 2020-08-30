package com.rtomyj.yugiohAPI.controller.banlist;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.constants.RegexExpressions;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.banlist.BanListInstance;
import com.rtomyj.yugiohAPI.service.banlist.CardsService;

import io.swagger.annotations.ApiParam;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Configures endpoint(s) that can be used to obtain information about cards for a particular ban list.
 */
@RestController
@RequestMapping(path="/ban/cards", produces = "application/hal+json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(tags = {SwaggerConstants.BAN_LIST_TAG_NAME})
public class BannedCardsController extends YgoApiBaseController {

	/**
	 * The base endpoint for this controller.
	 */
	private static final String endPoint = YgoApiBaseController.BASE_ENDPOINT + "/ban/cards";

	/**
	 * Service object used to get information about banned cards from the database.
	 */
	private final CardsService bannedCardsService;


	/**
	 * Create object instance.
	 * @param request Object containing info about the client and their request.
	 * @param bannedCardsService Service object to use to accomplish functionality needed by this endpoint.
	 */
	@Autowired
	public BannedCardsController(final HttpServletRequest request, final CardsService bannedCardsService)
	{

		this.request = request;
		this.bannedCardsService = bannedCardsService;

	}


	/**
	 * User can get the contents of a ban list by the start date of the ban list.
	 * User data will be validated. A regular expression is used to ensure that the
	 * user passed a valid date. The ban list cache will be utilized to speed up
	 * delivery. If desired ban list contents are not in query , the ban list
	 * contents will be fetched from DB.
	 *
	 * @param banListStartDate The date the desired ban list took effect.
	 * @return ban list for specified ban list start date.
	 */
	@ResponseBody
	@GetMapping(path = "{banListStartDate}")
	@ApiOperation(value = "Retrieves information about a ban list using a valid effective ban list start date (use /api/v1/ban/dates to see a valid list of start dates)."
		, response = BanListInstance.class
		, responseContainer = "Object"
		, tags = SwaggerConstants.BAN_LIST_TAG_NAME)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = SwaggerConstants.http200)
		, @ApiResponse(code = 400, message = SwaggerConstants.http400)
		, @ApiResponse(code = 404, message = SwaggerConstants.http404)
	})
	public ResponseEntity<BanListInstance> getBannedCards(
			@ApiParam(
					value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION
					, example = "2020-04-01"
					, required = true
			) @Pattern(regexp = RegexExpressions.DB_DATE_PATTERN, message = "Date doesn't have correct format.") @PathVariable final String banListStartDate
			, @ApiParam(
					value = SwaggerConstants.SAVE_BANDWIDTH_DESCRIPTION
			) @RequestParam(name = "saveBandwidth", required = false, defaultValue = "true") final boolean saveBandwidth
			, @ApiParam(
					value = SwaggerConstants.BAN_LIST_FETCH_ALL_DESCRIPTION
			) @RequestParam(name = "allInfo", required = false, defaultValue = "false") final boolean fetchAllInfo)
			throws YgoException
	{

		MDC.put("reqIp", request.getRemoteHost());
		MDC.put("reqRes", endPoint);

		final BanListInstance reqBanListInstance = bannedCardsService.getBanListByBanStatus(banListStartDate, saveBandwidth, fetchAllInfo);
		log.info("Successfully retrieved ban list: ( {} ) with saveBandwidth: ( {} ).", banListStartDate, saveBandwidth);

		MDC.clear();
		return ResponseEntity.ok(reqBanListInstance);

	}
}
