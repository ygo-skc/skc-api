package com.rtomyj.yugiohAPI.controller.banlist;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.helper.ResourceValidator;
import com.rtomyj.yugiohAPI.model.BanListNewContent;
import com.rtomyj.yugiohAPI.model.NewCards;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Class used as a REST controller for retrieving cards added to a particular ban list compared to previous ban list
 * or cards that switched statuses (Forbidden -&gt; limited, limited -&gt; semi-limited, etc) compared with the previous ban list.
 */
@RestController
@RequestMapping(path = "${ygo.endpoints.v1.ban-list-new-cards}", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
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
	private Map<String, BanListNewContent> cache;

	/**
	 * Logging object.
	 */
	private static final Logger LOG = LogManager.getLogger();



	/**
	 *
	 * @param banListStartDate The date of a ban list user wants to see new card information about.
	 * @return Information about the new cards for the specified ban list date.
	 */
	@GetMapping(path = "/{banListStartDate}")
	@ApiOperation(value = "Retrieve cards that are either newly added to a ban list or cards that have switched statuses (ie: from forbidden to limited) given valid date a ban list started (use /api/v1/ban/dates to see a valid list)."
		, response = BanListNewContent.class
		, responseContainer = "Object"
		, tags = "Ban List")
	@ApiResponses( value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure startDate is valid")
	})
	public ResponseEntity<BanListNewContent> getNewContent(@PathVariable final String banListStartDate)
	{
		// The values of the below variables will be changed in the if statements accordingly
		HttpStatus requestStatus = null;	// the status code for request
		// the metadata object for new cards - to contain; ban list requested, ban list compared to (previous list) and a list of new cards
		BanListNewContent newCardsMeta = cache.get(banListStartDate);
		boolean isInCache = false, isContentReturned = false;	// for logging helper method


		// Invalid ban list date requested - ie not in xxxx-xx-xx format
		if ( !ResourceValidator.isValidBanListDate(banListStartDate) )	requestStatus = HttpStatus.BAD_REQUEST;
		// Resource isn't in cache and ban list date passed validation
		else if ( newCardsMeta == null && ResourceValidator.isValidBanListDate(banListStartDate) )
		{
			// retrieving new cards by ban list status
			NewCards newCards = new NewCards();
			newCards.setForbidden(banListDiffService.getNewContentOfBanList(banListStartDate, Status.FORBIDDEN.toString()));
			newCards.setLimited(banListDiffService.getNewContentOfBanList(banListStartDate, Status.LIMITED.toString()));
			newCards.setSemiLimited(banListDiffService.getNewContentOfBanList(banListStartDate, Status.SEMI_LIMITED.toString()));

			// There are changes for requested date - ie, requested date found in DB
			if ( newCards.getForbidden().size() != 0 || newCards.getLimited().size() != 0 || newCards.getSemiLimited().size() != 0 )
			{
				// builds meta data object for new cards request
				newCardsMeta = new BanListNewContent();
				newCardsMeta.setListRequested(banListStartDate);
				newCardsMeta.setComparedTo(banListDiffService.getPreviousBanListDate(banListStartDate));
				newCardsMeta.setNewCards(newCards);


				cache.put(banListStartDate, newCardsMeta);

				requestStatus = HttpStatus.OK;
				isContentReturned = true;
			}
			// There are no changes for requested date - ie, requested date not found in DB.
			else	requestStatus = HttpStatus.NO_CONTENT;
		}
		// Resource in cache and ban list date passed validation
		else if (  newCardsMeta != null && ResourceValidator.isValidBanListDate(banListStartDate) )
		{
			requestStatus = HttpStatus.OK;
			isInCache = true;
			isContentReturned = true;
		}

		LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, requestStatus, isInCache, isContentReturned));
		return new ResponseEntity<>(newCardsMeta, requestStatus);
	}
}