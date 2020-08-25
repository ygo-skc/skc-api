package com.rtomyj.yugiohAPI.controller.banlist;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.constants.RegexConstants;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerResponseConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.banlist.BanListNewContent;
import com.rtomyj.yugiohAPI.service.banlist.BanService;
import com.rtomyj.yugiohAPI.service.banlist.DiffService;

import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * Class used as a REST controller for retrieving cards added to a particular ban list compared to previous ban list
 * or cards that switched statuses (Forbidden -&gt; limited, limited -&gt; semi-limited, etc) compared with the previous ban list.
 */
@RestController
@RequestMapping(path=YgoApiBaseController.BASE_ENDPOINT + "/ban/new", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Validated
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class BanListNewContentController extends YgoApiBaseController
{

	/**
	 * The base path/endpoint being used by controller.
	 */
	private static final String endPoint = YgoApiBaseController.BASE_ENDPOINT + "/ban/new";

	/**
	 * Service used to interface with dao.
	 */
	private DiffService banListDiffService;


	@Autowired
	public BanListNewContentController(final HttpServletRequest request, final DiffService banListDiffService)
	{

		this.request = request;
		this.banListDiffService = banListDiffService;

	}



	/**
	 *
	 * @param banListStartDate The date of a ban list user wants to see new card information about.
	 * @return Information about the new cards for the specified ban list date.
	 */
	@GetMapping(path = "/{banListStartDate}")
	@ApiOperation(
		value = "Retrieve cards that are either newly added to a ban list or cards that have switched statuses (ie: from forbidden to limited) using a valid start/effective date of a ban list (use /api/v1/ban/dates to see a valid list)."
		, response = BanListNewContent.class
		, responseContainer = "Object"
		, tags = "Ban List")
	@ApiResponses( value = {
		@ApiResponse(code = 200, message = SwaggerResponseConstants.http200)
		, @ApiResponse(code = 400, message = SwaggerResponseConstants.http400)
		, @ApiResponse(code = 404, message = SwaggerResponseConstants.http404)
	})
	public ResponseEntity<BanListNewContent> getNewlyAddedContentForBanList(
		@Pattern(regexp = RegexConstants.DB_DATE_PATTERN, message = "Date doesn't have correct format.") @PathVariable final String banListStartDate)
		throws YgoException
	{

		MDC.put("reqIp", request.getRemoteHost());
		MDC.put("reqRes", endPoint);

		final BanListNewContent serviceLayerHelper = banListDiffService.getNewContentOfBanList(banListStartDate);
		log.info("Successfully retrieved new content for ban list: ( {} ).", banListStartDate);

		MDC.clear();
		return ResponseEntity.ok(serviceLayerHelper);

	}
}