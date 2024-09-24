package com.rtomyj.skc.browse

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.exception.SKCError
import com.rtomyj.skc.model.Products
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.util.constant.SKCRegex
import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.util.enumeration.ProductType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

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
    responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
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
  fun getProductsByLocale(
    @Parameter(
      description = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
      example = "en",
      schema = Schema(implementation = String::class)
    ) @Pattern(
      regexp = SKCRegex.LOCALE, message = "Locale is formatted incorrectly"
    ) @NotNull @PathVariable("locale") locale: String
  ): Mono<ResponseEntity<Products>> = ReactiveMDC.deferMDC(Mono.fromCallable {
    log.info("Retrieving all products for given locale: {}", locale)

    ResponseEntity.ok(availableProductsService.getAllProductsWithLocale(locale))
  })

  @GetMapping("/{productType}/{locale}")
  @Operation(
    summary = "Retrieve products that fit a certain product type and locale."
  )
  @ApiResponse(
    responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE
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
  fun getAllProductsForProductTypeAndLocale(
    @Parameter(
      description = "A specific product type used to limit results.",
      example = "pack",
      schema = Schema(implementation = String::class)
    ) @NotNull @Pattern(
      regexp = SKCRegex.PRODUCT_TYPE, message = "Product Type not formatted correctly"
    ) @PathVariable("productType") productType: ProductType, @Parameter(
      description = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
      example = "en",
      schema = Schema(implementation = String::class)
    ) @NotNull @Pattern(
      regexp = SKCRegex.LOCALE, message = "Locale is formatted incorrectly"
    ) @PathVariable("locale") locale: String
  ): Mono<ResponseEntity<Products>> = ReactiveMDC.deferMDC(Mono.fromCallable {
    log.info("Retrieving all products categorized as {} product type for locale {}", productType, locale)

    ResponseEntity.ok(availableProductsService.getProductsUsingLocaleAndProductType(productType, locale))
  })
}