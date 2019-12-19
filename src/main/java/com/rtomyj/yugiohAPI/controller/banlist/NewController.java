package com.rtomyj.yugiohAPI.controller.banlist;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.helper.ResourceValidator;
import com.rtomyj.yugiohAPI.service.banlist.DiffService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class used as a REST controller for retrieving cards added to a particular ban list compared to previous ban list
 * or cards that switched statuses (Forbidden -&gt; limited, limited -&gt; semi-limited, etc) compared with the previous ban list.
 */
@RestController
@RequestMapping(path = "${ygo.endpoints.v1.ban-list-new-cards}", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
public class NewController
{
	/**
	 * Service used to interface with dao.
	 */
	@Autowired
	private DiffService banListDiffService;

	/**
	 * The base path/endpoint being used by controller.
	 */
	@Value("${ygo.endpoints.v1.ban-list-new-cards}")
	private String endPoint;

	/**
	 * Object holding information about the request.
	 */
	@Autowired
	private HttpServletRequest request;

	/**
	 * Cache for requests/data produced by requests.
	 */
	@Autowired
	@Qualifier("banListNewCardsCache")
	private Map<String, Map<String, Object>> cache;

	/**
	 * Logging object.
	 */
	private static final Logger LOG = LogManager.getLogger();



	/**
	 *
	 * @param banListDate The date of a ban list user wants to see new card information about.
	 * @return Information about the new cards for the specified ban list date.
	 */
	@GetMapping(path = "/{banListDate}")
	public ResponseEntity<Map<String, Object>> getNewContent(@PathVariable final String banListDate)
	{
		// The values of the below variables will be changed in the if statements accordingly
		HttpStatus requestStatus = null;	// the status code for request
		// the metadata object for new cards - to contain; ban list requested, ban list compared to (previous list) and a list of new cards
		Map<String, Object> newCardsMeta = cache.get(banListDate);
		boolean isInCache = false, isContentReturned = false;	// for logging helper method


		// Invalid ban list date requested - ie not in xxxx-xx-xx format
		if ( !ResourceValidator.isValidBanListDate(banListDate) )	requestStatus = HttpStatus.BAD_REQUEST;
		// Resource isn't in cache and ban list date passed validation
		else if ( newCardsMeta == null && ResourceValidator.isValidBanListDate(banListDate) )
		{
			// retrieving new cards by ban list status
			final Map<String, List<Map<String, String>>> newCards = new LinkedHashMap<>();
			newCards.put("forbidden", banListDiffService.getNewContentOfBanList(banListDate, Status.FORBIDDEN.toString()));
			newCards.put("limited", banListDiffService.getNewContentOfBanList(banListDate, Status.LIMITED.toString()));
			newCards.put("semiLimited", banListDiffService.getNewContentOfBanList(banListDate, Status.SEMI_LIMITED.toString()));

			// There are changes for requested date - ie, requested date found in DB
			if ( newCards.get("forbidden").size() != 0 || newCards.get("limited").size() != 0 || newCards.get("semiLimited").size() != 0 )
			{
				// builds meta data object for new cards request
				newCardsMeta = new HashMap<>();
				newCardsMeta.put("listRequested", banListDate);
				newCardsMeta.put("comparedTo", banListDiffService.getPreviousBanListDate(banListDate));
				newCardsMeta.put("newCards", newCards);


				cache.put(banListDate, newCardsMeta);

				requestStatus = HttpStatus.OK;
				isContentReturned = true;
			}
			// There are no changes for requested date - ie, requested date not found in DB.
			else	requestStatus = HttpStatus.NO_CONTENT;
		}
		// Resource in cache and ban list date passed validation
		else if (  newCardsMeta != null && ResourceValidator.isValidBanListDate(banListDate) )
		{
			requestStatus = HttpStatus.OK;
			isInCache = true;
			isContentReturned = true;
		}

		LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListDate, endPoint, requestStatus, isInCache, isContentReturned));
		return new ResponseEntity<>(newCardsMeta, requestStatus);
	}
}