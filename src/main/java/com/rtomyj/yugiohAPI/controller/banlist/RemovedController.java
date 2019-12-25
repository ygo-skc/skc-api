package com.rtomyj.yugiohAPI.controller.banlist;

import java.util.HashMap;
import java.util.Map;

import com.rtomyj.yugiohAPI.service.banlist.DiffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 *
 */
@RestController
@RequestMapping(path = "${ygo.endpoints.v1.ban-list-removed-cards}")
@CrossOrigin(origins = "*")
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class RemovedController {
	@Autowired
	DiffService banListDiffService;



	@GetMapping(path = "/{banListStartDate}")
	@ApiOperation(value = "Retrieve removed cards of a specific ban list given valid date a ban list started (use /api/v1/ban/dates to see a valid list)", response = ResponseEntity.class, tags = "Ban List")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "Request yielded no content"),
		@ApiResponse(code = 400, message = "Malformed request, make sure banListStartDate is valid")
	})
	public Map<String, Object> getRemovedContent(@PathVariable(name = "banListStartDate") String banListStartDate)
	{
		final Map<String, Object> removedCards = new HashMap<>();

		removedCards.put("listRequested", banListStartDate);
		removedCards.put("comparedTo", banListDiffService.getPreviousBanListDate(banListStartDate));
		removedCards.put("removedCards", banListDiffService.getRemovedContentOfBanList(banListStartDate));

		return removedCards;
	}
}