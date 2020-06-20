package com.rtomyj.yugiohAPI.controller;

import java.util.List;

import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.service.CardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/v1/card/search", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = {"*"})
public class CardSearchController
{

	@Autowired
	private CardService cardService;

	@GetMapping
	public ResponseEntity<List<Card>> searchByCriteria(
			@RequestParam(name = "cId", required = false, defaultValue="") final String cardId
			, @RequestParam(name = "cName", required = false, defaultValue="") final String cardName
			, @RequestParam(name = "cAttribute", required = false, defaultValue="") final String cardAttribute
			, @RequestParam(name = "cColor", required = false, defaultValue="") final String cardColor
			, @RequestParam(name = "mType", required = false, defaultValue="") final String monsterType
			, @RequestParam(name = "limit", required = false, defaultValue = "-1") final int limit
			, @RequestParam(name = "saveBandwidth", required = false, defaultValue = "true") final boolean saveBandwidth)
			throws YgoException
	{

		final List<Card> searchResult = cardService.getCardSearchResults(cardId, cardName, cardAttribute, cardColor, monsterType, limit, saveBandwidth);
		return new ResponseEntity<>(searchResult, HttpStatus.OK);

	}

}