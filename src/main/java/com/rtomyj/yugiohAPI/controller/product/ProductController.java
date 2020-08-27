package com.rtomyj.yugiohAPI.controller.product;

import javax.servlet.http.HttpServletRequest;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
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
@RequestMapping(path = "/product", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController extends YgoApiBaseController
{

	private static final String END_POINT = BASE_ENDPOINT + "/product";

	private final ProductService availablePacksService;


	@Autowired
	public ProductController(final HttpServletRequest request, final ProductService availablePacksService)
	{

		this.availablePacksService = availablePacksService;
		this.request = request;

	}


	// todo: add validation/prevent null pointer exception when productid is invalid
	@GetMapping("/{productId}/{locale}")
	public ResponseEntity<Product> getProduct(
			@PathVariable("productId") final String productId
			, @PathVariable("locale") final String locale)
	{
		log.info(productId);
		return ResponseEntity.ok(availablePacksService.getPack(productId, locale.toUpperCase()));
	}

}