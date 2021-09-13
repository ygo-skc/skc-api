package com.rtomyj.skc.controller.product;

import com.rtomyj.skc.controller.YgoApiBaseController;
import com.rtomyj.skc.helper.constants.SwaggerConstants;
import com.rtomyj.skc.model.product.Product;
import com.rtomyj.skc.service.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/product", produces = "application/json; charset=UTF-8")
@Slf4j
@Api(tags = {SwaggerConstants.TAG_PRODUCT_TAG_NAME})
public class ProductController extends YgoApiBaseController
{

	private final ProductService availablePacksService;


	@Autowired
	public ProductController(final ProductService availablePacksService)
	{

		this.availablePacksService = availablePacksService;

	}


	// todo: add validation/prevent null pointer exception when productid is invalid
	@GetMapping("/{productId}/{locale}")
	@ApiOperation(value = "Fetch information about a particular Yu-Gi-Oh! product using product ID given by Konami."
			, response = Product.class
			, responseContainer = "Object")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.http200)
			, @ApiResponse(code = 400, message = SwaggerConstants.http400)
			, @ApiResponse(code = 404, message = SwaggerConstants.http404)
	})
	public ResponseEntity<Product> getProduct(
			@ApiParam(value = "Unique identifier each Yu-Gi-Oh! product has. It is the 3 or 4 alpha numeric string found on every card."
					, example = "LOB"
			) @PathVariable("productId") final String productId
			, @ApiParam(value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION
					, example = "en"
			) @PathVariable("locale") final String locale)
	{

		return ResponseEntity.ok(availablePacksService.getProductByLocale(productId, locale.toUpperCase()));

	}

}