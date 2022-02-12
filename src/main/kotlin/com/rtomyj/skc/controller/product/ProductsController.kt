package com.rtomyj.skc.controller.product

import com.rtomyj.skc.constant.SKCRegex
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.enums.ProductType
import com.rtomyj.skc.model.product.Products
import com.rtomyj.skc.service.product.ProductService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@RestController
@RequestMapping(path = ["/products"], produces = ["application/json; charset=UTF-8"])
@Api(tags = [SwaggerConstants.TAG_PRODUCT_TAG_NAME])
class ProductsController @Autowired constructor(private val availableProductsService: ProductService) :
	YgoApiBaseController() {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	@GetMapping("/{locale}")
	@ApiOperation(
		value = "Retrieve all products for a given locale.",
		response = Products::class,
		responseContainer = "Object"
	)
	@ApiResponses(
		value = [
			ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE),
			ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE),
			ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
		]
	)
	fun getProductsByLocale(
		@ApiParam(
			value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
			example = "en"
		) @Pattern(
			regexp = SKCRegex.LOCALE,
			message = "Locale is formatted incorrectly"
		) @NotNull @PathVariable("locale") locale: String
	): ResponseEntity<Products> {
		log.info("Retrieving all products for given locale: {}", locale)

		return ResponseEntity.ok(availableProductsService.getAllProductsWithLocale(locale))
	}


	@GetMapping("/{productType}/{locale}")
	@ApiOperation(
		value = "Retrieve products that fit a certain product type and locale.",
		response = Products::class,
		responseContainer = "Object"
	)
	@ApiResponses(
		value = [
			ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE),
			ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE),
			ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
		]
	)
	fun getAllProductsForProductTypeAndLocale(
		@ApiParam(
			value = "A specific product type used to limit results.",
			example = "pack"
		)
		@NotNull
		@Pattern(
			regexp = SKCRegex.PRODUCT_TYPE,
			message = "Product Type not formatted correctly"
		)
		@PathVariable("productType") productType: ProductType,
		@ApiParam(
			value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
			example = "en"
		)
		@NotNull
		@Pattern(
			regexp = SKCRegex.LOCALE,
			message = "Locale is formatted incorrectly"
		)
		@PathVariable("locale") locale: String
	): ResponseEntity<Products> {
		log.info("Retrieving all products categorized as {} product type for locale {}", productType, locale)

		return ResponseEntity.ok(availableProductsService.getProductsUsingLocaleAndProductType(productType, locale))
	}
}