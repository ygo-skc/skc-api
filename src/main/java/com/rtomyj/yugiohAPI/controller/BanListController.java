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
		cards += Card.toJSON( (ArrayList<Card>) banListRepository.getForbiddenCards(startDate) );
		cards += "\n ] ";

		/*  Limited cards */
		cards += ", \"limited\": [\n";;
		cards += Card.toJSON( (ArrayList<Card>) banListRepository.getLimitedCards(startDate) );
		cards += "\n ] ";

		/*  Semi-Limited cards */
		cards += ", \"semiLimited\": [\n";
		cards += Card.toJSON( (ArrayList<Card>) banListRepository.getSemiLimitedCards(startDate) );
		cards += "\n ] ";

		cards += "\n } }";
		System.out.println("Fetched ban list");
		return cards;
	}

	@RequestMapping(path="ban_list/startDates")
	public String startDatesOfBanLists()
	{
		ArrayList<String> banStartDates = (ArrayList<String>) banListRepository.getBanListStartDates();


		String dates = "";
		dates += "{\n\"banListStartDates\": [";
		for (String date: banStartDates)
	{
			dates += "\"";
			dates += date;
			dates += "\",";
		}
		dates = dates.substring(0, dates.length() - 1);
		dates += "]\n}";

		return dates;
	}

}
