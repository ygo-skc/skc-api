package com.rtomyj.skc.controller.banlist;

import com.rtomyj.skc.controller.YgoApiBaseController;
import com.rtomyj.skc.constant.RegexExpressions;
import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.banlist.BanListNewContent;
import com.rtomyj.skc.model.banlist.BanListRemovedContent;
import com.rtomyj.skc.service.banlist.DiffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

/**
 * Class used as a REST controller for retrieving cards added to a particular ban list compared to previous ban list
 * or cards that switched statuses (Forbidden -&gt; limited, limited -&gt; semi-limited, etc) compared with the previous ban list.
 */
@RestController
@RequestMapping(path="/ban_list", produces = "application/json; charset=UTF-8")
@Slf4j
@Validated
@Api(tags = {SwaggerConstants.BAN_LIST_TAG_NAME})
public class BanListDiffController extends YgoApiBaseController
{
	/**
	 * Service used to interface with dao.
	 */
	private final DiffService banListDiffService;


	/**
	 * Create object instance.
	 * @param banListDiffService Service object to use to accomplish functionality needed by this endpoint.
	 */
	@Autowired
	public BanListDiffController(final DiffService banListDiffService)
	{

		this.banListDiffService = banListDiffService;

	}


	/**
	 *
	 * @param banListStartDate The date of a ban list user wants to see new card information about.
	 * @return Information about the new cards for the specified ban list date.
	 */
	@GetMapping(path = "/{banListStartDate}/new")
	@ApiOperation(
		value = "Retrieve cards that are either newly added to desired ban list or cards that have switched statuses (ie: from forbidden to limited) relative to desired ban list " +
				"using a valid start/effective date of a ban list (use /api/v1/ban/dates to see a valid list of start dates)."
		, response = BanListNewContent.class
		, responseContainer = "Object"
		, tags = SwaggerConstants.BAN_LIST_TAG_NAME)
	@ApiResponses( value = {
		@ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
		, @ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE)
		, @ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
		, @ApiResponse(code = 500, message = SwaggerConstants.HTTP_500_SWAGGER_MESSAGE)
	})
	public ResponseEntity<BanListNewContent> getNewlyAddedContentForBanList(
			@ApiParam(
					value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION
					, example = "2020-04-01"
					, required = true
			)
			@Pattern(regexp = RegexExpressions.DB_DATE_PATTERN, message = "Date doesn't have correct format.")
			@PathVariable final String banListStartDate
	)
			throws YgoException
	{
		log.info("User is requesting new content for ban list: {}", banListStartDate);
		final BanListNewContent banListNewContent = banListDiffService.getNewContentForGivenBanList(banListStartDate);

		log.info("Successfully retrieved new content for ban list ({}) using previous ban list ({}) for comparison. Newly... forbidden ({}), limited ({}), semi-limited ({})"
				, banListNewContent.getListRequested(), banListNewContent.getComparedTo(), banListNewContent.getNumNewForbidden(), banListNewContent.getNumNewLimited()
				, banListNewContent.getNumNewSemiLimited());
		return ResponseEntity.ok(banListNewContent);

	}


	@GetMapping(path = "/{banListStartDate}/removed")
	@ApiOperation(value = "Retrieve cards removed from the desired ban list compared to the previous logical ban list (use /api/v1/ban/dates to see a valid list of start dates)."
			, response = BanListRemovedContent.class
			, responseContainer = "Object"
			, tags = SwaggerConstants.BAN_LIST_TAG_NAME)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
			, @ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE)
			, @ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
			, @ApiResponse(code = 500, message = SwaggerConstants.HTTP_500_SWAGGER_MESSAGE)
	})
	public ResponseEntity<BanListRemovedContent> getNewlyRemovedContentForBanList(
			@ApiParam(
					value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION
					, example = "2020-04-01"
					, required = true
			)
			@Pattern(regexp = RegexExpressions.DB_DATE_PATTERN, message = "Date doesn't have correct format.") @PathVariable(name = "banListStartDate") final String banListStartDate)
			throws YgoException
	{
		final BanListRemovedContent banListRemovedContent = banListDiffService.getRemovedContentForGivenBanList(banListStartDate);
		log.info("Successfully retrieved removed content for banlist: ( {} ).", banListStartDate);

		return ResponseEntity.ok(banListRemovedContent);
	}
}