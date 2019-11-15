package com.rtomyj.yugiohAPI.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.service.BanListService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequestMapping(path = "${ygo.endpoints.ban-list-dates-v1}", produces = "application/json; charset=utf-8")
@RestController
@CrossOrigin(origins = "*")
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class BanListDatesController
{
	@Autowired
	private BanListService banListService;

	@Autowired
	@Value("${ygo.endpoints.ban-list-dates-v1}")
	private String endPoint;

	@Autowired
	private HttpServletRequest httpRequest;

	private static final Logger LOG = LogManager.getLogger();

	private static final Map<String, List<BanLists>> BAN_LISTS_START_DATES_CACHE = new HashMap<>();



	/**
	 * @return item
	 */
	@GetMapping()
	@ApiOperation(value = "Get dates of ban lists stored in database", response = ResponseEntity.class, tags = "Ban List")
	@ApiResponses( value = {
		@ApiResponse(code = 200, message = "OK")
	}
	)
	public ResponseEntity<Map<String, List<BanLists>>> startDatesOfBanLists()
	{
		if (BAN_LISTS_START_DATES_CACHE.size() == 0)
		{
			List<BanLists> banStartDates = (ArrayList<BanLists>) banListService.getBanListStartDates();
			BAN_LISTS_START_DATES_CACHE.put("banListStartDates", banStartDates);
		}


		HttpStatus status = HttpStatus.OK;
		LOG.info(LogHelper.requestInfo(httpRequest.getRemoteHost(), endPoint, String.format("Responding with { %s }", status)));
		return new ResponseEntity<>(BAN_LISTS_START_DATES_CACHE, status);
	}
}