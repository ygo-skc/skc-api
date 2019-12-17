package com.rtomyj.yugiohAPI.controller;

import java.util.HashMap;
import java.util.Map;

import com.rtomyj.yugiohAPI.service.BanListDiffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${ygo.endpoints.ban-list-removed-cards}")
@CrossOrigin(origins = "*")
public class BanListRemovedContentController {
	@Autowired
	BanListDiffService banListDiffService;

	@GetMapping(path = "/{banListDate}")
	public Map<String, Object> getRemovedContent(@PathVariable(name = "banListDate") String banListDate)
	{
		final Map<String, Object> removedCards = new HashMap<>();

		removedCards.put("listRequested", banListDate);
		removedCards.put("comparedTo", banListDiffService.getPreviousBanListDate(banListDate));
		removedCards.put("removedCards", banListDiffService.getRemovedContentOfBanList(banListDate));

		return removedCards;
	}
}