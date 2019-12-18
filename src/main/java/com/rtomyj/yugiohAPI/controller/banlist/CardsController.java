package com.rtomyj.yugiohAPI.controller.banlist;

import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.helper.ResourceValidator;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.service.banlist.CardsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Configures endpoint(s) that can be used to obtain information about cards for a particular ban list.
 */
@RequestMapping(path = "${ygo.endpoints.v1.banned-cards}", produces = "application/json; charset=utf-8")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Ban List")
public class CardsController {
	/**
	 * Service object used to get information about banned cards from the database.
	 */
	@Autowired
	private CardsService bannedCardsService;

	/**
	 * The base endpoint for this controller.
	 */
	@Value("${ygo.endpoints.v1.banned-cards}")
	private String endPoint;

	/**
	 * Updated on every request with info about request.
	 */
	@Autowired
	private HttpServletRequest request;

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 */
	@Autowired
	@Qualifier("banListCardsCache")
	private Map<String, Map<String, Map<String, List<Card>>>> BAN_LIST_CARDS_CACHE;



	/**
	 * User can get the contents of a ban list by the start date of the ban list.
	 * User data will be validated. A regular expression is used to ensure that the
	 * user passed a valid date. The ban list cache will be utilized to speed up
	 * delivery. If desired ban list contents are not in query , the ban list
	 * contents will be fetched from DB.
	 *
	 * @param startDate The date the desired ban list took effect.
	 * @param origin Where the request is coming from. Will be used to see which requests come from
	 * @return ban list for specified ban list start date.
	 */
	@ResponseBody
	@GetMapping(path = "{startDate}")
	@ApiOperation(value = "Retrieve the full ban list of a specified {startDate}", response = ResponseEntity.class, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure startDate is valid")
	})
	public ResponseEntity<Map<String, Map<String, List<Card>>>> getBannedCards(@PathVariable String startDate, @RequestHeader(value = "Origin", required = false) String origin )
	{
		/*
			If regex doesn't find users query date valid, return nothing to the user.
		*/
		if ( !ResourceValidator.isValidBanListDate(startDate) )
		{
			HttpStatus status = HttpStatus.BAD_REQUEST;
			LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), startDate, endPoint, status));
			return new ResponseEntity<>(status);
		}


		/*
			If the requested ban list is cached, access the cache and return the contents of the ban list.
		*/
		if (BAN_LIST_CARDS_CACHE.get(startDate) != null)
		{
			HttpStatus status = HttpStatus.OK;
			LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), startDate, endPoint, status, true, true));

			return new ResponseEntity<>(BAN_LIST_CARDS_CACHE.get(startDate), status);
		}
		/*
			If not in cache, try to retrieve ban list contents from DB
		*/
		else
		{
			Map<String, Map<String, List<Card>>> banList = new HashMap<>();
			Map<String, List<Card>> banListSections = new LinkedHashMap<>();

			/*
				Retrieves ban lists from DB by status
			*/
			banListSections.put("forbidden", bannedCardsService.getBanListByBanStatus(startDate, Status.FORBIDDEN));
			banListSections.put("limited", bannedCardsService.getBanListByBanStatus(startDate, Status.LIMITED));
			banListSections.put("semiLimited", bannedCardsService.getBanListByBanStatus(startDate, Status.SEMI_LIMITED));

			/*
				If DB doesn't return at least one card for at least one status, the users ban list isn't in the DB
			*/
			if (banListSections.get("forbidden").size() == 0 && banListSections.get("limited").size() == 0
					&& banListSections.get("semiLimited").size() == 0)
			{
				HttpStatus status = HttpStatus.NO_CONTENT;
				LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), startDate, endPoint, status));
				return new ResponseEntity<>(status);
			}
			/*
				If ban list is in DB, put ban list in cache and return the contents of ban list o the user.
			*/
			else
			{
				HttpStatus status = HttpStatus.OK;
				LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), startDate, endPoint, status, false, true));
				banList.put("bannedCards", banListSections);

				BAN_LIST_CARDS_CACHE.put(startDate, banList);
				return new ResponseEntity<>(banList, status);
			}
		}
	}
}
