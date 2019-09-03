package com.rtomyj.yugiohAPI.controller;

import java.util.ArrayList;

import com.rtomyj.yugiohAPI.repository.BanListRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "ban_lists/v1", produces = "application/json; charset=utf-8")
@RestController
@CrossOrigin("http://localhost:3000")
public class BanListController
{
	@Autowired
	BanListRepository banListRepository;

	@GetMapping()
	public String startDatesOfBanLists() {
		ArrayList<String> banStartDates = (ArrayList<String>) banListRepository.getBanListStartDates();

		String dates = "";
		dates += "{\n\"banListStartDates\": [";
		for (String date : banStartDates) {
			dates += "\"";
			dates += date;
			dates += "\",";
		}
		dates = dates.substring(0, dates.length() - 1);
		dates += "]\n}";

		return dates;
	}
}