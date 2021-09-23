package com.rtomyj.skc.controller.product;

import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.controller.YgoApiBaseController;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@RestController
@RequestMapping(path = "/product", produces = "application/json; charset=UTF-8")
@Validated
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


	@GetMapping("/{productId}/{locale}")
	@ApiOperation(value = "Fetch information about a particular Yu-Gi-Oh! product using product ID given by Konami."
			, response = Product.class
			, responseContainer = "Object")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
			, @ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE)
			, @ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
	})
	public ResponseEntity<Product> getProduct(
			@ApiParam(value = "Unique identifier each Yu-Gi-Oh! product has. It is the 3 or 4 alpha numeric string found on every card."
					, example = "LOB"
			)
			@PathVariable("productId")
			@NotNull
			@Size(min = 3, max = 4, message = "Product ID uses between 3-4 characters")
			@Pattern(regexp = "[a-zA-Z0-9]", message = "Product ID is formatted incorrectly")
			String productId
			, @ApiParam(value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION
					, example = "en"
			)
			@PathVariable("locale")
			@NotNull
			@Size(min = 2, max = 2, message = "Locale uses 2 characters")
			@Pattern(regexp = "[a-zA-Z]", message = "Locale value can only use letters")
			String locale
	)
	{
		return ResponseEntity.ok(availablePacksService.getProductByLocale(productId, locale.toUpperCase()));
	}
}