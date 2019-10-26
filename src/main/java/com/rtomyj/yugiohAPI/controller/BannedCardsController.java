package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.service.BannedCardsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


@RequestMapping(path = "${ygo.endpoints.banned-cards-v1}", produces = "application/json; charset=utf-8")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Ban List")
public class BannedCardsController {
	@Autowired
	private BannedCardsService bannedCardsService;

	@Autowired
	@Value("${ygo.endpoints.banned-cards-v1}")
	private String endPoint;

	@Autowired
	private HttpServletRequest request;

	private static final Logger LOG = LogManager.getLogger();


	@ResponseBody
	@GetMapping(path = "{startDate}")
	@ApiOperation(value = "Retrieve the full ban list of a specified {startDate}", response = ResponseEntity.class, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure startDate is valid")
	})
	public ResponseEntity<Map<String, Map<String, List<Map<String, String>>>>> getBannedCards(@PathVariable String startDate)
	{
		Pattern datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		if (! datePattern.matcher(startDate).matches())
		{
			HttpStatus status = HttpStatus.BAD_REQUEST;
			LOG.info(String.format("%s/{ %s } hit - responding with: { %s }", endPoint, startDate, status));
			return new ResponseEntity<>(status);
		}


		Map<String, Map<String, List<Map<String, String>>>> banList = new HashMap<>();
		Map<String, List<Map<String, String>>> banListSections = new LinkedHashMap<>();

		banListSections.put("forbidden", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Forbidden")));
		banListSections.put("limited", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Limited")));
		banListSections.put("semiLimited", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Semi-Limited")));


		if (banListSections.get("forbidden").size() == 0 && banListSections.get("limited").size() == 0 && banListSections.get("semiLimited").size() == 0)
		{
			HttpStatus status = HttpStatus.NO_CONTENT;
			LOG.info(String.format("%s/{ %s } hit - responding with: { %s }", endPoint, startDate, status));
			return new ResponseEntity<>(status);
		}
		else
		{
			HttpStatus status = HttpStatus.OK;
			LOG.info(String.format("%s %s/{ %s } hit - responding with: { %s }", request.getRemoteHost(), endPoint, startDate, status));
			banList.put("bannedCards", banListSections);
			return new ResponseEntity<>(banList, status);
		}
	}
}
