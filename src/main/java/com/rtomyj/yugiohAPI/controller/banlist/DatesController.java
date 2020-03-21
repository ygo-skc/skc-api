package com.rtomyj.yugiohAPI.controller.banlist;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.helper.ServiceLayerHelper;
import com.rtomyj.yugiohAPI.model.BanListStartDates;
import com.rtomyj.yugiohAPI.service.banlist.BanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * Configures endpoint(s) for returning user the dates of the ban lists in the database.
 */
@RestController
@RequestMapping(path="${ygo.endpoints.v1.ban-list-dates}", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Api(description = "Request information about current and past ban lists"
	, tags = "Ban List")
public class DatesController
{
	/**
	 * Service object used to interface the database DAO
	 */
	@Autowired
	private BanService banListService;

	/**
	 * The base endpoint used by this controller.
	 */
	@Value("${ygo.endpoints.v1.ban-list-dates}")
	private String endPoint;

	/**
	 * Object containing info about the request.
	 */
	@Autowired
	private HttpServletRequest request;


	/**
	 * Looks in the database or cache for the start dates of all ban lists stored in database.
	 * @return Map that contains a list of all dates of the ban lists in database.
	 */
	@GetMapping()
	@ApiOperation(value = "Retrieve dates of all ban lists stored in database. These dates are valid start dates that can be used by other endpoints."
		, response = BanListStartDates.class
		, tags = "Ban List")
	@ApiResponses( value = {
		@ApiResponse(code = 200, message = "OK")
	})
	public ResponseEntity<BanListStartDates> startDatesOfBanLists()
	{
		final ServiceLayerHelper serviceLayerHelper = banListService.getBanListStartDates();

		log.info(LogHelper.requestStatusLogString(request.getRemoteHost(), "Retrieve all ban lists from DB.", endPoint, serviceLayerHelper.getStatus()));
		return new ResponseEntity<>( (BanListStartDates) serviceLayerHelper.getRequestedResource(), serviceLayerHelper.getStatus());
	}
}