package com.rtomyj.yugiohAPI.controller.banlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.service.banlist.BanService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
@RequestMapping(path = "${ygo.endpoints.v1.ban-list-dates}", produces = "application/json; charset=utf-8")
@RestController
@CrossOrigin(origins = "*")
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
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
	private HttpServletRequest httpRequest;

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Cache for storing previous queries
	 */
	@Autowired
	@Qualifier("banListStartDatesCache")
	private Map<String, List<BanLists>> BAN_LISTS_START_DATES_CACHE;


	/**
	 * Looks in the database or cache for the start dates of all ban lists stored in database.
	 * @return Map that contains a list of all dates of the ban lists in database.
	 */
	@GetMapping()
	@ApiOperation(value = "Get dates of ban lists stored in database", response = ResponseEntity.class, tags = "Ban List")
	@ApiResponses( value = {
		@ApiResponse(code = 200, message = "OK")
	}
	)
	public ResponseEntity<Map<String, List<BanLists>>> startDatesOfBanLists()
	{
		/**
		 * If cache is empty, querying the DB is required. DB results are then cached.
		 */
		if (BAN_LISTS_START_DATES_CACHE.size() == 0)
		{
			List<BanLists> banStartDates = (ArrayList<BanLists>) banListService.getBanListStartDates();
			BAN_LISTS_START_DATES_CACHE.put("banListStartDates", banStartDates);
		}


		/**
		 * Configures the ResponseEntity to return,
		 */
		HttpStatus status = HttpStatus.OK;
		LOG.info(LogHelper.requestStatusLogString(httpRequest.getRemoteHost(), "ban list dates", endPoint, status));
		return new ResponseEntity<>(BAN_LISTS_START_DATES_CACHE, status);
	}
}