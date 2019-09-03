package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.repository.BannedCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.regex.Pattern;


@RequestMapping(path = "banned_cards/v1", produces = "application/json; charset=utf-8")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BannedCardsController {
	@Autowired
	BannedCardsRepository bannedCardsRepository;

	@ResponseBody
	@GetMapping("/{startDate}")
	public ResponseEntity<String> getBannedCards(@PathVariable String startDate)
	{
		Pattern datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		if (! datePattern.matcher(startDate).matches())
		{
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}


		String cards = "{ \"bannedCards\": {\n";

		/*  Forbidden cards */
		cards += "\"forbidden\": [\n";
		cards += Card.toJSON( (ArrayList<Card>) bannedCardsRepository.getForbiddenCards(startDate) );
		cards += "\n ] ";

		/*  Limited cards */
		cards += ", \"limited\": [\n";;
		cards += Card.toJSON( (ArrayList<Card>) bannedCardsRepository.getLimitedCards(startDate) );
		cards += "\n ] ";

		/*  Semi-Limited cards */
		cards += ", \"semiLimited\": [\n";
		cards += Card.toJSON( (ArrayList<Card>) bannedCardsRepository.getSemiLimitedCards(startDate) );
		cards += "\n ] ";

		cards += "\n } }";
		System.out.println(String.format("Fetched ban list: %s", startDate));
		return new ResponseEntity<String>(cards, HttpStatus.OK);
	}
}
