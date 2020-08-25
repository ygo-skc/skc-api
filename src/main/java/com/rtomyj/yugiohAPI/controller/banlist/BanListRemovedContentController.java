package com.rtomyj.yugiohAPI.controller.banlist;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.constants.RegexConstants;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerResponseConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.banlist.BanListRemovedContent;
import com.rtomyj.yugiohAPI.service.banlist.DiffService;

import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path=YgoApiBaseController.BASE_ENDPOINT + "/ban/removed", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class BanListRemovedContentController extends YgoApiBaseController {

	/**
	 * The base path/endpoint being used by controller.
	 */
	private static final String endPoint = YgoApiBaseController.BASE_ENDPOINT + "/ban/removed";

	/**
	 * Service used to interface with dao.
	 */
	private DiffService banListDiffService;


	@Autowired
	public BanListRemovedContentController(final HttpServletRequest request, final DiffService banListDiffService)
	{

		this.request = request;
		this.banListDiffService = banListDiffService;

	}


	@GetMapping(path = "/{banListStartDate}")
	@ApiOperation(value = "Retrieve removed cards of a specific ban list given valid date a ban list started (use /api/v1/ban/dates to see a valid list)"
		, response = BanListRemovedContent.class
		, responseContainer = "Object"
		, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = SwaggerResponseConstants.http200)
		, @ApiResponse(code = 400, message = SwaggerResponseConstants.http400)
		, @ApiResponse(code = 404, message = SwaggerResponseConstants.http404)
	})
	public ResponseEntity<BanListRemovedContent> getNewlyRemovedContentForBanList(
		@Pattern(regexp = RegexConstants.DB_DATE_PATTERN, message = "Date doesn't have correct format.") @PathVariable(name = "banListStartDate") final String banListStartDate)
		throws YgoException
	{

		MDC.put("reqIp", request.getRemoteHost());
		MDC.put("reqRes", endPoint);

		final BanListRemovedContent banListRemovedContent = banListDiffService.getRemovedContentOfBanList(banListStartDate);
		log.info("Successfully retrieved removed content for banlist: ( {} ).", banListStartDate);

		MDC.clear();
		return ResponseEntity.ok(banListRemovedContent);

	}
}