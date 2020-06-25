package com.rtomyj.yugiohAPI.controller.banlist;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.banlist.BanListInstance;
import com.rtomyj.yugiohAPI.service.banlist.CardsService;

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
@RequestMapping(path="/api/v1/ban/cards", produces = "application/hal+json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class CardsController {
	/**
	 * Service object used to get information about banned cards from the database.
	 */
	private final CardsService bannedCardsService;

	/**
	 * Updated on every request with info about request.
	 */
	private final HttpServletRequest request;

	/**
	 * The base endpoint for this controller.
	 */
	private static final String endPoint = "/api/v1/ban/cards";



	@Autowired
	public CardsController(final CardsService bannedCardsService, final HttpServletRequest request)
	{
		this.bannedCardsService = bannedCardsService;
		this.request = request;
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
	@ApiOperation(value = "Retrieve the full ban list of a given valid date a ban list started (use /api/v1/ban/dates to see a valid list)"
		, response = BanListInstance.class
		, responseContainer = "Object"
		, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK")
		, @ApiResponse(code = 400, message = "Malformed request, make sure banListStartDate is valid")
		, @ApiResponse(code = 404, message = "No resource for requested ban list start date")
	})
	public ResponseEntity<BanListInstance> getBannedCards(@Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "Date doesn't have correct format.") @PathVariable String banListStartDate
			, @RequestParam(name = "saveBandwidth", required = false, defaultValue = "true") boolean saveBandwidth
			, @RequestParam(name = "allInfo", required = false, defaultValue = "false") boolean fetchAllInfo)
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
