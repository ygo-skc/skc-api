package com.rtomyj.yugiohAPI.controller;

import java.util.LinkedHashMap;

import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.repository.CardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "card/v1/")
@RestController
public class CardController
{
	@Autowired
	CardRepository cardRepository;


	@GetMapping("/{cardName}")
	public ResponseEntity<LinkedHashMap<String, String>> getCard(@PathVariable("cardName") String cardName)
	{
		return new ResponseEntity<>(Card.toHashMap(cardRepository.getCardInfo(cardName)), HttpStatus.OK);
	}
}