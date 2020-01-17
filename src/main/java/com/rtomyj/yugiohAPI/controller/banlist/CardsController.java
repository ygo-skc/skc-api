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
@RestController
@RequestMapping(path = "${ygo.endpoints.v1.banned-cards}", produces = "application/json; charset=utf-8")
@CrossOrigin(origins = "*")
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
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

	/**
	 * Logging object.
	 */
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 */
	@Autowired
	@Qualifier("banListCardsCache")
	private Map<String, Map<String, Map<String, List<Card>>>> BAN_LIST_CARDS_CACHE;

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 * The difference between this and the above cache is that this cache has trimmed text to prevent using too much bandwidth
	 */
	@Autowired
	@Qualifier("banListCardsCacheLowBandwidth")
	private Map<String, Map<String, Map<String, List<Card>>>> BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;



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
	@ApiOperation(value = "Retrieve the full ban list of a given valid date a ban list started (use /api/v1/ban/dates to see a valid list)", response = ResponseEntity.class, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure banListStartDate is valid")
	})
	public ResponseEntity<Map<String, Map<String, List<Card>>>> getBannedCards(@PathVariable String banListStartDate, @RequestParam(name = "saveBandwidth", required = false) boolean saveBandwidth)
	{
		/*
			If regex doesn't find users query date valid, return nothing to the user.
		*/
		if ( !ResourceValidator.isValidBanListDate(banListStartDate) )
		{
			HttpStatus status = HttpStatus.BAD_REQUEST;
			LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, status));
			return new ResponseEntity<>(status);
		}

		Map<String, Map<String, Map<String, List<Card>>>> cache;
		if (saveBandwidth)	cache = BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;
		else	cache = BAN_LIST_CARDS_CACHE;


		/*
			If the requested ban list is cached, access the cache and return the contents of the ban list.
		*/
		if (cache.get(banListStartDate) != null)
		{
			HttpStatus status = HttpStatus.OK;
			LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, status, true, true));

			return new ResponseEntity<>(cache.get(banListStartDate), status);
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
			banListSections.put("forbidden", bannedCardsService.getBanListByBanStatus(banListStartDate, Status.FORBIDDEN, saveBandwidth));
			banListSections.put("limited", bannedCardsService.getBanListByBanStatus(banListStartDate, Status.LIMITED, saveBandwidth));
			banListSections.put("semiLimited", bannedCardsService.getBanListByBanStatus(banListStartDate, Status.SEMI_LIMITED, saveBandwidth));

			/*
				If DB doesn't return at least one card for at least one status, the users ban list isn't in the DB
			*/
			if (banListSections.get("forbidden").size() == 0 && banListSections.get("limited").size() == 0
					&& banListSections.get("semiLimited").size() == 0)
			{
				HttpStatus status = HttpStatus.NO_CONTENT;
				LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, status));
				return new ResponseEntity<>(status);
			}
			/*
				If ban list is in DB, put ban list in cache and return the contents of ban list o the user.
			*/
			else
			{
				HttpStatus status = HttpStatus.OK;
				LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, status, false, true));
				banList.put("bannedCards", banListSections);

				cache.put(banListStartDate, banList);
				return new ResponseEntity<>(banList, status);
			}
		}
	}
}
