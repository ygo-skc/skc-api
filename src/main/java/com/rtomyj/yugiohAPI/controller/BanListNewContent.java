package com.rtomyj.yugiohAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ban_list_new_content", produces = "application/json; charset=UTF-8")
public class BanListNewContent
{

	@GetMapping(path = "{banListDate}")
	public ResponseEntity<String> getNewContent(@PathVariable String banListDate)
	{
		return new ResponseEntity<>(banListDate, HttpStatus.OK);
	}
}