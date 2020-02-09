package com.rtomyj.yugiohAPI.controller.banlist;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.model.BanListInstance;
import com.rtomyj.yugiohAPI.service.banlist.CardsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
@Slf4j
@RestController
@RequestMapping(path = "${ygo.endpoints.v1.banned-cards}", produces = "application/json; charset=utf-8")
@Validated
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
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 */
	@Autowired
	@Qualifier("banListCardsCache")
	private Map<String, BanListInstance>  BAN_LIST_CARDS_CACHE;

	/**
	 * In memory cache for contents of previously queried ban lists. Each start date of a ban list has its own ban list. Each ban list has 3 type of banned cards.
	 * Each type has cards with that status.
	 * The difference between this and the above cache is that this cache has trimmed text to prevent using too much bandwidth
	 */
	@Autowired
	@Qualifier("banListCardsCacheLowBandwidth")
	private Map<String, BanListInstance>  BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;



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
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure banListStartDate is valid")
	})
	public ResponseEntity<BanListInstance> getBannedCards(@Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "Date doesn't have correct format.") @PathVariable String banListStartDate
		, @RequestParam(name = "saveBandwidth", required = false) boolean saveBandwidth)
		throws YgoException
	{
		/* Determines which cache to use depending on user bandwidth preferences */
		Map<String, BanListInstance> cache;
		if (saveBandwidth)	cache = BAN_LIST_CARDS_LOW_BANDWIDTH_CACHE;
		else	cache = BAN_LIST_CARDS_CACHE;


		/*
			If the requested ban list is cached, access the cache and return the contents of the ban list.
		*/
		if (cache.get(banListStartDate) != null)
		{
			HttpStatus status = HttpStatus.OK;
			log.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, status, true, true));

			return new ResponseEntity<>(cache.get(banListStartDate), status);
		}
		/*
			If not in cache, try to retrieve ban list contents from DB
		*/
		else
		{
			BanListInstance banListInstance = new BanListInstance();

			/*
				Retrieves ban lists from DB by status
			*/
			banListInstance.setForbidden(bannedCardsService.getBanListByBanStatus(banListStartDate, Status.FORBIDDEN, saveBandwidth));
			banListInstance.setLimited(bannedCardsService.getBanListByBanStatus(banListStartDate, Status.LIMITED, saveBandwidth));
			banListInstance.setSemiLimited(bannedCardsService.getBanListByBanStatus(banListStartDate, Status.SEMI_LIMITED, saveBandwidth));
			banListInstance.setStartDate(banListStartDate);

			/*
				If DB doesn't return at least one card for at least one status, the users ban list isn't in the DB
			*/
			if (banListInstance.getForbidden().size() == 0 && banListInstance.getLimited().size() == 0
					&& banListInstance.getSemiLimited().size() == 0)
			{
				HttpStatus status = HttpStatus.NO_CONTENT;
				log.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, status));
				return new ResponseEntity<>(status);
			}
			/*
				If ban list is in DB, put ban list in cache and return the contents of ban list o the user.
			*/
			else
			{
				HttpStatus status = HttpStatus.OK;
				log.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, status, false, true));

				cache.put(banListStartDate, banListInstance);
				return new ResponseEntity<>(banListInstance, status);
			}
		}
	}
}
