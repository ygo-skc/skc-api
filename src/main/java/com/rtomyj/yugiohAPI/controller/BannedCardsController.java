package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.service.BannedCardsService;

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

	@ResponseBody
	@GetMapping(path = "{startDate}")
	public ResponseEntity<HashMap<String, LinkedHashMap<String, List<HashMap<String, String>>>>> getBannedCards(
			@PathVariable String startDate)
	{
		Pattern datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		if (! datePattern.matcher(startDate).matches())
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		HashMap<String, LinkedHashMap<String, List<HashMap<String, String>>>> banList = new HashMap<>();
		LinkedHashMap<String, List<HashMap<String, String>>> banListSections = new LinkedHashMap<>();

		banListSections.put("forbidden", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Forbidden")));
		banListSections.put("limited", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Limited")));
		banListSections.put("semiLimited", Card.toHashMap(bannedCardsService.getBanListByBanStatus(startDate, "Semi-Limited")));

		banList.put("bannedCards", banListSections);

		return new ResponseEntity<>(banList, HttpStatus.OK);

	}
}
