package com.rtomyj.yugiohAPI.controller;

import java.util.List;

import javax.validation.Valid;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.model.CardSearchCriteria;
import com.rtomyj.yugiohAPI.service.CardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/v1/card/search", produces = "application/json; charset=UTF-8")
public class CardSearchController
{
	@Autowired
	private CardService cardService;

	@PostMapping()
	public ResponseEntity<List<Card>> postMethodName(@Valid @RequestBody CardSearchCriteria cardSearchCriteria) throws YgoException {
		final List<Card> searchResult = cardService.getCardSearchResults(cardSearchCriteria);

		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}

}