package com.rtomyj.yugiohAPI.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.dao.Dao.Status;
import com.rtomyj.yugiohAPI.service.BanListDiffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${ygo.endpoints.ban-list-new-cards}", produces = "application/json; charset=UTF-8")
public class BanListNewCardsController
{
	@Autowired
	private BanListDiffService banListDiffService;

	@GetMapping(path = "/{banListDate}")
	public ResponseEntity<Map<String, Object>> getNewContent(@PathVariable String banListDate)
	{
		Map<String, List<Map<String, String>>> newCards = new LinkedHashMap<>();
		newCards.put(Status.FORBIDDEN.toString(), banListDiffService.getNewContentOfBanList(banListDate, "Forbidden"));
		newCards.put(Status.LIMITED.toString(), banListDiffService.getNewContentOfBanList(banListDate, "Limited"));
		newCards.put(Status.SEMI_LIMITED.toString(), banListDiffService.getNewContentOfBanList(banListDate, "Semi-Limited"));

		Map<String, Object> newCardsMeta = new HashMap<>();
		newCardsMeta.put("listRequested", banListDate);
		newCardsMeta.put("comparedTo", banListDiffService.getPreviousBanListDate(banListDate));
		newCardsMeta.put("newCards", newCards);
		return new ResponseEntity<>(newCardsMeta, HttpStatus.OK);
	}
}