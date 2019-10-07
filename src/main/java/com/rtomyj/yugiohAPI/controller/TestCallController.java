package com.rtomyj.yugiohAPI.controller;

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

@RequestMapping(path = "${ygo.endpoints.test-call-v1}")
@RestController
@CrossOrigin(origins = "*")
public class TestCallController
{
	private static final Logger LOG = LogManager.getLogger();


	@Autowired
	@Value("${ygo.endpoints.test-call-v1}")
	private String endPoint;

	@GetMapping()
	public ResponseEntity<String> testCall()
	{
		LOG.info(String.format("%s hit", endPoint));
		return new ResponseEntity<>("API up and running.", HttpStatus.OK);
	}
}