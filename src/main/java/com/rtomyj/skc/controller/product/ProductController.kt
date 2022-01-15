package com.rtomyj.skc.controller.product

import com.rtomyj.skc.constant.SKCRegex
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.YgoApiBaseController
import com.rtomyj.skc.model.product.Product
import com.rtomyj.skc.service.product.ProductService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@RestController
@RequestMapping(path = ["/product"], produces = ["application/json; charset=UTF-8"])
@Validated
@Api(tags = [SwaggerConstants.TAG_PRODUCT_TAG_NAME])
class ProductController @Autowired constructor(private val availablePacksService: ProductService) :
	YgoApiBaseController() {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}


	@GetMapping("/{productId}/{locale}")
	@ApiOperation(
		value = "Fetch information about a particular Yu-Gi-Oh! product using product ID given by Konami.",
		response = Product::class,
		responseContainer = "Object"
	)
	@ApiResponses(
		value = [
			ApiResponse(code = 200, message = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE),
			ApiResponse(code = 400, message = SwaggerConstants.HTTP_400_SWAGGER_MESSAGE),
			ApiResponse(code = 404, message = SwaggerConstants.HTTP_404_SWAGGER_MESSAGE)
		]
	)
	fun productInfo(
		@ApiParam(
			value = "Unique identifier each Yu-Gi-Oh! product has. It is the 3 or 4 alpha numeric string found on every card.",
			example = "LOB"
		) @Pattern(
			regexp = SKCRegex.PRODUCT_ID,
			message = "Product ID is formatted incorrectly"
		) @NotNull @PathVariable("productId") productId:  String,
		@ApiParam(
			value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
			example = "en"
		) @Pattern(
			regexp = SKCRegex.LOCALE,
			message = "Locale is formatted incorrectly"
		) @NotNull @PathVariable("locale") locale: String
	): ResponseEntity<Product> {
		val localAsUpper = locale.uppercase()

		log.info("Retrieving product info for product w/ ID: {} and locale: {}", productId, localAsUpper)
		return ResponseEntity.ok(
			availablePacksService.getProductByLocale(
				productId,
				localAsUpper
			)
		)
	}
}