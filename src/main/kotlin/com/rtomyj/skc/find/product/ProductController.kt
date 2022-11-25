package com.rtomyj.skc.find.product

import com.rtomyj.skc.browse.product.model.Product
import com.rtomyj.skc.exception.SKCError
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.constant.SKCRegex
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/product"], produces = ["application/json; charset=UTF-8"])
@Validated
@Tag(name = SwaggerConstants.TAG_PRODUCT_TAG_NAME)
class ProductController @Autowired constructor(private val availablePacksService: ProductService) :
	YgoApiBaseController() {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	@GetMapping("/{productId}/{locale}")
	@Operation(
		summary = "Fetch information about a particular Yu-Gi-Oh! product using product ID given by Konami."
	)
	@ApiResponse(
		responseCode = "200",
		description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
	)
	@ApiResponse(
		responseCode = "400",
		description = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE,
		content = [Content(schema = Schema(implementation = SKCError::class))]
	)
	@ApiResponse(
		responseCode = "404",
		description = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE,
		content = [Content(schema = Schema(implementation = SKCError::class))]
	)
	fun productInfo(
		@Parameter(
			description = "Unique identifier each Yu-Gi-Oh! product has. It is the 3 or 4 alpha numeric string found on every card.",
			example = "LOB",
			schema = Schema(implementation = String::class)
		) @Pattern(
			regexp = SKCRegex.PRODUCT_ID,
			message = "Product ID is formatted incorrectly"
		) @NotNull @PathVariable("productId") productId: String,
		@Parameter(
			description = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
			example = "en",
			schema = Schema(implementation = String::class)
		) @Pattern(
			regexp = SKCRegex.LOCALE,
			message = "Locale is formatted incorrectly"
		) @NotNull @PathVariable("locale") locale: String,
	): ResponseEntity<Product> {
		val localAsUpper = locale.uppercase()

		log.info("Retrieving product info for product w/ ID: {} and locale: {}", productId, localAsUpper)
		return ResponseEntity.ok(
			availablePacksService.getSingleProductUsingLocale(
				productId,
				localAsUpper,
				MDC.get(AppConstants.CLIENT_IP_MDC)
			)
		)
	}
}