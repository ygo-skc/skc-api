package com.rtomyj.yugiohAPI.controller.banlist;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.model.banlist.BanListDates;
import com.rtomyj.yugiohAPI.service.banlist.BanService;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Configures endpoint(s) for returning user the dates of the ban lists in the database.
 */
@RestController
@RequestMapping(path="/ban_list/dates", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Api(tags = {SwaggerConstants.BAN_LIST_TAG_NAME})
public class BanListDatesController extends YgoApiBaseController
{

	/**
	 * The base endpoint used by this controller.
	 */
	private static final String END_POINT = BASE_ENDPOINT + "/ban_list/dates";

	/**
	 * Service object used to interface the database DAO
	 */
	private final BanService banListService;


	/**
	 * Create object instance.
	 * @param request Object containing info about the client and their request.
	 * @param banListService Service object to use to accomplish functionality needed by this endpoint.
	 */
	@Autowired
	public BanListDatesController(final HttpServletRequest request, final BanService banListService)
	{

		this.request = request;
		this.banListService = banListService;

	}


	/**
	 * Looks in the database for the start dates of all ban lists stored in database.
	 * @return Map that contains a list of all dates of the ban lists in database.
	 */
	@GetMapping
	@ApiOperation(value = "Retrieve start (effective) dates of all ban lists stored in database in logical order. These dates are \"valid\" start dates that can be used by other endpoints ban list endpoints."
		, response = BanListDates.class
		, tags = SwaggerConstants.BAN_LIST_TAG_NAME)
	@ApiResponses( value = {
		@ApiResponse(code = 200, message = SwaggerConstants.http200)
	})
	public ResponseEntity<BanListDates> getBanListStartDates()
	{

		MDC.put("reqIp", request.getRemoteHost());
		MDC.put("reqRes", END_POINT);

		final BanListDates banListDates = banListService.getBanListStartDates();

		MDC.clear();
		return ResponseEntity.ok(banListDates);

	}

}