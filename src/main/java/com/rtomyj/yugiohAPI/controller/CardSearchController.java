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
	public ResponseEntity<List<Card>> postMethodName(@RequestParam(name = "cId", required = false, defaultValue="") String cardId
		, @RequestParam(name = "cName", required = false, defaultValue="") String cardName
		, @RequestParam(name = "cAttribute", required = false, defaultValue="") String cardAttribute
		, @RequestParam(name = "cColor", required = false, defaultValue="") String cardColor
		, @RequestParam(name = "mType", required = false, defaultValue="") String monsterType
		, @RequestParam(name = "saveBandwidth", required = false, defaultValue = "true") boolean saveBandwidth)throws YgoException
	{
		final List<Card> searchResult = cardService.getCardSearchResults(cardId, cardName, cardAttribute, cardColor, monsterType, saveBandwidth);

		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}

}