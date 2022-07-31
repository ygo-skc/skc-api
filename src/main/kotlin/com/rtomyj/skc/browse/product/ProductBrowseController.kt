package com.rtomyj.skc.browse.product

import com.rtomyj.skc.constant.SKCRegex
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.enums.ProductType
import com.rtomyj.skc.exception.YgoError
import com.rtomyj.skc.browse.product.model.Products
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = SwaggerConstants.TAG_PRODUCT_TAG_NAME)
class ProductBrowseController @Autowired constructor(private val availableProductsService: ProductBrowseService) :
	YgoApiBaseController() {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	@GetMapping("/{locale}")
	@Operation(
		summary = "Retrieve all products for a given locale."
	)
	@ApiResponse(
		responseCode = "200",
		description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
	)
	@ApiResponse(responseCode = "400",
		description = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE,
		content = [Content(schema = Schema(implementation = YgoError::class))]
	)
	@ApiResponse(
		responseCode = "404",
		description = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE,
		content = [Content(schema = Schema(implementation = YgoError::class))]
	)
	fun getProductsByLocale(
		@Parameter(
			description = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
			example = "en",
			schema = Schema(implementation = String::class)
		) @Pattern(
			regexp = SKCRegex.LOCALE,
			message = "Locale is formatted incorrectly"
		) @NotNull @PathVariable("locale") locale: String
	): ResponseEntity<Products> {
		log.info("Retrieving all products for given locale: {}", locale)

		return ResponseEntity.ok(availableProductsService.getAllProductsWithLocale(locale))
	}


	@GetMapping("/{productType}/{locale}")
	@Operation(
		summary = "Retrieve products that fit a certain product type and locale."
	)
	@ApiResponse(
		responseCode = "200",
		description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
	)
	@ApiResponse(responseCode = "400",
		description = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE,
		content = [Content(schema = Schema(implementation = YgoError::class))]
	)
	@ApiResponse(
		responseCode = "404",
		description = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE,
		content = [Content(schema = Schema(implementation = YgoError::class))]
	)
	fun getAllProductsForProductTypeAndLocale(
		@Parameter(
			description = "A specific product type used to limit results.",
			example = "pack",
			schema = Schema(implementation = String::class)
		)
		@NotNull
		@Pattern(
			regexp = SKCRegex.PRODUCT_TYPE,
			message = "Product Type not formatted correctly"
		)
		@PathVariable("productType") productType: ProductType,
		@Parameter(
			description = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
			example = "en",
			schema = Schema(implementation = String::class)
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