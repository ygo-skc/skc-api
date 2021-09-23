package com.rtomyj.skc.controller.banlist;

import com.rtomyj.skc.controller.YgoApiBaseController;
import com.rtomyj.skc.constant.RegexExpressions;
import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.exception.YgoException;
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
 *
 */
@RestController
@RequestMapping(path="/ban_list", produces = "application/json; charset=UTF-8")
@Slf4j
@Validated
@Api(tags = {SwaggerConstants.BAN_LIST_TAG_NAME})
public class BanListRemovedContentController extends YgoApiBaseController
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
	public BanListRemovedContentController(final DiffService banListDiffService)
	{

		this.banListDiffService = banListDiffService;

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