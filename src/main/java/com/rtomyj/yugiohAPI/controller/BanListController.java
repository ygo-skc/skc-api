package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.Card;
import com.rtomyj.yugiohAPI.repository.BanListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.rtomyj.yugiohAPI.Card.toJSON;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BanListController {
	@Autowired
	BanListRepository banListRepository;

	@RequestMapping(path="/ban_list/{startDate}", method = RequestMethod.GET, produces = "application/json; charset=utf-8" )
	@ResponseBody
	public ResponseEntity<String> allCurrentBannedCards(@PathVariable String startDate)
	{
		Pattern datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		if (! datePattern.matcher(startDate).matches())
		{
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}


		String cards = "{ \"bannedCards\": {\n";

		/*  Forbidden cards */
		cards += "\"forbidden\": [\n";
		cards += toJSON( (ArrayList<Card>) banListRepository.getForbiddenCards("2019-07-15") );
		cards += "\n ] ";

		/*  Limited cards */
		cards += ", \"limited\": [\n";;
		cards += toJSON( (ArrayList<Card>) banListRepository.getLimitedCards("2019-07-15") );
		cards += "\n ] ";

		/*  Semi-Limited cards */
		cards += ", \"semiLimited\": [\n";
		cards += toJSON( (ArrayList<Card>) banListRepository.getSemiLimitedCards("2019-07-15") );
		cards += "\n ] ";

		cards += "\n } }";
		System.out.println("Fetched ban list");
		return cards;
	}

	@RequestMapping(path="ban_list/all")
	public String test()
	{
		return banListRepository.getBanListDates().toString();
	}

}
