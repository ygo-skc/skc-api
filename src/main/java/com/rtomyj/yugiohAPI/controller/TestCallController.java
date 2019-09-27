package com.rtomyj.yugiohAPI.controller;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "api/v1/testcall")
@RestController
public class TestCallController
{
	@GetMapping()
	public ResponseEntity<String> testCall(){
		return new ResponseEntity<>("Test call worked", HttpStatus.OK);
	}
}