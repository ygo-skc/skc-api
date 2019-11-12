package com.rtomyj.yugiohAPI.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.helper.LogHelper;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(path="${ygo.endpoints.card-v1}", produces = "application/json; charset=UTF-8")
@RestController
@CrossOrigin(origins = "*")
@Api(description = "", tags = "Card")
public class CardController
{
	@Autowired
	private CardService cardService;

	@Autowired
	private HttpServletRequest httpRequest;

	@Autowired
	@Value("${ygo.endpoints.card-v1}")
	private String endPoint;

	private static final Logger LOG = LogManager.getLogger();

	private static final Map<String, Card> CARD_CACHE = new HashMap<>();



	/**
	 * @return item
	 */
	@GetMapping("{cardID}")
	@ResponseBody
	@ApiOperation(value = "Get information about a specific card", response = ResponseEntity.class, tags = "Card")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure cardID is valid")
	})
	public ResponseEntity<Card> getCard(@PathVariable("cardID") String cardID)
	{
		String requestIP = httpRequest.getRemoteHost();

		Pattern cardIDPattern = Pattern.compile("[0-9]{8}");
		if (!cardIDPattern.matcher(cardID).matches())
		{
			HttpStatus status = HttpStatus.BAD_REQUEST;
			LOG.info(LogHelper.requestInfo(requestIP, endPoint, String.format("Responding with { %s }", status)));
			return new ResponseEntity<>(status);
		}


		Card cachedCard = CARD_CACHE.get(cardID);
		if (cachedCard != null)
		{
			HttpStatus status = HttpStatus.OK;
			LOG.info(LogHelper.requestInfo(requestIP, endPoint, String.format("Retrieved from cache: Responding with { %s }", status)));
			return new ResponseEntity<>(cachedCard, status);
		} else
		{
			Card foundCard = cardService.getCardInfo(cardID);
			if (foundCard == null)
			{
				HttpStatus status = HttpStatus.NO_CONTENT;
				LOG.info(LogHelper.requestInfo(requestIP, endPoint, String.format("Responding with { %s }", status)));
				return new ResponseEntity<>(status);
			}

			CARD_CACHE.put(cardID, foundCard);

			HttpStatus status = HttpStatus.OK;
			LOG.info(LogHelper.requestInfo(requestIP, endPoint, String.format("Responding with { %s }", status)));
			return new ResponseEntity<>(foundCard, status);
		}
	}
}