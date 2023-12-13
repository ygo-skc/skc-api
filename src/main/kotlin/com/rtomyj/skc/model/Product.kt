package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.find.ProductController
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import java.util.*
import java.util.function.Consumer

@JsonInclude(
  JsonInclude.Include.NON_EMPTY
)
@Schema(
  implementation = Product::class,
  description = "Information about a Yu-Gi-Oh! product such as a booster pack or tin."
)
data class Product(
  @Schema(
    implementation = String::class,
    description = SwaggerConstants.PRODUCT_ID_DESCRIPTION,
  )
  val productId: String,
  @Schema(
    implementation = String::class,
    description = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION,
  )
  val productLocale: String
) : RepresentationModel<Product>() {

  companion object {
    private val controllerClass = ProductController::class.java
    fun setLinks(products: List<Product>) {
      products
          .forEach(Consumer { obj: Product -> obj.setLinks() })
    }
  }


  @Schema(
    implementation = String::class,
    description = SwaggerConstants.PRODUCT_NAME_DESCRIPTION,
  )
  var productName: String? = null

  @Schema(
    implementation = String::class,
    description = SwaggerConstants.PRODUCT_TYPE_DESCRIPTION,
  )
  var productType: String? = null

  @Schema(
    implementation = String::class,
    description = SwaggerConstants.PRODUCT_SUB_TYPE_DESCRIPTION,
  )
  var productSubType: String? = null

  @Schema(
    implementation = Date::class,
    description = SwaggerConstants.PRODUCT_RELEASE_DATE_DESCRIPTION,
    pattern = "yyyy-MM-dd",
    format = "yyyy-MM-dd",
  )
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  var productReleaseDate: Date? = null

  @Schema(
    implementation = Int::class,
    description = SwaggerConstants.PRODUCT_TOTAL_DESCRIPTION,
  )
  var productTotal: Int? = null

  @Schema(
    implementation = Map::class,
    description = SwaggerConstants.PRODUCT_RARITY_STATS_DESCRIPTION,
  )
  var productRarityStats: Map<String, Int>? = null

  @Schema(
    implementation = MutableList::class,
    description = SwaggerConstants.PRODUCT_CONTENT_DESCRIPTION,
  )
  var productContent: MutableList<ProductContent> = mutableListOf()


  private fun setLink() {
    this.add(
      WebMvcLinkBuilder
          .linkTo(
            WebMvcLinkBuilder
                .methodOn(controllerClass)
                .productInfo(
                  productId, productLocale
                )
          )
          .withSelfRel()
    )
  }

  fun setLinks() {
    setLink()
    if (productContent.isNotEmpty()) ProductContent.setLinks(productContent) // set links for pack contents
  }
}