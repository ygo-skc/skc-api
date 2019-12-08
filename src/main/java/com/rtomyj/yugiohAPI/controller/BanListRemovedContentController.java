package com.rtomyj.yugiohAPI.controller;

import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.service.BanListDiffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${ygo.endpoints.ban-list-removed-cards}")
public class BanListRemovedContentController {
	@Autowired
	BanListDiffService banListDiffService;

	@GetMapping(path = "/{banListDate}")
	public List<Map<String, String>> getRemovedContent(@PathVariable(name = "banListDate") String banListDate)
	{
		return banListDiffService.getRemovedContentOfBanList(banListDate);
	}
}