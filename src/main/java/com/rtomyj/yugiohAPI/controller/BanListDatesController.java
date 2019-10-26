package com.rtomyj.yugiohAPI.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.service.BanListService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(path = "${ygo.endpoints.ban-list-dates-v1}", produces = "application/json; charset=utf-8")
@RestController
@CrossOrigin(origins = "*")
@Api(description = "Request information about current and past ban lists", tags = "Ban List")
public class BanListDatesController
{
	@Autowired
	private BanListService banListService;

	@Autowired
	@Value("${ygo.endpoints.ban-list-dates-v1}")
	private String endPoint;

	private static final Logger LOG = LogManager.getLogger();



	/**
	 * @return item
	 */
	@GetMapping()
	@ApiOperation(value = "Get dates of ban lists stored in database", response = ResponseEntity.class, tags = "Ban List")
	@ApiResponses( value = {
		@ApiResponse(code = 200, message = "OK")
	}
	)
	public ResponseEntity<Map<String, List<BanLists>>> startDatesOfBanLists()
	{
		List<BanLists> banStartDates = (ArrayList<BanLists>) banListService.getBanListStartDates();
		HashMap<String, List<BanLists>> response = new HashMap<>();
		response.put("banListStartDates", banStartDates);

		HttpStatus status = HttpStatus.OK;
		LOG.info(String.format("%s hit - responding with: %s", endPoint, status));
		return new ResponseEntity<>(response, status);
	}
}