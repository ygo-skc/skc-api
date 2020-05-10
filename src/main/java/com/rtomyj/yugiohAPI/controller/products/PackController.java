package com.rtomyj.yugiohAPI.controller.products;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.model.pack.Pack;
import com.rtomyj.yugiohAPI.model.pack.Packs;
import com.rtomyj.yugiohAPI.service.products.PackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(path = "/api/v1/products/packs", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
public class PackController
{

	private PackService availablePacksService;
	private HttpServletRequest request;


	@Autowired
	public PackController(final PackService availablePacksService, final HttpServletRequest request)
	{
		this.availablePacksService = availablePacksService;
		this.request = request;
	}



	@GetMapping()
	public ResponseEntity<Packs> getPacks()
	{
		return ResponseEntity.ok(availablePacksService.getAvailablePacks());
	}



	@GetMapping("/{packId}/{locale}")
	public ResponseEntity<Pack> getPack(@PathVariable("packId") final String packId, @PathVariable("locale") final String locale)
	{
		return ResponseEntity.ok(availablePacksService.getPack(packId, locale.toUpperCase()));
	}
}