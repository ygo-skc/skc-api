package com.rtomyj.yugiohAPI.controller.packs;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.model.pack.Pack;
import com.rtomyj.yugiohAPI.model.pack.Packs;
import com.rtomyj.yugiohAPI.service.packs.AvailablePacksService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(path = "/api/v1/packs", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
public class AvailablePacksController
{

	private AvailablePacksService availablePacksService;
	private HttpServletRequest request;


	@Autowired
	public AvailablePacksController(final AvailablePacksService availablePacksService, final HttpServletRequest request)
	{
		this.availablePacksService = availablePacksService;
		this.request = request;
	}



	@GetMapping
	public ResponseEntity<Packs> getPacks()
	{
		log.info("hello world");
		return ResponseEntity.ok(availablePacksService.getAvailablePacks());
	}



	@GetMapping("/{packId}")
	public ResponseEntity<Pack> getPack(@PathVariable("packId") final String packId)
	{
		return ResponseEntity.ok(availablePacksService.getPack(packId));
	}
}