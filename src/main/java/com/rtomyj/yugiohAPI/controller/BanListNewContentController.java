package com.rtomyj.yugiohAPI.controller;

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
	public ResponseEntity<Integer> getNewContent(@PathVariable String banListDate)
	{
		return new ResponseEntity<>(newContentService.getBanListPosition(), HttpStatus.OK);
	}
}