package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.service.BannedCardsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;


@RequestMapping(path = "api/v1/banned_cards", produces = "application/json; charset=utf-8")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BannedCardsController {
	@Autowired
	BannedCardsService bannedCardsService;

	private static final Logger LOG = LogManager.getLogger();

	@ResponseBody
	@GetMapping(path = "{startDate}")
	public ResponseEntity<HashMap<String, LinkedHashMap<String, List<HashMap<String, String>>>>> getBannedCards(@PathVariable String startDate)
	{
		Pattern datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		if (! datePattern.matcher(startDate).matches())
		{
			HttpStatus status = HttpStatus.NOT_FOUND;
			LOG.info(String.format("List requested: { %s } - responding with: { %s }", startDate, status));
			return new ResponseEntity<>(status);
		}

		HashMap<String, LinkedHashMap<String, List<HashMap<String, String>>>> banList = new HashMap<>();
		LinkedHashMap<String, List<HashMap<String, String>>> banListSections = new LinkedHashMap<>();

		banListSections.put("forbidden", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Forbidden")));
		banListSections.put("limited", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Limited")));
		banListSections.put("semiLimited", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Semi-Limited")));

		if (banListSections.get("forbidden").size() == 0 && banListSections.get("limited").size() == 0 && banListSections.get("semiLimited").size() == 0)
		{
			HttpStatus status = HttpStatus.NO_CONTENT;
			LOG.info(String.format("List requested: { %s } - responding with: { %s }", startDate, status));
			return new ResponseEntity<>(status);
		}
		else
		{
			HttpStatus status = HttpStatus.OK;
			LOG.info(String.format("List requested: { %s } - responding with: { %s }", startDate, status));
			banList.put("bannedCards", banListSections);
			return new ResponseEntity<>(banList, status);
		}
	}
}
