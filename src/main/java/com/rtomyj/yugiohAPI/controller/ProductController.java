package com.rtomyj.yugiohAPI.controller;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.helper.enumeration.products.ProductType;
import com.rtomyj.yugiohAPI.model.product.Product;
import com.rtomyj.yugiohAPI.model.product.Products;
import com.rtomyj.yugiohAPI.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(path = "/api/v1", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController
{

	private ProductService availablePacksService;
	private HttpServletRequest request;


	@Autowired
	public ProductController(final ProductService availablePacksService, final HttpServletRequest request)
	{
		this.availablePacksService = availablePacksService;
		this.request = request;
	}



	@GetMapping("/products/{locale}")
	public ResponseEntity<Products> getProductsByLocale(@PathVariable("locale") final String locale)
	{
		return ResponseEntity.ok(availablePacksService.getProductsByLocale(locale));
	}



	@GetMapping("/products/{productType}/{locale}")
	public ResponseEntity<Products> getProduct(
			@PathVariable("productType") final ProductType productType
			, @PathVariable("locale") final String locale)
	{
		log.info(productType.toString());
		return ResponseEntity.ok(availablePacksService.getAvailablePacks(productType, locale));
	}



	// todo: add validation/prevent null pointer exception when productid is invalid
	@GetMapping("/product/{productId}/{locale}")
	public ResponseEntity<Product> getProduct(
			@PathVariable("productId") final String productId
			, @PathVariable("locale") final String locale)
	{
		log.info(productId);
		return ResponseEntity.ok(availablePacksService.getPack(productId, locale.toUpperCase()));
	}

}