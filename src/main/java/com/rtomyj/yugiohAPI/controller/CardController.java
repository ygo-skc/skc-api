package com.rtomyj.yugiohAPI.controller;
import java.util.Map;
import java.util.regex.Pattern;

import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.service.CardService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path="${ygo.endpoints.card-v1}", produces = "application/json; charset=UTF-8")
@RestController
@CrossOrigin(origins = "*")
public class CardController
{
	@Autowired
	private CardService cardService;

	@Autowired
	@Value("${ygo.endpoints.card-v1}")
	private String endPoint;

	private static final Logger LOG = LogManager.getLogger();


	@GetMapping("{cardID}")
	public ResponseEntity<Map<String, String>> getCard(@PathVariable("cardID") String cardID)
	{
		Pattern cardIDPattern = Pattern.compile("[0-9]{8}");
		if (!cardIDPattern.matcher(cardID).matches())
		{
			HttpStatus status = HttpStatus.BAD_REQUEST;
			LOG.info(String.format("%s/{ %s } hit - responding with: %s", endPoint, cardID, status));
			return new ResponseEntity<>(status);
		}


		Card foundCard = cardService.getCardInfo(cardID);
		if (foundCard == null)
		{
			HttpStatus status = HttpStatus.NO_CONTENT;
			LOG.info(String.format("%s/{ %s } hit - responding with: %s", endPoint, cardID, status));
			return new ResponseEntity<>(status);
		}


		HttpStatus status = HttpStatus.OK;
		LOG.info(String.format("%s/{ %s } hit - responding with: %s", endPoint, cardID, status));
		return new ResponseEntity<>(Card.toHashMap(foundCard), status);
	}
}