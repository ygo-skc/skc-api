package com.rtomyj.skc.find

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.model.Product
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.constant.SKCRegex
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/product"], produces = ["application/json; charset=UTF-8"])
@Validated
@Tag(name = SwaggerConstants.TAG_PRODUCT_TAG_NAME)
class ProductController @Autowired constructor(private val availablePacksService: ProductService) {
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }

  @GetMapping("/{productId}/{locale}")
  @Operation(summary = "Fetch information about a particular Yu-Gi-Oh! product using product ID given by Konami.")
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  @ApiResponse(responseCode = "400", ref = "badRequest")
  @ApiResponse(responseCode = "422", ref = "unprocessableEntity")
  @ApiResponse(responseCode = "404", ref = "notFound")
  fun productInfo(
    @Parameter(ref = "productID")
    @NotNull @Pattern(regexp = SKCRegex.PRODUCT_ID, message = "Product ID is formatted incorrectly")
    @PathVariable("productId") productId: String,
    @Parameter(ref = "locale")
    @NotNull @Pattern(regexp = SKCRegex.LOCALE, message = "Locale is formatted incorrectly")
    @PathVariable("locale") locale: String,
  ): ResponseEntity<Mono<Product>> = ResponseEntity.ok(
    ReactiveMDC.deferMDC(availablePacksService
        .getSingleProductUsingLocale(
          productId, locale.uppercase(), MDC.get(AppConstants.CLIENT_IP_MDC)
        )
        .doOnSubscribe {
          log.info("Retrieving product info for product w/ ID: {} and locale: {}", productId, locale.uppercase())
        })
  )
}