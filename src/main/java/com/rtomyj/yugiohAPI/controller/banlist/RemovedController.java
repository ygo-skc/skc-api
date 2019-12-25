package com.rtomyj.yugiohAPI.controller.banlist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 *
 */
@RestController
@RequestMapping(path = "${ygo.endpoints.v1.ban-list-removed-cards}")
@CrossOrigin(origins = "*")
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class RemovedController {

	@Autowired
	DiffService banListDiffService;

	@Value("${ygo.endpoints.v1.ban-list-removed-cards}")
	private String endPoint;

	@Autowired
	HttpServletRequest request;

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	@Qualifier("banListRemovedCardsCache")
	private Map<String, Map<String, Object>> cache;



	@GetMapping(path = "/{banListStartDate}")
	@ApiOperation(value = "Retrieve removed cards of a specific ban list given valid date a ban list started (use /api/v1/ban/dates to see a valid list)", response = ResponseEntity.class, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure banListStartDate is valid")
	})
	public ResponseEntity<Map<String, Object>> getRemovedContent(@PathVariable(name = "banListStartDate") String banListStartDate)
	{
		// The values of the below variables will be changed in the if statements accordingly
		HttpStatus requestStatus = null;	// the status code for request
		// the metadata object for removed cards - to contain; ban list requested, ban list compared to (previous list) and a list of removed cards
		Map<String, Object> removedCardsMeta = cache.get(banListStartDate);
		boolean isInCache = false, isContentReturned = false;	// for logging helper method


		// Invalid ban list date requested - ie not in xxxx-xx-xx format
		if ( !ResourceValidator.isValidBanListDate(banListStartDate) )	requestStatus = HttpStatus.BAD_REQUEST;
		// Resource isn't in cache and ban list date passed validation
		else if ( removedCardsMeta == null && ResourceValidator.isValidBanListDate(banListStartDate) )
		{
			// retrieving removed cards by ban list status

			final Map<String, List<Map<String, String>>> removedCards = new HashMap<>();
			removedCards.put("removedCards", banListDiffService.getRemovedContentOfBanList(banListStartDate));

			// There are changes for requested date - ie, requested date found in DB
			if ( removedCards.get("removedCards").size() != 0 )
			{
				// builds meta data object for removed cards request
				removedCardsMeta = new HashMap<>();
				removedCardsMeta.put("listRequested", banListStartDate);
				removedCardsMeta.put("comparedTo", banListDiffService.getPreviousBanListDate(banListStartDate));
				removedCardsMeta.put("removedCards", removedCards);


				cache.put(banListStartDate, removedCardsMeta);

				requestStatus = HttpStatus.OK;
				isContentReturned = true;
			}
			// There are no changes for requested date - ie, requested date not found in DB.
			else	requestStatus = HttpStatus.NO_CONTENT;
		}
		// Resource in cache and ban list date passed validation
		else if (  removedCardsMeta != null && ResourceValidator.isValidBanListDate(banListStartDate) )
		{
			requestStatus = HttpStatus.OK;
			isInCache = true;
			isContentReturned = true;
		}

		LOG.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, requestStatus, isInCache, isContentReturned));
		return new ResponseEntity<>(removedCardsMeta, requestStatus);
	}
}