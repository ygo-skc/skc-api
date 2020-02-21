package com.rtomyj.yugiohAPI.controller.banlist;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.helper.constants.RegexConstants;
import com.rtomyj.yugiohAPI.model.BanListComparisonResults;
import com.rtomyj.yugiohAPI.model.BanListRemovedContent;
import com.rtomyj.yugiohAPI.service.banlist.DiffService;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@RestController
@RequestMapping(path="${ygo.endpoints.v1.ban-list-removed-cards}", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class RemovedController {

	@Autowired
	DiffService banListDiffService;

	@Value("${ygo.endpoints.v1.ban-list-removed-cards}")
	private String endPoint;

	@Autowired
	HttpServletRequest request;

	@Autowired
	@Qualifier("banListRemovedCardsCache")
	private Map<String, BanListRemovedContent> cache;



	@GetMapping(path = "/{banListStartDate}")
	@ApiOperation(value = "Retrieve removed cards of a specific ban list given valid date a ban list started (use /api/v1/ban/dates to see a valid list)"
		, response = BanListRemovedContent.class
		, responseContainer = "Object"
		, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK")
		, @ApiResponse(code = 204, message = "Request yielded no content")
		, @ApiResponse(code = 400, message = "Malformed request, make sure banListStartDate is valid")
	})
	public ResponseEntity<BanListRemovedContent> getRemovedContent(
		@Pattern(regexp = RegexConstants.DB_DATE_PATTERN, message = "Date doesn't have correct format.") @PathVariable(name = "banListStartDate") final String banListStartDate)
		throws YgoException
	{
		// The values of the below variables will be changed in the if statements accordingly
		HttpStatus requestStatus = null;	// the status code for request
		// the metadata object for removed cards - to contain; ban list requested, ban list compared to (previous list) and a list of removed cards
		BanListRemovedContent removedCardsMeta = cache.get(banListStartDate);
		boolean isInCache = false, isContentReturned = false;	// for logging helper method


		if ( removedCardsMeta == null )
		{
			// retrieving removed cards by ban list status

			List<BanListComparisonResults> removedCards = banListDiffService.getRemovedContentOfBanList(banListStartDate);

			// There are changes for requested date - ie, requested date found in DB
			if ( removedCards.size() != 0 )
			{
				// builds meta data object for removed cards request
				removedCardsMeta = BanListRemovedContent.builder()
					.listRequested(banListStartDate)
					.comparedTo(banListDiffService.getPreviousBanListDate(banListStartDate))
					.removedCards(removedCards)
					.build();


				cache.put(banListStartDate, removedCardsMeta);

				requestStatus = HttpStatus.OK;
				isContentReturned = true;
			}
			// There are no changes for requested date - ie, requested date not found in DB.
			else	requestStatus = HttpStatus.NO_CONTENT;
		}
		// Resource in cache and ban list date passed validation
		else if (  removedCardsMeta != null )
		{
			requestStatus = HttpStatus.OK;
			isInCache = true;
			isContentReturned = true;
		}


		log.info(LogHelper.requestStatusLogString(request.getRemoteHost(), banListStartDate, endPoint, requestStatus, isInCache, isContentReturned));
		return new ResponseEntity<>(removedCardsMeta, requestStatus);
	}
}