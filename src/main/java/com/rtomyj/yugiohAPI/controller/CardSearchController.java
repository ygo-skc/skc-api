package com.rtomyj.yugiohAPI.controller;

import java.util.List;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.model.CardSearch;
import com.rtomyj.yugiohAPI.service.CardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(path = "/api/v1/card/search", produces = "application/json; charset=UTF-8")
@Slf4j
public class CardSearchController
{
	@Autowired
	private CardService cardService;

	@PostMapping()
	public ResponseEntity<List<Card>> postMethodName(@RequestBody CardSearch searchQuery) throws YgoException {
		final List<Card> searchResult = cardService.getCardSearchResults(searchQuery.getCardName());

		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}

}