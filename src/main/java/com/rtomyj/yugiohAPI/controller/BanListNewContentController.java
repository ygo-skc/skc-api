package com.rtomyj.yugiohAPI.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.service.BanListNewContentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ban_list_new_content", produces = "application/json; charset=UTF-8")
public class BanListNewContentController
{
	@Autowired
	private BanListNewContentService newContentService;

	@GetMapping(path = "/{banListDate}")
	public ResponseEntity<Map<String, List<String>>> getNewContent(@PathVariable String banListDate)
	{
		Map<String, List<String>> newContent = new HashMap<>();
		newContent.put("Forbidden", newContentService.getNewContentFromBanList(banListDate, "Forbidden"));
		newContent.put("Limited", newContentService.getNewContentFromBanList(banListDate, "Limited"));
		newContent.put("Semi-Limited", newContentService.getNewContentFromBanList(banListDate, "Semi-Limited"));
		return new ResponseEntity<>(newContent, HttpStatus.OK);
	}
}