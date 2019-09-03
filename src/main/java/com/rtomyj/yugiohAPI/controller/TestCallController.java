package com.rtomyj.yugiohAPI.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/testcall")
@RestController
public class TestCallController
{
	@GetMapping()
	public Map<String, HashMap> testCall(){
		HashMap<String, HashMap> outer = new HashMap();
		HashMap<String, String> inner = new HashMap<>();

		inner.put("key", "value");
		outer.put("inner", inner);

		return outer;
	}
}